/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.in;

public class RenameObjectRequest {
    private String item;
    private String newItemPath;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNewItemPath() {
        return newItemPath;
    }

    public void setNewItemPath(String newItemPath) {
        this.newItemPath = newItemPath;
    }
}
