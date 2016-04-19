/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.webtoplite.dtos.out;

import com.emc.documentum.restclient.model.JsonEntry;
import com.emc.documentum.restclient.model.JsonObject;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {
    private String id;
    private String name;
    private String type;
    private Integer size;
    private String date;
    private String rights;

    public Item() {}

    public Item(JsonEntry entry) {
        setName(entry.getTitle());
        setDate(entry.getUpdated());

        JsonObject object = entry.getContentObject();
        if (object != null) {
            setType(convertType(object.getType()));
            setId((String) object.getPropertyByName("r_object_id"));
            setSize((Integer) object.getPropertyByName("r_content_size")) ;
            setRights("drwxr-xr-x") ;
        }
    }

    public Item(JsonObject object) {
        setName((String) object.getPropertyByName("object_name"));
        setDate((String) object.getPropertyByName("r_modify_date"));
        setType(convertType(object.getType()));
        setId((String) object.getPropertyByName("r_object_id"));
        setSize((Integer) object.getPropertyByName("r_content_size"));
        setRights("drwxr-xr-x") ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    private String convertType(String type) {
        return "dm_folder".equals(type) || "dm_cabinet".equals(type) ? "dir" : "file";
    }
}
