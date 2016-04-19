package com.emc.documentum.webtoplite.controller;

/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.webtoplite.dtos.out.CommonResult;

public abstract class BaseController {

    static CommonResult successResponse() {
        return new CommonResult(true, null, null);
    }

    static CommonResult errorResponse(String error) {
        return new CommonResult(false, error, null);
    }

    static CommonResult errorResponse(String error, String details) {
        return new CommonResult(false, error, details);
    }

    @Component
    @ControllerAdvice(annotations = RestController.class)
    public static class CommonExceptionHandler {

        @ExceptionHandler(HttpClientErrorException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public CommonResult onHttpClientErrorException(HttpClientErrorException e) {
            String details = e.getResponseBodyAsString();
            return errorResponse(e.getLocalizedMessage(), details);
        }

        @ExceptionHandler(HttpServerErrorException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public CommonResult onHttpServerErrorException(HttpServerErrorException e) {
            String details = e.getResponseBodyAsString();
            return errorResponse(e.getLocalizedMessage(), details);
        }

        @ExceptionHandler(DocumentumException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public CommonResult onDocumentumException(DocumentumException e) {
            return errorResponse(e.getLocalizedMessage());
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public CommonResult onGeneralException(Exception e) {
            return errorResponse(e.getLocalizedMessage());
        }

        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public CommonResult onGeneralRuntimeException(RuntimeException e) {
            return errorResponse(e.getMessage());
        }
    }

}
