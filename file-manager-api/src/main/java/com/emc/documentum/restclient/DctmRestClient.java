/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.restclient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.emc.documentum.constants.AppRuntime;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.emc.documentum.restclient.model.JsonEntry;
import com.emc.documentum.restclient.model.JsonObject;

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
        // Not implemented
    }

    public List<JsonEntry> getAllCabinets(int pageNumber, int pageSize) {
        throw new RuntimeException("Not implemented.");
    }

    public List<JsonEntry> getChildren(String path, int pageNumber, int pageSize) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject createFolder(String parentId, String folderName) {
        throw new RuntimeException("Not implemented.");
    }

    private Map<String, Object> singleProperty(String property, String value) {
        return Collections.<String, Object>singletonMap(property, value);
    }

    public JsonObject update(JsonObject object, Map<String, Object> newProperties) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject copy(JsonObject object, JsonObject targetFolder) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject move(JsonObject object, JsonObject targetObject) {
        throw new RuntimeException("Not implemented.");
    }

    public void deleteObjectById(String id, boolean recursive) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject getObjectById(String id) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject getObjectByPath(String path) {
        throw new RuntimeException("Not implemented.");
    }

    public ByteArrayResource getContentById(String docId) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject createContentfulDocument(JsonObject folder, byte[] data, String filename, String mime) {
        throw new RuntimeException("Not implemented.");
    }

    public JsonObject updateContent(JsonObject doc, byte[] data) {
        throw new RuntimeException("Not implemented.");
    }

    public List<JsonEntry> simpleSearch(String terms, String path, int page, int itemsPerPage) {
        throw new RuntimeException("Not implemented.");
    }
}
