/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.dtos.out;

public class CommonResult {
    boolean success;
    String error;
    Object result;

    public CommonResult(boolean success, String error, Object result) {
        this.success = success;
        this.error = error;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String isError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
