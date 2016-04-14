package com.emc.documentum.filemanager.api;

import java.util.HashMap;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.DocumentCreation;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.emc.documentum.restclient.model.ByteArrayResource;

public interface FileManagerApi {

    Item createFolder(String cabinetName, String folderName) throws DocumentumException;

    Item renameByPath(String oldPath, String newPath) throws DocumentumException;

    Item moveObject(String id, String newParentPath) throws DocumentumException;

    Item createDocument(DocumentCreation docCreation) throws DocumentumException;

    Item getCabinetByName(String cabinetName) throws DocumentumException;

	Item getObjectById(String cabinetId) throws DocumentumException;

	Collection getAllCabinets(int pageNumber, int pageSize) throws DocumentumException;

	Collection getChildren(String folderId, int pageNumber, int pageSize) throws DocumentumException;

    Collection getPaginatedResult(String folderId , int startIndex , int pageSize) throws DocumentumException;

    ByteArrayResource getContentById(String documentId) throws DocumentumException;

	Collection getDocumentByName(String name) throws DocumentumException;

	Item createFolderByParentId(String ParentId, String folderName) throws DocumentumException;

    Item cancelCheckout(String documentId) throws DocumentumException;

    Item checkoutDocument(String documentId) throws DocumentumException;

    Item checkinDocument(String documentId,byte[]content) throws DocumentumException;

    void deleteObjectById(String objectId, boolean deleteChildrenOrNot) throws DocumentumException ;

    Item createFolder(String parentId, HashMap<String, Object> properties) throws DocumentumException;;
}