package com.emc.documentum.filemanager.api;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.emc.documentum.exceptions.CabinetNotFoundException;
import com.emc.documentum.exceptions.DocumentCreationException;
import com.emc.documentum.exceptions.DocumentNotFoundException;
import com.emc.documentum.exceptions.DocumentumException;
import com.emc.documentum.exceptions.FolderCreationException;
import com.emc.documentum.exceptions.RepositoryNotAvailableException;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.DocumentCreation;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.emc.documentum.restclient.DctmRestClient;
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
    public Collection getChildren(String folderId, int pageNumber, int pageSize)
            throws DocumentumException {
        return convertCoreRSEntryList(restClientX.getChildren(folderId, pageNumber, pageSize));
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
    public void deleteObjectById(String objectId, boolean deleteChildrenOrNot) throws DocumentumException {
        restClientX.deleteObjectById(objectId, deleteChildrenOrNot);
    }

    @Override
    public Item renameByPath(String oldPath, String newPath) throws DocumentumException {
        JsonObject object = restClientX.getObjectByPath(oldPath);
        JsonObject updated = restClientX.update(
                object,
                Collections.<String, Object>singletonMap("object_name", newPath.substring(newPath.lastIndexOf("/") + 1)));
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
    public ByteArrayResource getContentById(String documentId)
            throws DocumentumException {
        return restClientX.getContentById(documentId);
    }



    //todo//////////////////////////////////////////////////////////////////////////////
    //todo////////////// above methods are refactored - 1st round //////////////////////
    //todo//////////////     todo for below methods   - 1st round //////////////////////
    //todo//////////////////////////////////////////////////////////////////////////////



    @Autowired
    DctmRestClient restClient;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.emc.documentum.delegates.FileManagerApi#createFolder(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public Item createFolder(String cabinetName, String folderName) throws DocumentumException {
		JsonObject cabinet;
		JsonObject folder;
		try {
			cabinet = restClient.getCabinet(cabinetName);
			folder = restClient.createFolder(cabinet, folderName);
			return convertJsonObject(folder);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (CabinetNotFoundException | FolderCreationException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.FileManagerApi#createDocument(com.emc.
	 * documentum.dtos.DocumentCreation)
	 */
	@Override
	public Item createDocument(DocumentCreation docCreation) throws DocumentumException {
		JsonObject document;
		JsonObject folder;
		try {
			folder = restClient.getObjectById(docCreation.getParentId());
			document = restClient.createDocument(folder, docCreation.getProperties());
			return convertJsonObject(document);
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch ( DocumentCreationException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.FileManagerApi#getCabinetByName(java.
	 * lang.String)
	 */
	@Override
	public Item getCabinetByName(String cabinetName) throws DocumentumException {

		try {
			return convertJsonObject(restClient.getCabinet(cabinetName));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (CabinetNotFoundException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emc.documentum.delegates.FileManagerApi#getObjectById(java.lang.
	 * String)
	 */
	@Override
	public Item getObjectById(String cabinetId)
			throws DocumentumException {
		try {
			return convertJsonObject(restClient.getObjectById(cabinetId));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (Exception e) {
			// TODO Object Not Found Exception
			throw new CabinetNotFoundException(cabinetId);
		}
	}

	@Override
	public Collection getDocumentByName(String name) throws DocumentumException {
		try {
			return convertCoreRSEntryList(restClient.getDocumentByName(name));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		} catch (DocumentNotFoundException e) {
			return new Collection();
		}

	}

	@Override
	public Item checkoutDocument(String documentId)
			throws DocumentumException {
		try {
			return convertJsonObject(restClient.checkOutDocument(documentId));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
    }

	@Override
	public Item checkinDocument(String documentId, byte[] content)
			throws DocumentumException {
		try {
			return convertJsonObject(restClient.checkinDocument(documentId, content));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
	}

	@Override
	public Collection getPaginatedResult(String folderId, int startIndex, int pageSize)
			throws RepositoryNotAvailableException {
		try {
			return convertCoreRSEntryList(
                    restClient.getPaginatedResult(folderId, startIndex, pageSize));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
	}
	
	@Override
	public Item cancelCheckout(String documentId) throws DocumentumException {
		try {
			return convertJsonObject(restClient.cancelCheckout(documentId));
		} catch (ResourceAccessException e) {
			throw new RepositoryNotAvailableException("CoreRest");
		}
	}

	@Override
	public Item createFolder(String parentId, HashMap<String, Object> properties)
			throws FolderCreationException, RepositoryNotAvailableException, DocumentumException {
		throw new UnsupportedOperationException();
	}

}
