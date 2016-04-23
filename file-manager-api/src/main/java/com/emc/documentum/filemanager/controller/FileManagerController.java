/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.controller;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.api.FileManagerApi;
import com.emc.documentum.filemanager.dtos.in.BaseRequest;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.google.common.base.Strings;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FileManagerController extends BaseController {

    @Autowired
    FileManagerApi fileManagerApi;

    @RequestMapping(value = "/about",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Item about() throws DocumentumException {
        Item about = new Item();
        about.setName("Welcome to Documentum AngularJS File Manager API Server. ");
        about.setDate(new DateFormatter("yyyy/MM/dd hh:mm:ss a").print(new Date(), Locale.ENGLISH));
        return about;
    }

    @RequestMapping(value = "/listUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection listObjects(@RequestBody BaseRequest request) throws DocumentumException {
        Collection result = null;
        String path = request.getPath();
        Integer pageNumber = request.getIntParam("pageNumber", 1);
        Integer pageSize = request.getIntParam("pageSize", 100);
        if (isRoot(path)) {
            result = fileManagerApi.getAllCabinets(pageNumber, pageSize);
        } else {
            result = fileManagerApi.getChildren(path, pageNumber, pageSize);
        }
        return result;
    }

    private boolean isRoot(String path) {
        return Strings.isNullOrEmpty(path) || "/".equals(path);
    }
}