package com.emc.documentum.services.rest;

import java.util.ArrayList;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emc.documentum.delegate.provider.DelegateProvider;
import com.emc.documentum.delegates.DocumentumDelegate;
import com.emc.documentum.dtos.DocumentumFolder;
import com.emc.documentum.dtos.DocumentumObject;
import com.emc.documentum.dtos.DocumentumProperty;
import com.emc.documentum.exceptions.CanNotDeleteFolderException;
import com.emc.documentum.exceptions.DelegateNotFoundException;
import com.emc.documentum.exceptions.DocumentNotFoundException;
import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.exceptions.RepositoryNotAvailableException;
import com.emc.documentum.model.JsonObject;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

@CrossOrigin(origins = "*")
@RestController
public class FileManagerController extends BaseController{

	DocumentumDelegate dcDelegate;
	
	@Autowired
	DelegateProvider delegateProvider ;
	
	@RequestMapping(value = "/api/listUrl", method = RequestMethod.POST, headers = "Content-Type=application/json;charset=UTF-8")
	public String listURL(@RequestBody String jsonString , @RequestHeader(value="API_BASE", defaultValue="Rest") String delegateKey) {
		try {
			dcDelegate = delegateProvider.getDelegate(delegateKey) ;
		} catch (DelegateNotFoundException e1) {
			e1.printStackTrace();
			return errorResponse(delegateKey + " Repository is not available ") ;
		}
		
		JSONObject jsonRequest = null;
		JSONObject jsonRequestParams = null;
		JSONObject resultJson = null ;
		
		try {
			jsonRequest = (JSONObject) JSONValue.parseWithException(jsonString);
			jsonRequestParams = (JSONObject) jsonRequest.get("params");
			String folderId = (String) jsonRequestParams.get("folderId");
			System.out.println(jsonRequestParams);
			System.out.println("folder id " + folderId);
			
			if (folderId == null || folderId.equals("")) {
				System.out.println("-----getting cabinets : " + folderId);				
				ArrayList<DocumentumFolder> cabinets = dcDelegate.getAllCabinets() ;
				resultJson = transformFoldersToJson(cabinets) ;
			}
			else
			{
				System.out.println("-----getting children for folder ID : " + folderId);
				ArrayList<DocumentumObject> folders = dcDelegate.getChildren(folderId);
				resultJson = transformChildrenFromDocumentumObjects(folders) ;
				//JsonFeed feed = dcRestDelegate.getPaginatedResult(folderId, 0, 0);
				//resultJson = transformJsonFeedToJson(feed) ;
			}
			return resultJson.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}

	@RequestMapping(value = "/api/renameUrl", method = RequestMethod.POST)
	public String renameUrl() {
		return commonResponse();
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

	@RequestMapping(value = "/api/createFolderUrl", method = RequestMethod.POST)
	public String createFolderUrl(@RequestBody String jsonString , @RequestHeader(value="API_BASE", defaultValue="Rest") String delegateKey) {
		try {
			dcDelegate = delegateProvider.getDelegate(delegateKey) ;
		} catch (DelegateNotFoundException e1) {
			e1.printStackTrace();
			return errorResponse(delegateKey + " Repository is not available ") ;
		}
		
		JSONObject jsonRequest = null;
		JsonObject resultJson = null ;
		try {
			jsonRequest = (JSONObject) JSONValue.parseWithException(jsonString);
			String parentFolderId = (String) jsonRequest.get("parentFolderId");
			String folderName = (String) jsonRequest.get("name");
			System.out.println(jsonRequest);
			System.out.println("parent Folder  id " + parentFolderId);
			DocumentumFolder folder = dcDelegate.createFolderByParentId(parentFolderId, folderName) ;
			return commonResponse();
		} catch (DocumentumException | ParseException e ) {
			e.printStackTrace();
			return errorResponse("can't create folder");
		}	
	}
	
	
	
	@RequestMapping(value = "/api/deleteFolderUrl" , method = RequestMethod.POST)
	public String deleteFolderUrl(@RequestBody String jsonString , @RequestHeader(value="API_BASE", defaultValue="Rest") String delegateKey) {
		try {
			JSONObject jsonRequest = null;
			jsonRequest = (JSONObject) JSONValue.parseWithException(jsonString);
			JSONArray items = (JSONArray) jsonRequest.get("items");
			dcDelegate = delegateProvider.getDelegate(delegateKey) ;
			if(!items.isEmpty())
			{
				for(int i = 0 ; i < items.size() ; i++)
				{
					//TODO should get this boolean from UI
					dcDelegate.deleteObject((String) items.get(i) , false);
				}
			}
			return commonResponse();
			
		} catch (DelegateNotFoundException | ParseException e1) {
			e1.printStackTrace();
			return errorResponse(delegateKey + " Repository is not available ") ;
		} catch (CanNotDeleteFolderException e) {
			e.printStackTrace();
			return errorResponse(e.getMessage()) ;
		}
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
	

	@RequestMapping(value= "/api/document/content/{documentId}" , produces = "application/pdf")
	public byte[] getDocumentContentById(@PathVariable(value="documentId")String documentId , @RequestHeader(value="API_BASE" , defaultValue="Rest") String delegateKey) throws DocumentNotFoundException{
		try {
			dcDelegate = delegateProvider.getDelegate(delegateKey) ;
		} catch (DelegateNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			byte[] enCodedfileContent = (byte[]) dcDelegate.getDocumentContentById(documentId);
			return Base64.decodeBase64(enCodedfileContent);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	@RequestMapping(value= "/api/document/open/{documentId}" )
	public JSONObject openDocumentById(@PathVariable(value="documentId")String documentId , @RequestHeader(value="API_BASE", defaultValue="Rest") String delegateKey) throws DocumentNotFoundException{
		try {
			dcDelegate = delegateProvider.getDelegate(delegateKey) ;
		} catch (DelegateNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			byte[] enCodedfileContent = (byte[]) dcDelegate.getDocumentContentById(documentId);
			byte[] decoded = Base64.decodeBase64(enCodedfileContent);
			JSONObject json = new JSONObject();
			json.put("data", decoded);
			return json ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}

	
	@RequestMapping(value= "/api/folder/content/{folderId}/startIndex/pageSize")
	public String paginationService(@PathVariable(value="folderId")String folderId , @PathVariable(value="startIndex")String startIndex , @PathVariable(value="pageSize")String pageSize){		
		//TODO to be implemented
		JSONObject resultJson = null ;
		ArrayList<DocumentumFolder> folders;
		try {
			folders = dcDelegate.getPaginatedResult(folderId, 0, 0);
			resultJson = transformFoldersToJson(folders) ;
			return resultJson.toJSONString() ;
		} catch (RepositoryNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
	
	
	
	private JSONObject transformFoldersToJson(ArrayList<DocumentumFolder> folders) {
		JSONArray children = new JSONArray() ;
		for (int i = 0 ; i < folders.size() ; i++) 
		{	
			JSONObject json = new JSONObject() ;
			json.put("id", folders.get(i).getId()) ;
			json.put("name", folders.get(i).getName()) ;
			json.put("rights", "drwxr-xr-x") ;
			json.put("size", "4096") ;
			//StringBuffer dateString = new StringBuffer((String) folders.get(i).getProperties().get("r_creation_date")) ;
			//dateString.replace(10, 11, " ").delete(18,28) ;
			//json.put("date", dateString.toString()) ;
			json.put("date", "2016-03-05 04:33:27") ;
			json.put("type", "dir") ;
			children.add(json) ;
		}		
		JSONObject returnJson = new JSONObject() ;
		returnJson.put("result", children) ;
		return returnJson ;
	}
	
	private JSONObject transformChildrenFromDocumentumObjects(ArrayList<DocumentumObject> objects)
	{
		JSONArray children = new JSONArray() ;
		for (int i = 0 ; i < objects.size() ; i++) {
				String type = "";
				if(objects.get(i).getType().endsWith("Document") || objects.get(i).getType().endsWith("document")){
					type = "file";
				}else if(objects.get(i).getType().endsWith("Folder") || objects.get(i).getType().endsWith("folder")){
					type = "dir";
				}
				else if (objects.get(i).getType().endsWith("Object") || objects.get(i).getType().endsWith("object")){
					continue ;
				}
				else
				{
					type = "file";
				}
				
				JSONObject json = new JSONObject() ;					
				json.put("id", objects.get(i).getId()) ;
				json.put("name", objects.get(i).getName()) ;
				json.put("rights", "drwxr-xr-x") ;
				ArrayList<DocumentumProperty> properties = objects.get(i).getProperties();
				for(DocumentumProperty property : properties){
					if(property.getLocalName().equals("r_content_size")){
						json.put("size", property.getValue()) ;
						break;
					}
				}
														
				//parsing date
				//StringBuffer dateString = new StringBuffer((String) folders.get(i).getProperties().get("r_creation_date")) ;
				//dateString.replace(10, 11, " ").delete(18,28) ;
				//json.put("date", dateString.toString()) ;
				json.put("date", "2016-03-05 04:33:27") ;
				json.put("type", type) ;
				children.add(json) ;
		}
		JSONObject returnJson = new JSONObject() ;
		returnJson.put("result", children) ;
		return returnJson ;
	}
	
}