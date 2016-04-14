package com.emc.documentum.filemanager.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.dtos.out.CommonResult;

public abstract class BaseController {

    protected static final Log LOGGER = LogFactory.getLog(FileManagerController.class);

    static CommonResult commonResponse() {
        return new CommonResult(true, null, null);
    }

    static CommonResult errorResponse(String error) {
        return new CommonResult(true, error, null);
    }

    @Component
    @ControllerAdvice(annotations = RestController.class)
    public static class CommonExceptionHandler {

        @ExceptionHandler(DocumentumException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public CommonResult docuemntumException(DocumentumException e) {
            LOGGER.error(e);
            return errorResponse(e.getLocalizedMessage());
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public CommonResult generalException(Exception e) {
            LOGGER.error(e);
            return errorResponse(e.getLocalizedMessage());
        }

    }

}
