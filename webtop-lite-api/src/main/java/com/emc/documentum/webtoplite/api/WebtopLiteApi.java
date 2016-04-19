/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.webtoplite.api;

import java.io.InputStream;

import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.webtoplite.dtos.out.Collection;
import com.emc.documentum.webtoplite.dtos.out.Item;
import com.emc.documentum.restclient.model.ByteArrayResource;

/**
 * File manager APIs
 */
public interface WebtopLiteApi {

    /**
     * Get a page of cabinets (root folders)
     *
     * @param pageNumber page number for current collection
     * @param pageSize   page size for current collection
     * @return a page of cabinets
     * @throws DocumentumException
     */
    Collection getAllCabinets(int pageNumber, int pageSize) throws DocumentumException;

    /**
     * Get a page of child objects under a specified folder path
     *
     * @param path       the parent folder path
     * @param pageNumber page number for current collection
     * @param pageSize   page size for current collection
     * @return a page of folder child objects
     * @throws DocumentumException
     */
    Collection getChildren(String path, int pageNumber, int pageSize) throws DocumentumException;

    /**
     * Create a folder under the parent folder
     *
     * @param ParentId   the parent folder id
     * @param folderName the new folder name
     * @return the created folder
     * @throws DocumentumException
     */
    Item createFolderByParentId(String ParentId, String folderName) throws DocumentumException;

    /**
     * Rename a folder or object by path
     *
     * @param oldPath the older object path
     * @param newPath the new object path
     * @return the updated object
     * @throws DocumentumException
     */
    Item renameByPath(String oldPath, String newPath) throws DocumentumException;

    /**
     * Move an object to another folder
     *
     * @param id            the id of the object to move
     * @param newParentPath the target parent folder path
     * @return the updated object
     * @throws DocumentumException
     */
    Item moveObject(String id, String newParentPath) throws DocumentumException;

    /**
     * Copy an object to another folder
     *
     * @param id            the id of the object to copy
     * @param newParentPath the target parent folder path
     * @return the updated object
     * @throws DocumentumException
     */
    Item copyObject(String id, String newParentPath) throws DocumentumException;

    /**
     * Upload the content and create the document under a folder
     *
     * @param targetFolderPath the parent folder path
     * @param inputStream      the input stream of the content
     * @param filename         the file name of the new document
     * @param mime             the mime type of the content
     * @return the created document
     * @throws DocumentumException
     */
    Item uploadContent(String targetFolderPath, InputStream inputStream, String filename, String mime)
            throws DocumentumException;

    /**
     * Update the primary content for a document (support textural content only)
     *
     * @param objectId the id of the document to update
     * @param content  the new content in text
     * @return the updated document
     * @throws DocumentumException
     */
    Item updateContent(String objectId, String content) throws DocumentumException;

    /**
     * Get the content of a document
     *
     * @param documentId the id of the document
     * @return the content in byte array
     * @throws DocumentumException
     */
    ByteArrayResource getContentById(String documentId) throws DocumentumException;

    /**
     * Delete an object
     *
     * @param objectId            the id of the object to delete
     * @param deleteChildrenOrNot true indicates to delete all children; false for otherwise
     * @throws DocumentumException
     */
    void deleteObjectById(String objectId, boolean deleteChildrenOrNot) throws DocumentumException;

    /**
     * Search a page of documents with the full-text terms
     *
     * @param terms        the search terms in Simple Search Language
     * @param path         the location to search documents from; for empty location or "/", documents are searched
     *                     in all folders of the repository
     * @param page         page number for current collection
     * @param itemsPerPage page size for current collection
     * @return a page of search results
     * @throws DocumentumException
     */
    Collection search(String terms, String path, int page, int itemsPerPage) throws DocumentumException;
}