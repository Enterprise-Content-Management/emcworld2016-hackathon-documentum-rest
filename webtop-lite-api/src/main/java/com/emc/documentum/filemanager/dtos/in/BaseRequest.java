/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.in;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {
    // api operation
    private String action;

    // source object(s)
    private String path;
    private String id;
    private String[] ids;

    // target objects
    private String newPath;
    private String parentId;

    // additional parameters
    private Map<String, Object> params;

    public String getAction() {
        return action;
    }

    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

    public String[] getIds() {
        return ids;
    }

    public String getNewPath() {
        return newPath;
    }

    public String getParentId() {
        return parentId;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public String getParam(String key) {
        return getParams().containsKey(key) ? String.valueOf(getParams().get(key)) : null;
    }
}
