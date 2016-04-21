/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.out;

public class Data {
    private String mime;
    private byte[] data;

    public Data(byte[] data, String mime) {
        this.data = data;
        this.mime = mime;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}
