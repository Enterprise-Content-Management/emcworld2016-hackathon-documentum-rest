package com.emc.documentum.delegates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.emc.documentum.dtos.DocumentCreation;
import com.emc.documentum.dtos.DocumentumCabinet;
import com.emc.documentum.dtos.DocumentumDocument;
import com.emc.documentum.dtos.DocumentumFolder;
import com.emc.documentum.dtos.DocumentumObject;
import com.emc.documentum.exceptions.CabinetNotFoundException;
import com.emc.documentum.exceptions.CanNotDeleteFolderException;
import com.emc.documentum.exceptions.DocumentCheckoutException;
import com.emc.documentum.exceptions.DocumentCreationException;
import com.emc.documentum.exceptions.DocumentNotFoundException;
import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.exceptions.FolderCreationException;
import com.emc.documentum.exceptions.RepositoryNotAvailableException;
import com.emc.documentum.model.JsonObject;
import com.emc.documentum.transformation.CoreRestTransformation;
import com.emc.documentum.wrappers.DCRestAPIWrapper;

@Component("DocumentumRestDelegate")
public class DocumentumRestDelegate implements DocumentumDelegate {

	Logger log = Logger.getLogger(DocumentumRestDelegate.class.getCanonicalName());
	@Autowired
	DCRestAPIWrapper dcAPI;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#createFolder(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public DocumentumFolder createFolder(String cabinetName, String folderName) throws DocumentumException {
		log.entering(DocumentumRestDelegate.class.getSimpleName(), "CreateFolder");
		JsonObject cabinet;
		JsonObject folder;
		try {
			cabinet = dcAPI.getCabinet(cabinetName);
			folder = dcAPI.createFolder(cabinet, folderName);
			return CoreRestTransformation.convertJsonObject(folder, DocumentumFolder.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (CabinetNotFoundException | FolderCreationException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new DocumentumException("Unable to instantiate class of type " + DocumentumDocument.class.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#createFolder(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public DocumentumFolder createFolderByParentId(String parentId, String folderName) throws DocumentumException {
		log.entering(DocumentumRestDelegate.class.getSimpleName(), "CreateFolder");
		JsonObject parent = dcAPI.getObjectById(parentId);
		JsonObject folder;
		try {
			folder = dcAPI.createFolder(parent, folderName);
			return CoreRestTransformation.convertJsonObject(folder, DocumentumFolder.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (FolderCreationException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new DocumentumException("Unable to instantiate class of type " + DocumentumDocument.class.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#createDocument(com.emc.
	 * documentum.dtos.DocumentCreation)
	 */
	@Override
	public DocumentumDocument createDocument(DocumentCreation docCreation) throws DocumentumException {
		log.entering(DocumentumRestDelegate.class.getSimpleName(), "createDocument");
		JsonObject document;
		JsonObject folder;
		try {
			folder = dcAPI.getObjectById(docCreation.getParentId());
			document = dcAPI.createDocument(folder, docCreation.getProperties());
			return CoreRestTransformation.convertJsonObject(document, DocumentumDocument.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch ( DocumentCreationException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new DocumentumException("Unable to instantiate class of type " + DocumentumDocument.class.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#getCabinetByName(java.
	 * lang.String)
	 */
	@Override
	public DocumentumFolder getCabinetByName(String cabinetName) throws DocumentumException {

		try {
			return CoreRestTransformation.convertJsonObject(dcAPI.getCabinet(cabinetName), DocumentumCabinet.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (CabinetNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new DocumentumException("Unable to instantiate class of type " + DocumentumCabinet.class.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#getObjectById(java.lang.
	 * String)
	 */
	@Override
	public DocumentumObject getObjectById(String cabinetId)
			throws CabinetNotFoundException, RepositoryNotAvailableException {
		try {
			return CoreRestTransformation.convertJsonObject(dcAPI.getObjectById(cabinetId));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			// TODO Object Not Found Exception
			throw new CabinetNotFoundException(cabinetId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.emc.documentum.delegates.DocumentumDelegate#getAllCabinets()
	 */
	@Override
	public ArrayList<DocumentumFolder> getAllCabinets() throws RepositoryNotAvailableException {
		try {
			//TODO cabinets pagination should be set on front end ...
			return CoreRestTransformation.convertCoreRSEntryList(dcAPI.getAllCabinets(1, 20), DocumentumFolder.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#getChildren(java.lang.
	 * String)
	 */
	@Override
	public ArrayList<DocumentumObject> getChildren(String folderId) throws RepositoryNotAvailableException {
		try {
			return CoreRestTransformation.convertCoreRSEntryList(dcAPI.getChildren(folderId, 1, 20));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (Exception e) {
			// TODO Object Not Found Exception
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.DocumentumDelegate#getDocumentContentById(
	 * java.lang.String)
	 */
	@Override
	public byte[] getDocumentContentById(String documentId)
			throws DocumentNotFoundException, RepositoryNotAvailableException {
		try {
			return dcAPI.getDocumentContentById(documentId);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
	}

	@Override
	public ArrayList<DocumentumObject> getDocumentByName(String name) throws RepositoryNotAvailableException {
		try {
			return CoreRestTransformation.convertCoreRSEntryList(dcAPI.getDocumentByName(name));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (DocumentNotFoundException e) {
			return new ArrayList<DocumentumObject>();
		}

	}

	@Override
	public DocumentumDocument checkoutDocument(String documentId)
			throws DocumentumException {
		try {
			return CoreRestTransformation.convertJsonObject(dcAPI.checkOutDocument(documentId),DocumentumDocument.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new DocumentumException("Unable to instantiate class of type " + DocumentumDocument.class.getName());
		}
	}

	@Override
	public DocumentumDocument checkinDocument(String documentId, byte[] content)
			throws DocumentumException {
		try {
			return CoreRestTransformation.convertJsonObject(dcAPI.checkinDocument(documentId, content),
					DocumentumDocument.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new DocumentumException("Unable to instantiate class of type " + DocumentumDocument.class.getName());
		}

	}

	@Override
	public ArrayList<DocumentumFolder> getPaginatedResult(String folderId, int startIndex, int pageSize)
			throws RepositoryNotAvailableException {
		try {
			return dcAPI.getPaginatedResult(folderId, startIndex, pageSize);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
	}

	@Override
	public String getIdentifier() {
		return "corerest";
	}

	@Override
	public ArrayList<DocumentumObject> getChildren(String folderId, int pageNumber, int pageSize)
			throws RepositoryNotAvailableException {
		try {
			return CoreRestTransformation.convertCoreRSEntryList(dcAPI.getChildren(folderId, pageNumber, pageSize));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (Exception e) {
			// TODO Object Not Found Exception
			throw e;
		}
	}

	@Override
	public ArrayList<DocumentumFolder> getAllCabinets(int pageNumber, int pageSize)
			throws RepositoryNotAvailableException {
		try {
			return CoreRestTransformation.convertCoreRSEntryList(dcAPI.getAllCabinets(pageNumber, pageSize),
					DocumentumFolder.class);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void deleteObject(String objectId , boolean deleteChildrenOrNot) throws CanNotDeleteFolderException {
		dcAPI.deleteObject(objectId , deleteChildrenOrNot) ;
	}
	
	@Override
	public DocumentumObject cancelCheckout(String documentId) throws RepositoryNotAvailableException, DocumentCheckoutException {
		try {
			return CoreRestTransformation.convertJsonObject(dcAPI.cancelCheckout(documentId));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
	}

	@Override
	public DocumentumFolder createFolder(String parentId, HashMap<String, Object> properties)
			throws FolderCreationException, RepositoryNotAvailableException, DocumentumException {
		throw new UnsupportedOperationException();
	}

}
