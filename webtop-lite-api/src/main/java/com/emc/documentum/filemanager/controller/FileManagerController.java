package com.emc.documentum.filemanager.controller;

import java.util.ArrayList;

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
import com.emc.documentum.filemanager.dtos.DocumentumFolder;
import com.emc.documentum.filemanager.dtos.DocumentumObject;
import com.emc.documentum.filemanager.dtos.DocumentumProperty;
import com.emc.documentum.filemanager.dtos.in.DeleteObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.ListObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.MoveObjectsRequest;
import com.emc.documentum.filemanager.dtos.in.NewFolderRequest;
import com.emc.documentum.filemanager.dtos.in.RenameObjectRequest;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.Data;
import com.emc.documentum.filemanager.dtos.out.Item;
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
	public Collection listURL(@RequestBody ListObjectsRequest request)
            throws DocumentumException {
        //todo: pagination params
        Collection result = null;
        String folderId = request.getParam("folderId");
        if (Strings.isNullOrEmpty(folderId)) {
            LOGGER.debug("Getting cabinets");
            result = fileManagerApi.getAllCabinets(1, 20) ;
        }
        else {
            LOGGER.debug("getting children for FOLDER ID : " + folderId);
            result = fileManagerApi.getChildren(folderId, 1, 20);
        }
        return result;
	}

	@RequestMapping(value = "/api/createFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createFolderUrl(@RequestBody NewFolderRequest request)
            throws DocumentumException {
        fileManagerApi.createFolderByParentId(request.getParentFolderId(), request.getName()) ;
        return commonResponse();
	}

    @RequestMapping(value = "/api/renameUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String renameUrl(@RequestBody RenameObjectRequest request)
            throws DocumentumException {
        fileManagerApi.renameByPath(request.getItem(), request.getNewItemPath());
        return commonResponse();
    }

    @RequestMapping(value = "/api/moveUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String moveUrl(@RequestBody MoveObjectsRequest request)
            throws DocumentumException {
        //todo: ui has bugs on selecting the target path
        for (String id : request.getItems()) {
            fileManagerApi.moveObject(id, request.getNewPath());
        }
        return commonResponse();
    }

    @RequestMapping(value = "/api/deleteFolderUrl",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteFolderUrl(@RequestBody DeleteObjectsRequest request)
            throws DocumentumException {
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

    @RequestMapping(value = "/api/copyUrl", method = RequestMethod.POST)
    public String copyUrl() {
        return commonResponse();
    }

    @RequestMapping(value = "/api/removeUrl", method = RequestMethod.POST)
    public String removeUrl() {
        return commonResponse();
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
	
	private Collection transformFoldersToJson(ArrayList<DocumentumFolder> folders) {
		Collection collection = new Collection();
		for (int i = 0 ; i < folders.size() ; i++) 
		{
            Item item = new Item();
            item.setId(folders.get(i).getId()) ;
            item.setName(folders.get(i).getName()) ;
            item.setRights("drwxr-xr-x") ;
            item.setSize(4096) ;
			//StringBuffer dateString = new StringBuffer((String) folders.get(i).getProperties().get("r_creation_date")) ;
			//dateString.replace(10, 11, " ").delete(18,28) ;
			//json.put("date", dateString.toString()) ;
            item.setDate("2016-03-05 04:33:27"); ;
            item.setType("dir") ;
			collection.getResult().add(item);
		}		
		return collection;
	}
	
	private Collection transformChildrenFromDocumentumObjects(ArrayList<DocumentumObject> objects)
	{
        Collection collection = new Collection();
		for (int i = 0 ; i < objects.size() ; i++) {
            String type = "";
            if(objects.get(i).getType().endsWith("Document") || objects.get(i).getType().endsWith("document")){
                type = "file";
            }else if(objects.get(i).getType().endsWith("Folder") || objects.get(i).getType().endsWith("FOLDERS")){
                type = "dir";
            }
            else if (objects.get(i).getType().endsWith("Object") || objects.get(i).getType().endsWith("object")){
                continue ;
            }
            else
            {
                type = "file";
            }

            Item item = new Item();
            item.setId(objects.get(i).getId()) ;
            item.setName(objects.get(i).getName()) ;
            item.setRights("drwxr-xr-x") ;
            ArrayList<DocumentumProperty> properties = objects.get(i).getProperties();
            for(DocumentumProperty property : properties){
                if(property.getLocalName().equals("r_content_size")){
                    item.setSize((int) property.getValue()) ;
                    break;
                }
            }
														
            //parsing date
            //StringBuffer dateString = new StringBuffer((String) folders.get(i).getProperties().get("r_creation_date")) ;
            //dateString.replace(10, 11, " ").delete(18,28) ;
            //json.put("date", dateString.toString()) ;
            item.setDate("2016-03-05 04:33:27"); ;
            item.setType(type); ;
            collection.getResult().add(item);
		}
		return collection;
	}
	
}