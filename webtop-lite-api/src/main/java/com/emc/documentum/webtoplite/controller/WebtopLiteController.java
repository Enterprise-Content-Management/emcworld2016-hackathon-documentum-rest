/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.webtoplite.controller;

import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.emc.documentum.webtoplite.api.WebtopLiteApi;
import com.emc.documentum.webtoplite.dtos.in.BaseRequest;
import com.emc.documentum.webtoplite.dtos.in.CreateObjectRequest;
import com.emc.documentum.webtoplite.dtos.out.Collection;
import com.emc.documentum.webtoplite.dtos.out.CommonResult;
import com.emc.documentum.webtoplite.dtos.out.Data;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.google.common.base.Strings;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WebtopLiteController extends BaseController {

    @Autowired
    WebtopLiteApi webtopLiteApi;

    @RequestMapping(value = "/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection testUrl() throws DocumentumException {
        return webtopLiteApi.getAllCabinets(1, 5);
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
            result = webtopLiteApi.getAllCabinets(pageNumber, pageSize);
        } else {
            result = webtopLiteApi.getChildren(path, pageNumber, pageSize);
        }
        return result;
    }

    @RequestMapping(value = "/createFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult createFolder(@RequestBody CreateObjectRequest request) throws DocumentumException {
        webtopLiteApi.createFolderByParentId(request.getParentId(), request.getName());
        return successResponse();
    }

    @RequestMapping(value = "/renameUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult rename(@RequestBody BaseRequest request) throws DocumentumException {
        webtopLiteApi.renameByPath(request.getPath(), request.getNewPath());
        return successResponse();
    }

    @RequestMapping(value = "/moveUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult move(@RequestBody BaseRequest request) throws DocumentumException {
        for (String id : request.getIds()) {
            webtopLiteApi.moveObject(id, request.getNewPath());
        }
        return successResponse();
    }

    @RequestMapping(value = "/copyUrl",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult copy(@RequestBody BaseRequest request) throws DocumentumException {
        for (String id : request.getIds()) {
            webtopLiteApi.copyObject(id, request.getNewPath());
        }
        return successResponse();
    }

    @RequestMapping(value = "/editUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult editContent(@RequestBody CreateObjectRequest request) throws DocumentumException {
        webtopLiteApi.updateContent(request.getId(), request.getContent());
        return successResponse();
    }

    @RequestMapping(value = "/deleteFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult deleteFolderUrl(@RequestBody BaseRequest request) throws DocumentumException {
        for (String id : request.getIds()) {
            //TODO should get this boolean from UI
            webtopLiteApi.deleteObjectById(id, false);
        }
        return successResponse();
    }

    @RequestMapping(value = "/document/content/{documentId}",
            method = RequestMethod.GET)
    public ResponseEntity<byte[]> getContentByDocId(@PathVariable(value = "documentId") String documentId)
            throws DocumentumException {
        ByteArrayResource content = webtopLiteApi.getContentById(documentId);

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
        ByteArrayResource content = webtopLiteApi.getContentById(documentId);
        return new Data(content.getData(), content.getMime().toString());
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
                    webtopLiteApi.uploadContent(targetFolderPath, next.getInputStream(), filename, mime);
                }
            }
            return successResponse();
        } catch (ServletException | IOException e) {
            throw new DocumentumException("Fail to receive multipart file upload.", e);
        }
    }

    @RequestMapping(value = "/searchUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection search(@RequestBody BaseRequest request) throws DocumentumException {
        Collection result = webtopLiteApi.search(
                request.getParam("terms"),
                request.getPath(),
                request.getIntParam("pageNumber", 1),
                request.getIntParam("pageSize", 100));
        return result;
    }

    private boolean isRoot(String path) {
        return Strings.isNullOrEmpty(path) || "/".equals(path);
    }
}