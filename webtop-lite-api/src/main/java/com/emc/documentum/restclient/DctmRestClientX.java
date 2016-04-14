/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.restclient;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import com.emc.documentum.constants.DCCoreRestConstants;
import com.emc.documentum.constants.LinkRelation;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.emc.documentum.restclient.model.HrefObject;
import com.emc.documentum.restclient.model.JsonEntry;
import com.emc.documentum.restclient.model.JsonFeed;
import com.emc.documentum.restclient.model.JsonObject;
import com.emc.documentum.restclient.model.PlainRestObject;

@Component("DctmRestClientX")
@PropertySource("classpath:application.properties")
public class DctmRestClientX implements InitializingBean{

    public static final String DEFAULT_VIEW = "r_object_id,r_object_type,object_name,owner_name,r_creation_date,r_modify_date,r_content_size";
    public static final String DQL_QUERY_BY_ID = "select %s from dm_sysobject where r_object_id='%s'";
    public static final String DQL_QUERY_BY_PATH = "select %s from dm_sysobject where object_name='%s' and folder('%s')";
    public static final String DQL_QUERY_CABINET_BY_PATH = "select %s from dm_cabinet where object_name='%s'";

    @Autowired
    DCCoreRestConstants data;

    protected DctmRestTemplate restTemplate;
    protected DctmRestTemplate streamingTemplate;
    protected JsonObject repository;

    public List<JsonEntry> getAllCabinets(int pageNumber, int pageSize) {
        String cabinetsUrl = repository.getHref(LinkRelation.CABINETS);
        return getJsonEntriesByUrl(pageNumber, pageSize, cabinetsUrl);
    }

    public List<JsonEntry> getChildren(String path, int pageNumber, int pageSize) {
        JsonObject folder = getObjectByPath(path);
        String childrenUrl = folder.getHref(LinkRelation.OBJECTS);
        return getJsonEntriesByUrl(pageNumber, pageSize, childrenUrl);
    }

    public JsonObject createFolder(String parentId, String folderName) {
        JsonObject parentFolder = getObjectById(parentId);
        String childFoldersUrl = parentFolder.getHref(LinkRelation.FOLDERS);

        ResponseEntity<JsonObject> result = restTemplate.post(childFoldersUrl,
                new PlainRestObject("dm_folder", Collections.<String, Object>singletonMap("object_name", folderName)),
                JsonObject.class,
                "view", DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject update(JsonObject object, Map<String, Object> newProperties) {
        ResponseEntity<JsonObject> result = restTemplate.post(object.getHref(LinkRelation.SELF),
                new PlainRestObject(null, newProperties),
                JsonObject.class,
                "view", DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject copy(JsonObject object, JsonObject targetFolder) {
        ResponseEntity<JsonObject> result = restTemplate.post(targetFolder.getHref(LinkRelation.OBJECTS),
                new HrefObject(object.getHref(LinkRelation.EDIT)),
                JsonObject.class,
                "view", DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject move(JsonObject object, JsonObject targetObject) {
        JsonFeed parentLinks = restTemplate.get(
                object.getHref(LinkRelation.PARENT_LINKS),
                JsonFeed.class,
                "inline", "false").getBody();
        String parentLinkUrl = parentLinks.getEntries().get(0).getContentSrc();
        ResponseEntity<JsonObject> result = restTemplate.put(
                parentLinkUrl,
                new HrefObject(targetObject.getHref(LinkRelation.EDIT)),
                JsonObject.class);
        return result.getBody();
    }

    public void deleteObjectById(String id, boolean recursive) {
        JsonObject object = querySingleObjectById(id);
        restTemplate.delete(object.getHref(LinkRelation.SELF),
                "delete-non-empty", String.valueOf(recursive),
                "delete-all-links", String.valueOf(recursive),
                "delete-version", "all");
    }

    public JsonObject getObjectById(String id) {
        JsonObject object = querySingleObjectById(id);
        ResponseEntity<JsonObject> result = restTemplate.get(object.getHref(LinkRelation.SELF),
                JsonObject.class,
                "view", DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject getObjectByPath(String path) {
        JsonObject object = querySingleObjectByPath(path);
        ResponseEntity<JsonObject> result = restTemplate.get(object.getHref(LinkRelation.SELF),
                JsonObject.class,
                "view", DEFAULT_VIEW);
        return result.getBody();
    }

    public ByteArrayResource getContentById(String docId) {
        JsonObject object = getObjectById(docId);
        JsonObject contentMeta = restTemplate.get(
                object.getHref(LinkRelation.PRIMARY_CONTENT),
                JsonObject.class,
                "media-url-policy", "local")
            .getBody();
        ResponseEntity<byte[]> content = streamingTemplate.get(contentMeta.getHref(LinkRelation.CONTENT_MEDIA), byte[].class);
        return new ByteArrayResource(
                content.getBody(),
                (String) contentMeta.getPropertyByName("object_name"),
                (String) contentMeta.getPropertyByName("dos_extension"),
                content.getHeaders().getContentType(),
                content.getHeaders().getContentLength());
    }

    private JsonObject querySingleObjectById(String id) {
        String dql = String.format(DQL_QUERY_BY_ID, DEFAULT_VIEW, id);
        return querySingleObject(dql);
    }

    private JsonObject querySingleObjectByPath(String path) {
        String dql = null;
        if (path.lastIndexOf("/") == 0) {
            dql = String.format(DQL_QUERY_CABINET_BY_PATH, DEFAULT_VIEW, path.substring(1));
        } else {
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            String name = path.substring(path.lastIndexOf("/") + 1);
            dql = String.format(DQL_QUERY_BY_PATH, DEFAULT_VIEW, name, parentPath);
        }
        return querySingleObject(dql);
    }

    private JsonObject querySingleObject(String dql) {
        String dqlUrl = repository.getHref(LinkRelation.DQL);
        ResponseEntity<JsonFeed> response =
                restTemplate.get(dqlUrl, JsonFeed.class,
                        "dql", constructDqlParam(dql));
        List<JsonEntry> entries = response.getBody().getEntries();
        if (entries == null || entries.size() == 0)
            throw new RuntimeException("No object for dql: " + dql);
        if (entries.size() > 1) {
            throw new RuntimeException("Ambiguous objects for dql: " + dql);
        }
        return entries.get(0).getContentObject();
    }

    private static String constructDqlParam(String dql) {
        try {
            return UriUtils.encodeQueryParam(dql, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<JsonEntry> getJsonEntriesByUrl(int pageNumber, int pageSize, String childrenUrl) {
        ResponseEntity<JsonFeed> response =
                restTemplate.get(childrenUrl, JsonFeed.class,
                        "inline", "true",
                        "view", DEFAULT_VIEW,
                        "page", String.valueOf(pageNumber),
                        "items-per-page", String.valueOf(pageSize));
        return response.getBody().getEntries();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restTemplate = new DctmRestTemplate(data.username, data.password, false);
        streamingTemplate = new DctmRestTemplate(data.username, data.password, true);
        // get home doc
        ResponseEntity<Map> homedoc = restTemplate.get(data.contextRootUri + "/services", Map.class);
        Map rootResources = (Map) homedoc.getBody().get("resources");
        Map repositoriesEntry = (Map) rootResources.get(LinkRelation.REPOSITORIES);
        String repositoriesUri = (String) repositoriesEntry.get("href");

        // get repositories
        ResponseEntity<JsonFeed> repositories = restTemplate.get(repositoriesUri, JsonFeed.class, "inline", "true");
        for(JsonEntry repo : repositories.getBody().getEntries()) {
            if (data.repo.equals(repo.getTitle())) {
                repository = repo.getContentObject();
                break;
            }
        }
    }
}
