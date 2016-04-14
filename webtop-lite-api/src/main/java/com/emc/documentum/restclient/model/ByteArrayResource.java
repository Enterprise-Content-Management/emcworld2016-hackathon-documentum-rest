/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.restclient.model;

import org.springframework.http.MediaType;

public class ByteArrayResource {
    private String name;
    private String ext;
    private byte[] data;
    private MediaType mime;
    private long length;

    public ByteArrayResource(byte[] data, String name, String ext, MediaType mime, long length) {
        this.name = name;
        this.ext = ext;
        this.data = data;
        this.mime = mime;
        this.length = length;
    }

    public byte[] getData() {

        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public MediaType getMime() {
        return mime;
    }

    public void setMime(MediaType mime) {
        this.mime = mime;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getNormalizedName() {
        return name.endsWith(ext) ? name : String.format("%s.%s", name, ext);
    }
}
