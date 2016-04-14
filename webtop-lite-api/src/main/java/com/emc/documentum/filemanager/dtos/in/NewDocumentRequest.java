package com.emc.documentum.filemanager.dtos.in;

import java.util.HashMap;

public class NewDocumentRequest {

	private String parentId;

	private HashMap<String, Object> properties;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "NewDocumentRequest [parentId=" + parentId + ", properties=" + properties + "]";
	}

}
