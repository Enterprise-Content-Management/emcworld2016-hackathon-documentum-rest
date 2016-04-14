package com.emc.documentum.filemanager.transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.emc.documentum.constants.Cardinality;
import com.emc.documentum.constants.DocumentumProperties;
import com.emc.documentum.filemanager.dtos.out.Collection;
import com.emc.documentum.filemanager.dtos.DocumentumCabinet;
import com.emc.documentum.filemanager.dtos.DocumentumDocument;
import com.emc.documentum.filemanager.dtos.DocumentumFolder;
import com.emc.documentum.filemanager.dtos.DocumentumObject;
import com.emc.documentum.filemanager.dtos.DocumentumProperty;
import com.emc.documentum.filemanager.dtos.out.Item;
import com.emc.documentum.restclient.model.JsonEntry;
import com.emc.documentum.restclient.model.JsonEntry.Content;
import com.emc.documentum.restclient.model.JsonLink;
import com.emc.documentum.restclient.model.JsonObject;
import com.emc.documentum.restclient.DctmRestClient;

public final class CoreRestTransformation {

	private CoreRestTransformation() {

	}

    public static Collection convertCoreRSEntryList(List<JsonEntry> jsonEntryFeed) {
        Collection collection = new Collection();
        if (jsonEntryFeed != null) {
            for (JsonEntry jsonEntry : jsonEntryFeed) {
                collection.getResult().add(new Item(jsonEntry));
            }
        }
        return collection;
    }

	public static Item convertJsonObject(JsonObject jsonObject) {
		return new Item(jsonObject);
	}


    //todo//////////////////////////////////////////////////////////////////////////////
    //todo////////////// above methods are refactored - 1st round //////////////////////
    //todo//////////////     todo for below methods   - 1st round //////////////////////
    //todo//////////////////////////////////////////////////////////////////////////////


	private static void setDocumentumObjectProperties(DocumentumObject documentumObject, HashMap<String,Object> properties){
		ArrayList<DocumentumProperty> objectProperties = documentumObject.getProperties();
		Iterator<Entry<String, Object>> it = properties.entrySet().iterator();
	    while (it.hasNext()) {
			Map.Entry<String, Object> pair = it.next();
	    	Object value = pair.getValue();
	    	Cardinality cardinality = Cardinality.Single;
	    	if(value instanceof List<?>){
	    		cardinality = Cardinality.List;
	    	}
	    	objectProperties.add(new DocumentumProperty(pair.getKey(), pair.getValue(),cardinality));
	    }
	}
	
	private static void mapJsonObjectProperties(JsonObject jsonObject, DocumentumObject documentumObject) {
		documentumObject.setId((String) jsonObject.getPropertyByName(DocumentumProperties.OBJECT_ID));
		documentumObject.setName((String) jsonObject.getPropertyByName("object_name"));
		setDocumentumObjectProperties(documentumObject, jsonObject.getProperties());
		documentumObject.setDefinition(jsonObject.getDefinition());
		Object lockUser = jsonObject.getPropertyByName("r_lock_owner");
		if (lockUser != null && lockUser.toString().length() > 0) {
			documentumObject.setCheckedOut(true);
			documentumObject.setLockUser(lockUser.toString());
		}
	}

	private static DocumentumObject getDocumentumObject(JsonObject jsonObject) {
		JsonLink link = DctmRestClient.getLink(jsonObject.getLinks(), "canonical");
		if(link == null){
			link = DctmRestClient.getLink(jsonObject.getLinks(), "self");
		}
		String[] linkParts = link.getHref().split("/");
		String baseType = linkParts[linkParts.length - 2];
		DocumentumObject documentumObject = createDocumentumObject(baseType);
		return documentumObject;
	}

	public static <T extends DocumentumObject> T convertJsonObject(JsonObject jsonObject, Class<T> classType)
			throws InstantiationException, IllegalAccessException {
		T documentumObject = classType.newInstance();
		mapJsonObjectProperties(jsonObject, documentumObject);
		return documentumObject;

	}

	private static DocumentumObject createDocumentumObject(String baseTypeId) {
		DocumentumObject documentumObject;
		switch (baseTypeId) {
		case "cmis:FOLDERS":
		case "folders":
			documentumObject = new DocumentumFolder();
			break;
		case "cabinets":
			documentumObject = new DocumentumCabinet();
			break;
		case "cmis:document":
		case "documents":
			documentumObject = new DocumentumDocument();
			break;
		default:
			documentumObject = new DocumentumObject();
		}
		return documentumObject;
	}



    private static DocumentumObject ConvertCoreRSJsonEntry(JsonEntry jsonEntry) {
        Content content = jsonEntry.getContent();
        String linkUrl = DctmRestClient.getLink(content.getLinks(), "self").getHref();
        String[] linkParts = linkUrl.split("/");
        String baseType = linkParts[linkParts.length - 2];
        DocumentumObject documentumObject = createDocumentumObject(baseType);
        convertCoreRSContent(content, documentumObject);
        return documentumObject;
    }

	private static void convertCoreRSContent(Content content, DocumentumObject documentumObject) {

		documentumObject.setId(content.getPropertyByName("r_object_id").toString());
		documentumObject.setName(content.getPropertyByName("object_name").toString());
		setDocumentumObjectProperties(documentumObject, content.getProperties());
		Object lockUser = content.getPropertyByName("r_lock_owner");
		if (lockUser != null && lockUser.toString().length() > 0) {
			documentumObject.setCheckedOut(true);
			documentumObject.setLockUser(lockUser.toString());
		}
	}

}
