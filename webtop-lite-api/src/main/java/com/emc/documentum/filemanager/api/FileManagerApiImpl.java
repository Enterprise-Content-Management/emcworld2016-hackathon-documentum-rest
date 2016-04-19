/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.filemanager.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.emc.documentum.restclient.DctmRestClientX;
import com.emc.documentum.restclient.model.ByteArrayResource;
import com.emc.documentum.restclient.model.JsonObject;

import static com.emc.documentum.filemanager.transformation.CoreRestTransformation.convertCoreRSEntryList;
import static com.emc.documentum.filemanager.transformation.CoreRestTransformation.convertJsonObject;

@Component("FileManagerApi")
public class FileManagerApiImpl implements FileManagerApi {

    @Autowired
    DctmRestClientX restClientX;

    @Override
    public void deleteObjectById(String objectId, boolean deleteChildrenOrNot) throws DocumentumException {
        restClientX.deleteObjectById(objectId, deleteChildrenOrNot);
    }

    @Override
    public Collection getChildren(String path, int pageNumber, int pageSize)
            throws DocumentumException {
        return convertCoreRSEntryList(restClientX.getChildren(path, pageNumber, pageSize));
    }

    @Override
    public Collection getAllCabinets(int pageNumber, int pageSize)
            throws DocumentumException {
        return convertCoreRSEntryList(restClientX.getAllCabinets(pageNumber, pageSize));
    }

    @Override
    public Item createFolderByParentId(String parentId, String folderName)
            throws DocumentumException {
        JsonObject folder = restClientX.createFolder(parentId, folderName);
        return convertJsonObject(folder);
    }

    @Override
    public Item renameByPath(String oldPath, String newPath) throws DocumentumException {
        JsonObject object = restClientX.getObjectByPath(oldPath);
        JsonObject updated = restClientX.update(
                object,
                Collections
                        .<String, Object>singletonMap("object_name", newPath.substring(newPath.lastIndexOf("/") + 1)));
        return convertJsonObject(updated);
    }

    @Override
    public Item moveObject(String id, String newParentPath) throws DocumentumException {
        JsonObject object = restClientX.getObjectById(id);
        JsonObject targetFolder = restClientX.getObjectByPath(newParentPath);
        JsonObject updated = restClientX.move(
                object,
                targetFolder);
        return convertJsonObject(updated);
    }

    @Override
    public Item copyObject(String id, String newParentPath) throws DocumentumException {
        JsonObject object = restClientX.getObjectById(id);
        JsonObject targetFolder = restClientX.getObjectByPath(newParentPath);
        JsonObject updated = restClientX.copy(
                object,
                targetFolder);
        return convertJsonObject(updated);
    }

    @Override
    public Item updateContent(String id, String content) throws DocumentumException {
        JsonObject object = restClientX.getObjectById(id);
        JsonObject updated = restClientX.updateContent(
                object,
                Base64.decodeBase64(content));
        return convertJsonObject(updated);
    }

    @Override
    public ByteArrayResource getContentById(String documentId)
            throws DocumentumException {
        return restClientX.getContentById(documentId);
    }

    @Override
    public Item uploadContent(String targetFolderPath, InputStream inputStream, String filename, String mime)
            throws DocumentumException {
        try {
            JsonObject folder = restClientX.getObjectByPath(targetFolderPath);
            byte[] data = IOUtils.toByteArray(inputStream);
            return convertJsonObject(restClientX.createContentfulDocument(folder, data, filename, mime));
        } catch (IOException e) {
            throw new DocumentumException("Fail to read input stream.", e);
        }
    }

    @Override
    public Collection search(String terms, String path, int page, int itemsPerPage)
            throws DocumentumException {
        return convertCoreRSEntryList(restClientX.simpleSearch(terms, path, page, itemsPerPage));
    }
}
