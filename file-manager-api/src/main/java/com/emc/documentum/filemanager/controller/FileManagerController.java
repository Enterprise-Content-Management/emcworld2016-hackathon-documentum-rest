/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.api.FileManagerApi;
import com.emc.documentum.filemanager.dtos.in.BaseRequest;
import com.emc.documentum.filemanager.dtos.in.CreateObjectRequest;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.CommonResult;
import com.emc.documentum.filemanager.dtos.out.Data;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.emc.documentum.restclient.model.ByteArrayResource;
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

    @RequestMapping(value = "/createFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult createFolder(@RequestBody CreateObjectRequest request) throws DocumentumException {

        //CODE FOR ROUND 3 -- BEGIN
        //CODE FOR ROUND 3 -- CALL 'fileManagerApi' to create folder.
        fileManagerApi.createFolderByParentId(request.getParentId(), request.getName());
        //CODE FOR ROUND 3 -- END
        return successResponse();
    }

    @RequestMapping(value = "/uploadUrl",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult uploadContent(MultipartHttpServletRequest request) throws DocumentumException {
        try {
            String targetFolderPath = null;
            Iterator<Part> parts = request.getParts().iterator();
            while (parts.hasNext()) {
                Part next = parts.next();
                if ("destination".equals(next.getName())) {
                    targetFolderPath = IOUtils.toString(next.getInputStream());
                } else {
                    String filename = next.getSubmittedFileName();
                    String mime = next.getContentType();
                    fileManagerApi.uploadContent(targetFolderPath, next.getInputStream(), filename, mime);
                }
            }
            return successResponse();
        } catch (ServletException | IOException e) {
            throw new DocumentumException("Fail to receive multipart file upload.", e);
        }
    }

    @RequestMapping(value = "/document/content/{documentId}",
            method = RequestMethod.GET)
    public ResponseEntity<byte[]> getContentByDocId(@PathVariable(value = "documentId") String documentId)
            throws DocumentumException {
        ByteArrayResource content = fileManagerApi.getContentById(documentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(content.getMime());
        headers.setContentLength(content.getLength());
        headers.setContentDispositionFormData("attachment", content.getNormalizedName());
        return new ResponseEntity<>(
                content.getData(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/document/open/{documentId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Data openContentByDocId(@PathVariable(value = "documentId") String documentId)
            throws DocumentumException {
        ByteArrayResource content = fileManagerApi.getContentById(documentId);
        return new Data(content.getData(), content.getMime().toString());
    }

    //TODO FOR ROUND 5 -- BEGIN
    //TODO FOR ROUND 5 -- RESOLVE @RequestMapping
    //TODO FOR ROUND 5 -- END
    public CommonResult editContent(@RequestBody CreateObjectRequest request) throws DocumentumException {
        fileManagerApi.updateContent(request.getId(), request.getContent());
        return successResponse();
    }

    private boolean isRoot(String path) {
        return Strings.isNullOrEmpty(path) || "/".equals(path);
    }
}