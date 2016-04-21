/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.out;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    private List<Item> result;

    public List<Item> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void setResult(List<Item> result) {
        this.result = result;
    }
}
