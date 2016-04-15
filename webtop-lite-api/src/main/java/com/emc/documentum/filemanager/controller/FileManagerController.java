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
import com.emc.documentum.filemanager.dtos.in.BaseRequest;
import com.emc.documentum.filemanager.dtos.in.CreateObjectRequest;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.CommonResult;
import com.emc.documentum.filemanager.dtos.out.Data;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.google.common.base.Strings;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FileManagerController extends BaseController {

    @RequestMapping(value = "/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection testUrl() throws DocumentumException {
        return fileManagerApi.getAllCabinets(1, 5);
    }

    @Autowired
    FileManagerApi fileManagerApi;

    @RequestMapping(value = "/listUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection listURL(@RequestBody BaseRequest request) throws DocumentumException {
        Collection result = null;
        String path = request.getPath();
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
    public CommonResult createFolderUrl(@RequestBody CreateObjectRequest request) throws DocumentumException {
        fileManagerApi.createFolderByParentId(request.getParentId(), request.getName()) ;
        return commonResponse();
	}

    @RequestMapping(value = "/renameUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult renameUrl(@RequestBody BaseRequest request) throws DocumentumException {
        fileManagerApi.renameByPath(request.getPath(), request.getNewPath());
        return commonResponse();
    }

    @RequestMapping(value = "/moveUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult moveUrl(@RequestBody BaseRequest request) throws DocumentumException {
        for (String id : request.getIds()) {
            fileManagerApi.moveObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/copyUrl",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult copyUrl(@RequestBody BaseRequest request) throws DocumentumException {
        for (String id : request.getIds()) {
            fileManagerApi.copyObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/editUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult editUrl(@RequestBody CreateObjectRequest request) throws DocumentumException {
        fileManagerApi.updateContent(request.getId(), request.getContent());
        return commonResponse();
    }

    @RequestMapping(value = "/deleteFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult deleteFolderUrl(@RequestBody BaseRequest request) throws DocumentumException {
        for (String id : request.getIds()) {
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
        return new Data(content.getData(), content.getMime().toString());
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


    @RequestMapping(value = "/permissionsUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult permissionsUrl() {
        return commonResponse();
	}

    @RequestMapping(value = "/extractUrl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult extractUrl() {
        return commonResponse();
	}
}