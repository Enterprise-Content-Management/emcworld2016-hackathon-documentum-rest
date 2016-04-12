package com.emc.documentum.services.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emc.documentum.delegate.provider.APIDelegateProvider;
import com.emc.documentum.delegates.DocumentumDelegate;
import com.emc.documentum.dtos.DocumentCreation;
import com.emc.documentum.dtos.DocumentumDocument;
import com.emc.documentum.dtos.DocumentumFolder;
import com.emc.documentum.dtos.DocumentumObject;
import com.emc.documentum.exceptions.CabinetNotFoundException;
import com.emc.documentum.exceptions.CanNotDeleteFolderException;
import com.emc.documentum.exceptions.DelegateNotFoundException;
import com.emc.documentum.exceptions.DocumentCheckoutException;
import com.emc.documentum.exceptions.DocumentNotFoundException;
import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.exceptions.RepositoryNotAvailableException;
import com.emc.documentum.translation.TranslationUtility;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("{api}/services")
@CrossOrigin("*")
public class DocumentumIntegrationController {

	Logger log = Logger.getLogger(DocumentumIntegrationController.class.getCanonicalName());

	@Autowired
	APIDelegateProvider delegateProvider;

	@Autowired
	TranslationUtility translationUtility;

	@ApiOperation(value = "Create Folder", notes = "Create a folder named {folderName} under the folder/cabinet identified using {parentId}")
	@RequestMapping(value = "/folder/create/{parentId}/{folderName}", method = RequestMethod.POST)
	public DocumentumFolder createFolderUsingParentId(@PathVariable(value = "api") String api,
			@PathVariable(value = "parentId") String parentId,
			@PathVariable(value = "folderName") String folderName)
			throws DocumentumException, DelegateNotFoundException {
		try {
			return delegateProvider.getDelegate(api).createFolderByParentId(parentId, folderName);
		} catch (DocumentumException e) {
			// TODO Customize Error Handling
			throw e;
		}
	}
	
	
	@ApiOperation(value = "Create Folder under a parent", notes = "Create a folder under a parent identifed by {parentId}",hidden=true)
	@RequestMapping(value = "/folder/create/{parentId}", method = RequestMethod.POST)
	public DocumentumFolder createFolder(@PathVariable(value = "api") String api,
			@PathVariable(value = "parentId") String parentId,
			@RequestBody HashMap<String, Object> properties)
			throws DocumentumException, DelegateNotFoundException {
		try {
			return delegateProvider.getDelegate(api).createFolder(parentId, properties);
		} catch (DocumentumException e) {
			// TODO Customize Error Handling
			throw e;
		}
	}

	@ApiOperation(value = "Create Document", notes = "Create a Contentless document")
	@RequestMapping(value = "/document/create", method = RequestMethod.POST)
	public DocumentumDocument createDocument(@PathVariable(value = "api") String api,
			@RequestBody DocumentCreation docCreation) throws DocumentumException, DelegateNotFoundException {
		try {
			return (delegateProvider.getDelegate(api)).createDocument(docCreation);
		} catch (DocumentumException e) {
			// TODO Customize Error Handling
			throw e;
		}
	}

	@ApiOperation(value = "Get Cabinet By Name", notes = "Get a Cabinet by its name")
	@RequestMapping(value = "get/cabinet/name/{cabinetName}", method = RequestMethod.GET)
	public DocumentumFolder getCabinetByName(@PathVariable(value = "api") String api,
			@PathVariable(value = "cabinetName") String cabinetName)
			throws DelegateNotFoundException, DocumentumException {

		try {
			DocumentumDelegate delegate = delegateProvider.getDelegate(api);
			DocumentumFolder cabinet = delegate.getCabinetByName(cabinetName);
			translationUtility.translateFromRepo(cabinet, api);
			return cabinet;
		} catch (CabinetNotFoundException e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	@ApiOperation(value = "Get Object By Id", notes = "Get an object by its object_id")
	@RequestMapping(value = "get/object/id/{objectId}", method = { RequestMethod.GET })
	public DocumentumObject getCabinetById(@PathVariable(value = "api") String api,
			@PathVariable(value = "objectId") String objectId)
			throws CabinetNotFoundException, RepositoryNotAvailableException, DelegateNotFoundException {
		return (delegateProvider.getDelegate(api)).getObjectById(objectId);

	}

	@ApiOperation(value = "Delete Object", notes = "Deletes an object using identified by its {objectId}, if the object is a folder with children the deleteChildren query paramater must be set to true")
	@RequestMapping(value = "delete/object/id/{objectId}", method = { RequestMethod.DELETE })
	public void deleteObject(@PathVariable(value = "api") String api, @PathVariable(value = "objectId") String objectId,
			@RequestParam(name = "deleteChildren", defaultValue = "false") boolean deleteChildren)
			throws CabinetNotFoundException, RepositoryNotAvailableException, DelegateNotFoundException,
			CanNotDeleteFolderException {
		try {
			(delegateProvider.getDelegate(api)).deleteObject(objectId, deleteChildren);
		} catch (CanNotDeleteFolderException e) {
			e.printStackTrace();
			throw e;
		}
		return;

	}

	@ApiOperation(value = "Get Cabinets", notes = "Get all Cabinets")
	@RequestMapping(value = "get/cabinets", method = RequestMethod.GET)
	public ArrayList<DocumentumFolder> getAllCabinets(@PathVariable(value = "api") String api,
			@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize)
			throws RepositoryNotAvailableException, DelegateNotFoundException {
		return (delegateProvider.getDelegate(api)).getAllCabinets(pageNumber, pageSize);
	}

	@ApiOperation(value = "Get Children of a Folder", notes = "Get children of a folder/cabinet identified by its {folderId}")
	@RequestMapping(value = "get/{folderId}/children", method = RequestMethod.GET)
	public ArrayList<DocumentumObject> getChildren(@PathVariable(value = "api") String api,
			@PathVariable(value = "folderId") String folderId,
			@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize) throws Exception {
		System.out.println("Page Number: " + pageNumber + " Page Size: " + pageSize);
		return (delegateProvider.getDelegate(api)).getChildren(folderId, pageNumber, pageSize);
	}

	@ApiOperation(value = "Get Document Content", notes = "Gets the document content as a Base64 encoded string")
	@RequestMapping(value = "get/document/content/id/{documentId}", method = RequestMethod.GET)
	public Object getDocumentContentById(@PathVariable(value = "api") String api,
			@PathVariable(value = "documentId") String documentId)
			throws DocumentNotFoundException, RepositoryNotAvailableException, DelegateNotFoundException {
		return (delegateProvider.getDelegate(api)).getDocumentContentById(documentId);
	}

	@ApiOperation(value = "Search document by name", notes = "Search document by its name")
	@RequestMapping(value = "document/search/{name}", method = RequestMethod.GET)
	public ArrayList<DocumentumObject> searchDocumentByName(@PathVariable(value = "api") String api,
			@PathVariable(value = "name") String name)
			throws RepositoryNotAvailableException, DelegateNotFoundException {
		log.entering("searchDocumentByName", name);
		return (delegateProvider.getDelegate(api)).getDocumentByName(name);
	}

	@ApiOperation(value = "Checkout Document", notes = "Checkout a specific document")
	@RequestMapping(value = "get/document/checkout/id/{documentId}", method = RequestMethod.POST)
	public DocumentumDocument checkoutDocument(@PathVariable(value = "api") String api,
			@PathVariable(value = "documentId") String documentId)
			throws DelegateNotFoundException, DocumentumException {
		log.entering("checkout document ", documentId);
		return (delegateProvider.getDelegate(api)).checkoutDocument(documentId);
	}

	@ApiOperation(value = "Checkin Document", notes = "Check in a document using the provided Base64 encoded stream")
	@RequestMapping(value = "get/document/checkin/id/{documentId}", method = RequestMethod.POST)
	public DocumentumDocument checkinDocument(@PathVariable(value = "api") String api,
			@PathVariable(value = "documentId") String documentId, @RequestBody byte[] content)
			throws DelegateNotFoundException, DocumentumException {
		log.entering("checkin document ", documentId);
		return (delegateProvider.getDelegate(api)).checkinDocument(documentId, content);
	}

	@ApiOperation(value = "Cancel Document Checkout", notes = "Cancels the Checkout of this specific document")
	@RequestMapping(value = "get/document/cancelCheckout/id/{documentId}", method = RequestMethod.POST)
	public DocumentumObject cancelCheckout(@PathVariable(value = "api") String api,
			@PathVariable(value = "documentId") String documentId)
			throws RepositoryNotAvailableException, DocumentCheckoutException, DelegateNotFoundException {
		log.entering("checkin document ", documentId);
		return (delegateProvider.getDelegate(api)).cancelCheckout(documentId);
	}
}
