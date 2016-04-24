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
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriUtils;

import com.emc.documentum.constants.AppRuntime;
import com.emc.documentum.constants.DocumentumProperties;
import com.emc.documentum.constants.LinkRelation;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.emc.documentum.restclient.model.HrefObject;
import com.emc.documentum.restclient.model.JsonEntry;
import com.emc.documentum.restclient.model.JsonFeed;
import com.emc.documentum.restclient.model.JsonObject;
import com.emc.documentum.restclient.model.PlainRestObject;
import com.emc.documentum.restclient.util.QueryParams;
import com.google.common.base.Strings;

@Component("DctmRestClient")
@PropertySource("classpath:application.properties")
public class DctmRestClient implements InitializingBean {

    public static final String DEFAULT_VIEW = "r_object_id,r_object_type,object_name,owner_name,r_creation_date,r_modify_date,r_content_size,a_content_type";
    public static final String DQL_QUERY_BY_ID = "select %s from dm_sysobject where r_object_id='%s'";
    public static final String DQL_QUERY_BY_PATH = "select %s from dm_sysobject where object_name='%s' and folder('%s')";
    public static final String DQL_QUERY_CABINET_BY_PATH = "select %s from dm_cabinet where object_name='%s'";

    @Autowired
    AppRuntime data;

    protected DctmRestTemplate restTemplate;
    protected DctmRestTemplate streamingTemplate;
    protected JsonObject repository;

    @Override
    public void afterPropertiesSet() throws Exception {
        restTemplate = new DctmRestTemplate(data.username, data.password, false);
        streamingTemplate = new DctmRestTemplate(data.username, data.password, true);

        // get home doc
        ResponseEntity<Map> homedoc = restTemplate.get(data.contextRootUri + "/services", Map.class);
        Map rootResources = (Map) homedoc.getBody().get("resources");

        //CODE FOR ROUND 2 -- BEGIN
        //CODE FOR ROUND 2 -- RESOLVE 'repositoriesUri', FROM 'rootResources'
        Map repositoriesEntry = (Map) rootResources.get(LinkRelation.REPOSITORIES);
        String repositoriesUri = (String) repositoriesEntry.get("href");
        //CODE FOR ROUND 2 -- END

        // get repositories
        ResponseEntity<JsonFeed> repositories = restTemplate
                .get(repositoriesUri, JsonFeed.class, QueryParams.INLINE, "true");
        for (JsonEntry repo : repositories.getBody().getEntries()) {
            if (data.repo.equals(repo.getTitle())) {
                repository = repo.getContentObject();
                break;
            }
        }
    }

    public List<JsonEntry> getAllCabinets(int pageNumber, int pageSize) {
        //CODE FOR ROUND 2 -- BEGIN
        //CODE FOR ROUND 2 -- RESOLVE 'cabinetsUrl', FROM 'repository'
        String cabinetsUrl = repository.getHref(LinkRelation.CABINETS);
        //CODE FOR ROUND 2 -- END

        return getJsonEntriesByUrl(pageNumber, pageSize, cabinetsUrl);
    }

    public List<JsonEntry> getChildren(String path, int pageNumber, int pageSize) {
        JsonObject folder = getObjectByPath(path);

        //CODE FOR ROUND 2 -- BEGIN
        //CODE FOR ROUND 2 -- RESOLVE 'childrenUrl', FROM 'folder'
        String childrenUrl = folder.getHref(LinkRelation.OBJECTS);
        //CODE FOR ROUND 2 -- END

        return getJsonEntriesByUrl(pageNumber, pageSize, childrenUrl);
    }

    public JsonObject createFolder(String parentId, String folderName) {
        JsonObject parentFolder = getObjectById(parentId);

        //CODE FOR ROUND 3 -- BEGIN
        //CODE FOR ROUND 3 -- RESOLVE 'childFoldersUrl', FROM 'parentFolder'
        String childFoldersUrl = parentFolder.getHref(LinkRelation.FOLDERS);;
        //CODE FOR ROUND3 -- END

        ResponseEntity<JsonObject> result = restTemplate.post(childFoldersUrl,
                new PlainRestObject("dm_folder", singleProperty(DocumentumProperties.OBJECT_NAME, folderName)),
                JsonObject.class,
                QueryParams.VIEW, DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject createContentfulDocument(JsonObject folder, byte[] data, String filename, String mime) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        //CODE FOR ROUND 4 -- BEGIN
        //CODE FOR ROUND 4 -- RESOLVE 'part1'
        PlainRestObject doc = new PlainRestObject("dm_document",
                singleProperty(DocumentumProperties.OBJECT_NAME, filename));
        MultiValueMap<String, String> part1Headers = new LinkedMultiValueMap<>();
        part1Headers.set("Content-Type", DctmRestTemplate.DCTM_VND_JSON_TYPE.toString());
        HttpEntity part1 = new HttpEntity<>(doc, part1Headers);
        //CODE FOR ROUND4 -- END
        parts.add("metadata", part1);

        //CODE FOR ROUND 4 -- BEGIN
        //CODE FOR ROUND 4 -- RESOLVE 'part2'
        MultiValueMap<String, String> part2Headers = new LinkedMultiValueMap<>();
        part2Headers.set("Content-Type", mime);
        HttpEntity part2 = new HttpEntity<>(data, part2Headers);
        //CODE FOR ROUND4 -- END
        parts.add("binary", part2);

        ResponseEntity<JsonObject> result = streamingTemplate.post(folder.getHref(LinkRelation.DOCUMENTS),
                parts,
                JsonObject.class);
        return result.getBody();
    }

    public ByteArrayResource getContentById(String docId) {
        JsonObject object = getObjectById(docId);
        JsonObject contentMeta = restTemplate.get(
                object.getHref(LinkRelation.PRIMARY_CONTENT),
                JsonObject.class,
                QueryParams.MEDIA_URL_POLICY, "local")
                .getBody();
        ResponseEntity<byte[]> content = streamingTemplate.get(contentMeta.getHref(LinkRelation.CONTENT_MEDIA), byte[].class);
        return new ByteArrayResource(
                content.getBody(),
                (String) contentMeta.getPropertyByName(DocumentumProperties.OBJECT_NAME),
                (String) contentMeta.getPropertyByName(DocumentumProperties.DOS_EXTENSION),
                content.getHeaders().getContentType(),
                content.getHeaders().getContentLength());
    }

    public JsonObject updateContent(JsonObject doc, byte[] data) {
        //CODE FOR ROUND 5 -- BEGIN
        //CODE FOR ROUND 5 -- IMPLEMENT
        String format = (String) doc.getPropertyByName(DocumentumProperties.CONTENT_TYPE);
        ResponseEntity<JsonObject> result = streamingTemplate.post(doc.getHref(LinkRelation.CONTENTS),
                data,
                JsonObject.class,
                QueryParams.OVERWRITE, "true",
                QueryParams.FORMAT, format);
        return result.getBody();
        //CODE FOR ROUND 5 -- END
    }

    public List<JsonEntry> simpleSearch(String terms, String path, int page, int itemsPerPage) {
        //CODE FOR ROUND 6 -- BEGIN
        //CODE FOR ROUND 6 -- IMPLEMENT
        String searchUrl = repository.getHref(LinkRelation.SEARCH);
        String[] queryParams = new String[0];
        if (("/".equals(path) || Strings.isNullOrEmpty(path))) {
            queryParams = new String[] {
                    QueryParams.Q, urlEncodeQueryParam(terms),
                    QueryParams.VIEW, DEFAULT_VIEW,
                    QueryParams.PAGE, String.valueOf(page),
                    QueryParams.ITEMS_PER_PAGE, String.valueOf(itemsPerPage)
            };
        } else {
            queryParams = new String[] {
                    QueryParams.Q, urlEncodeQueryParam(terms),
                    QueryParams.VIEW, DEFAULT_VIEW,
                    QueryParams.PAGE, String.valueOf(page),
                    QueryParams.ITEMS_PER_PAGE, String.valueOf(itemsPerPage),
                    QueryParams.LOCATIONS, urlEncodePathParam(path)
            };
        }
        //CODE FOR ROUND 6 -- END
        ResponseEntity<JsonFeed> result = restTemplate.get(searchUrl,
                JsonFeed.class,
                queryParams);
        return result.getBody().getEntries();
    }

    public JsonObject update(JsonObject object, Map<String, Object> newProperties) {
        ResponseEntity<JsonObject> result = restTemplate.post(object.getHref(LinkRelation.SELF),
                new PlainRestObject(null, newProperties),
                JsonObject.class,
                QueryParams.VIEW, DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject copy(JsonObject object, JsonObject targetFolder) {
        ResponseEntity<JsonObject> result = restTemplate.post(targetFolder.getHref(LinkRelation.OBJECTS),
                new HrefObject(object.getHref(LinkRelation.EDIT)),
                JsonObject.class,
                QueryParams.VIEW, DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject move(JsonObject object, JsonObject targetObject) {
        JsonFeed parentLinks = restTemplate.get(
                object.getHref(LinkRelation.PARENT_LINKS),
                JsonFeed.class,
                QueryParams.INLINE, "false")
                .getBody();
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
                QueryParams.DELETE_NON_EMPTY, String.valueOf(recursive),
                QueryParams.DELETE_ALL_LINKS, String.valueOf(recursive),
                QueryParams.DELETE_ALL_VERSIONS, "all");
    }

    public JsonObject getObjectById(String id) {
        JsonObject object = querySingleObjectById(id);
        ResponseEntity<JsonObject> result = restTemplate.get(object.getHref(LinkRelation.SELF),
                JsonObject.class,
                QueryParams.VIEW, DEFAULT_VIEW);
        return result.getBody();
    }

    public JsonObject getObjectByPath(String path) {
        JsonObject object = querySingleObjectByPath(path);
        ResponseEntity<JsonObject> result = restTemplate.get(object.getHref(LinkRelation.SELF),
                JsonObject.class,
                QueryParams.VIEW, DEFAULT_VIEW);
        return result.getBody();
    }

    private Map<String, Object> singleProperty(String property, String value) {
        return Collections.<String, Object>singletonMap(property, value);
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
                        QueryParams.DQL, urlEncodeQueryParam(dql));
        List<JsonEntry> entries = response.getBody().getEntries();
        if (entries == null || entries.size() == 0)
            throw new RuntimeException("No object for dql: " + dql);
        if (entries.size() > 1) {
            throw new RuntimeException("Ambiguous objects for dql: " + dql);
        }
        return entries.get(0).getContentObject();
    }

    private static String urlEncodeQueryParam(String q) {
        try {
            return UriUtils.encodeQueryParam(q, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String urlEncodePathParam(String q) {
        try {
            return UriUtils.encodePathSegment(q, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<JsonEntry> getJsonEntriesByUrl(int pageNumber, int pageSize, String childrenUrl) {
        ResponseEntity<JsonFeed> response =
                restTemplate.get(childrenUrl, JsonFeed.class,
                        QueryParams.INLINE, "true",
                        QueryParams.VIEW, DEFAULT_VIEW,
                        QueryParams.PAGE, String.valueOf(pageNumber),
                        QueryParams.ITEMS_PER_PAGE, String.valueOf(pageSize));
        return response.getBody().getEntries();
    }
}
