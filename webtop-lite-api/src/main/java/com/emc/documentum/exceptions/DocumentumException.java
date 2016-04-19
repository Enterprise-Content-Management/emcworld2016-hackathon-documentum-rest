/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.exceptions;

public class DocumentumException extends Exception {

    public DocumentumException(String msg) {
        super(msg);
    }

    public DocumentumException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
