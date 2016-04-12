package com.emc.documentum.dtos;

import java.util.HashMap;

public class DocumentCreation {

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
		return "DocumentCreation [parentId=" + parentId + ", properties=" + properties + "]";
	}

}
