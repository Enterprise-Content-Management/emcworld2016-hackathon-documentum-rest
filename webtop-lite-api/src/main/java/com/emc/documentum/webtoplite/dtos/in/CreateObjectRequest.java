/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.webtoplite.dtos.in;

import java.util.HashMap;
import java.util.Map;

public class CreateObjectRequest extends BaseRequest {
    private String name;
    private Map<String, Object> properties;
    private String content;

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    public String getContent() {
        return content;
    }
}
