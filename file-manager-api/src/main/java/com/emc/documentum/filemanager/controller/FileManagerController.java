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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.api.FileManagerApi;
import com.emc.documentum.filemanager.dtos.out.Item;

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
}