/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.webtoplite.transformation;

import java.util.List;

import com.emc.documentum.webtoplite.dtos.out.Collection;
import com.emc.documentum.webtoplite.dtos.out.Item;
import com.emc.documentum.restclient.model.JsonEntry;
import com.emc.documentum.restclient.model.JsonObject;

public final class CoreRestTransformation {

    private CoreRestTransformation() {
    }

    public static Collection convertCoreRSEntryList(List<JsonEntry> jsonEntryFeed) {
        Collection collection = new Collection();
        if (jsonEntryFeed != null) {
            for (JsonEntry jsonEntry : jsonEntryFeed) {
                collection.getResult().add(new Item(jsonEntry));
            }
        }
        return collection;
    }

    public static Item convertJsonObject(JsonObject jsonObject) {
        return new Item(jsonObject);
    }
}
