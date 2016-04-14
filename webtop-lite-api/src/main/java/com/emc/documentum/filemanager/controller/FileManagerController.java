package com.emc.documentum.filemanager.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.exceptions.RepositoryNotAvailableException;
import com.emc.documentum.filemanager.api.FileManagerApi;
import com.emc.documentum.filemanager.dtos.in.DeleteObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.ListObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.CopyMoveObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.NewFolderRequest;
import com.emc.documentum.filemanager.dtos.in.RenameObjectRequest;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.Data;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.google.common.base.Strings;

@CrossOrigin
@RestController
public class FileManagerController extends BaseController {

	private static final  Log LOGGER = LogFactory.getLog(FileManagerController.class);

    @Autowired
    FileManagerApi fileManagerApi;

	@RequestMapping(value = "/api/listUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection listURL(@RequestBody ListObjectsRequest request) throws DocumentumException {
        //todo: pagination params
        Collection result = null;
        String path = request.getParam("path");
        if (Strings.isNullOrEmpty(path) || "/".equals(path)) {
            LOGGER.debug("Getting cabinets");
            result = fileManagerApi.getAllCabinets(1, 20) ;
        }
        else {
            LOGGER.debug("getting children for PATH : " + path);
            result = fileManagerApi.getChildren(path, 1, 20);
        }
        return result;
	}

	@RequestMapping(value = "/api/createFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String createFolderUrl(@RequestBody NewFolderRequest request) throws DocumentumException {
        fileManagerApi.createFolderByParentId(request.getParentFolderId(), request.getName()) ;
        return commonResponse();
	}

    @RequestMapping(value = "/api/renameUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String renameUrl(@RequestBody RenameObjectRequest request) throws DocumentumException {
        fileManagerApi.renameByPath(request.getItem(), request.getNewItemPath());
        return commonResponse();
    }

    @RequestMapping(value = "/api/moveUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String moveUrl(@RequestBody CopyMoveObjectsRequest request) throws DocumentumException {
        //todo: ui has bugs on selecting the target path
        for (String id : request.getItems()) {
            fileManagerApi.moveObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/api/copyUrl", method = RequestMethod.POST)
    public String copyUrl(@RequestBody CopyMoveObjectsRequest request) throws DocumentumException {
        for (String id : request.getItems()) {
            fileManagerApi.copyObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/api/deleteFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteFolderUrl(@RequestBody DeleteObjectsRequest request) throws DocumentumException {
        for (String id : request.getItems()) {
            //TODO should get this boolean from UI
            fileManagerApi.deleteObjectById(id, false);
        }
        return commonResponse();
    }

    @RequestMapping(value= "/api/document/content/{documentId}",
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

    @RequestMapping(value= "/api/document/open/{documentId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Data openDocumentById(@PathVariable(value="documentId")String documentId)
            throws DocumentumException {
        ByteArrayResource content = fileManagerApi.getContentById(documentId);
        return new Data(content.getData());
    }

    //todo//////////////////////////////////////////////////////////////////////////////
    //todo////////////// above methods are refactored - 1st round //////////////////////
    //todo//////////////     todo for below methods   - 1st round //////////////////////
    //todo//////////////////////////////////////////////////////////////////////////////


    @RequestMapping(value= "/api/FOLDERS/content/{folderId}/startIndex/pageSize")
    public Collection paginationService(@PathVariable(value="folderId")String folderId , @PathVariable(value="startIndex")String startIndex , @PathVariable(value="pageSize")String pageSize)
            throws DocumentumException {
        //TODO to be implemented
        Collection result = null;
        try {
            result = fileManagerApi.getPaginatedResult(folderId, 0, 0);
            return result;
        } catch (RepositoryNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/api/editUrl", method = RequestMethod.POST)
    public String editUrl() {
        return commonResponse();
    }

	@RequestMapping(value = "/api/permissionsUrl", method = RequestMethod.POST)
	public String permissionsUrl() {
		return commonResponse();
	}

	@RequestMapping(value = "/api/extractUrl", method = RequestMethod.POST)
	public String extractUrl() {
		return commonResponse();
	}

	@RequestMapping(value = "/api/uploadUrl", method = RequestMethod.POST)
	public String uploadUrl() {
		return commonResponse();
	}
}