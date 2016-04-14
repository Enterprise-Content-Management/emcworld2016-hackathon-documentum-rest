/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.in;

import java.util.ArrayList;
import java.util.List;

public class DeleteObjectsRequest {
    private List<String> items;

    public List<String> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
