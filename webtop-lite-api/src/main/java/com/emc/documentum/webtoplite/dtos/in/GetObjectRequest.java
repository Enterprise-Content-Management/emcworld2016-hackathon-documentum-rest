/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.webtoplite.dtos.in;


public class GetObjectRequest extends BaseRequest {
    // client download new file name
    private String toFilename;
    // client download directory
    private String toDirectory;
    // client download new folder name
    private String toFoldername;

    public String getToFilename() {
        return toFilename;
    }

    public String getToDirectory() {
        return toDirectory;
    }

    public String getToFoldername() {
        return toFoldername;
    }
}
