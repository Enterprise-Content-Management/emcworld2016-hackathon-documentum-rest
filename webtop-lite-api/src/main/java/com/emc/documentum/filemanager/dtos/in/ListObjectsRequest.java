/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.in;

import java.util.HashMap;
import java.util.Map;

public class ListObjectsRequest {
    private Map<String, String> params;

    public Map<String, String> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getParam(String param) {
        return getParams().get(param);
    }
}
