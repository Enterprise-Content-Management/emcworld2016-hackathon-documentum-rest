package com.emc.documentum.filemanager.api;

import java.io.InputStream;
import java.util.HashMap;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.emc.documentum.restclient.model.ByteArrayResource;

public interface FileManagerApi {

    Collection getAllCabinets(int pageNumber, int pageSize) throws DocumentumException;

    Collection getChildren(String path, int pageNumber, int pageSize) throws DocumentumException;

    Item renameByPath(String oldPath, String newPath) throws DocumentumException;

    Item moveObject(String id, String newParentPath) throws DocumentumException;

    Item copyObject(String id, String newParentPath) throws DocumentumException;

    Item uploadContent(String targetFolderPath, InputStream inputStream, String filename, String mime)
            throws DocumentumException;

    Item updateContent(String objectId, String content) throws DocumentumException;

    ByteArrayResource getContentById(String documentId) throws DocumentumException;

    Item createFolderByParentId(String ParentId, String folderName) throws DocumentumException;

    void deleteObjectById(String objectId, boolean deleteChildrenOrNot) throws DocumentumException;

    //todo//////////////////////////////////////////////////////////////////////////////
    //todo//////////////        below methods are not used        //////////////////////
    //todo//////////////////////////////////////////////////////////////////////////////


    Item getCabinetByName(String cabinetName) throws DocumentumException;

	Item getObjectById(String cabinetId) throws DocumentumException;

    Item createFolder(String cabinetName, String folderName) throws DocumentumException;

    Collection getPaginatedResult(String folderId , int startIndex , int pageSize) throws DocumentumException;

	Collection getDocumentByName(String name) throws DocumentumException;

    Item cancelCheckout(String documentId) throws DocumentumException;

    Item checkoutDocument(String documentId) throws DocumentumException;

    Item checkinDocument(String documentId,byte[]content) throws DocumentumException;

    Item createFolder(String parentId, HashMap<String, Object> properties) throws DocumentumException;
}