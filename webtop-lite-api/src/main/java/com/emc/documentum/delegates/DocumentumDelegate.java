package com.emc.documentum.delegates;

import java.util.ArrayList;
import java.util.HashMap;

import com.emc.documentum.dtos.DocumentCreation;
import com.emc.documentum.dtos.DocumentumDocument;
import com.emc.documentum.dtos.DocumentumFolder;
import com.emc.documentum.dtos.DocumentumObject;
import com.emc.documentum.exceptions.CabinetNotFoundException;
import com.emc.documentum.exceptions.CanNotDeleteFolderException;
import com.emc.documentum.exceptions.DocumentCheckinException;
import com.emc.documentum.exceptions.DocumentCheckoutException;
import com.emc.documentum.exceptions.DocumentNotFoundException;
import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.exceptions.FolderCreationException;
import com.emc.documentum.exceptions.RepositoryNotAvailableException;

public interface DocumentumDelegate {

	String getIdentifier();
	
	DocumentumFolder createFolder(String cabinetName, String folderName)
			throws FolderCreationException, CabinetNotFoundException, RepositoryNotAvailableException, DocumentumException;

	DocumentumDocument createDocument(DocumentCreation docCreation) throws DocumentumException;

	DocumentumFolder getCabinetByName(String cabinetName) throws CabinetNotFoundException, RepositoryNotAvailableException, DocumentumException;

	DocumentumObject getObjectById(String cabinetId) throws CabinetNotFoundException, RepositoryNotAvailableException;

	@Deprecated
	/**
	 * Use the paginated version getAllCabinets(pageNumber,pageSize)
	 * @return
	 * @throws RepositoryNotAvailableException
	 */
	ArrayList<DocumentumFolder> getAllCabinets() throws RepositoryNotAvailableException;

	@Deprecated
	/**
	 * Use the paginated version getChildren(folderId,pageNumber,pageSize)
	 * @param folderId
	 * @return
	 * @throws RepositoryNotAvailableException
	 */
	ArrayList<DocumentumObject> getChildren(String folderId) throws RepositoryNotAvailableException;
	
	ArrayList<DocumentumObject> getChildren(String folderId, int pageNumber, int pageSize) throws RepositoryNotAvailableException;
	

	byte[] getDocumentContentById(String documentId) throws DocumentNotFoundException, RepositoryNotAvailableException;

	ArrayList<DocumentumObject> getDocumentByName(String name) throws RepositoryNotAvailableException;
	
	DocumentumDocument checkoutDocument(String documentId) throws RepositoryNotAvailableException, DocumentCheckoutException, DocumentumException;
	
	DocumentumDocument checkinDocument(String documentId,byte[]content) throws RepositoryNotAvailableException, DocumentCheckinException, DocumentumException;
	ArrayList<DocumentumFolder> getPaginatedResult(String folderId , int startIndex , int pageSize) throws RepositoryNotAvailableException;

	DocumentumFolder createFolderByParentId(String ParentId, String folderName)
			throws FolderCreationException, RepositoryNotAvailableException, DocumentumException;

	ArrayList<DocumentumFolder> getAllCabinets(int pageNumber, int pageSize) throws RepositoryNotAvailableException;

	void deleteObject(String objectId , boolean deleteChildrenOrNot) throws CanNotDeleteFolderException ;
	
	DocumentumObject cancelCheckout(String documentId) throws RepositoryNotAvailableException, DocumentCheckoutException;

	DocumentumFolder createFolder(String parentId, HashMap<String, Object> properties) throws FolderCreationException, CabinetNotFoundException, RepositoryNotAvailableException, DocumentumException;;
}