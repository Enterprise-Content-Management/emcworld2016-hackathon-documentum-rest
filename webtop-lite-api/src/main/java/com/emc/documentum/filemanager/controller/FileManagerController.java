package com.emc.documentum.filemanager.controller;

import java.util.Iterator;
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
import com.emc.documentum.filemanager.api.FileManagerApi;
import com.emc.documentum.filemanager.dtos.in.CopyMoveObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.DeleteObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.ListObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.NewFolderRequest;
import com.emc.documentum.filemanager.dtos.in.RenameObjectRequest;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.CommonResult;
import com.emc.documentum.filemanager.dtos.out.Data;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.google.common.base.Strings;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FileManagerController extends BaseController {

    @Autowired
    FileManagerApi fileManagerApi;

    @RequestMapping(value = "/listUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection listURL(@RequestBody ListObjectsRequest request) throws DocumentumException {
        Collection result = null;
        String path = request.getParam("path");
        Integer pageNumber = 1;
        Integer pageSize = 20;
        try{
        	pageNumber = Integer.parseInt(request.getParam("pageNumber"));
        }catch(NumberFormatException e){
        	
        }
        
        try{
        	pageSize = Integer.parseInt(request.getParam("pageSize"));
        }catch(NumberFormatException e){
        	
        }
        
        if (Strings.isNullOrEmpty(path) || "/".equals(path)) {
            LOGGER.debug("Getting cabinets");
            result = fileManagerApi.getAllCabinets(pageNumber, pageSize) ;
        }
        else {
            LOGGER.debug("getting children for PATH : " + path);
            result = fileManagerApi.getChildren(path, pageNumber, pageSize);
        }
        return result;
	}

    @RequestMapping(value = "/createFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult createFolderUrl(@RequestBody NewFolderRequest request) throws DocumentumException {
        fileManagerApi.createFolderByParentId(request.getParentFolderId(), request.getName()) ;
        return commonResponse();
	}

    @RequestMapping(value = "/renameUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult renameUrl(@RequestBody RenameObjectRequest request) throws DocumentumException {
        fileManagerApi.renameByPath(request.getItem(), request.getNewItemPath());
        return commonResponse();
    }

    @RequestMapping(value = "/moveUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult moveUrl(@RequestBody CopyMoveObjectsRequest request) throws DocumentumException {
        //todo: ui has bugs on selecting the target path
        for (String id : request.getItems()) {
            fileManagerApi.moveObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/copyUrl",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult copyUrl(@RequestBody CopyMoveObjectsRequest request) throws DocumentumException {
        for (String id : request.getItems()) {
            fileManagerApi.copyObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/deleteFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult deleteFolderUrl(@RequestBody DeleteObjectsRequest request) throws DocumentumException {
        for (String id : request.getItems()) {
            //TODO should get this boolean from UI
            fileManagerApi.deleteObjectById(id, false);
        }
        return commonResponse();
    }

    @RequestMapping(value = "/document/content/{documentId}",
            method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocumentContentById(@PathVariable(value="documentId")String documentId)
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
    public Data openDocumentById(@PathVariable(value="documentId")String documentId)
            throws DocumentumException {
        ByteArrayResource content = fileManagerApi.getContentById(documentId);
        return new Data(content.getData());
    }

    @RequestMapping(value = "/uploadUrl",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult uploadUrl(MultipartHttpServletRequest request) throws DocumentumException {
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
            return commonResponse();
        } catch (Exception e) {
            throw new DocumentumException("Fail to receive multipart file upload.", e);
        }
    }


    //todo//////////////////////////////////////////////////////////////////////////////
    //todo////////////// above methods are refactored - 1st round //////////////////////
    //todo//////////////     todo for below methods   - 1st round //////////////////////
    //todo//////////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/editUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult editUrl() {
        return commonResponse();
    }

    @RequestMapping(value = "/permissionsUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult permissionsUrl() {
        return commonResponse();
	}

    @RequestMapping(value = "/extractUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult extractUrl() {
        return commonResponse();
	}
}