package com.emc.documentum.dtos;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DocumentumObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7947371281083780726L;
	private String id;
	private String path;
	private String name;
	private String type;
	private String definition;
	private Boolean isCheckedOut = false;
	private String lockUser;
	private ArrayList<DocumentumProperty> properties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getLockUser() {
		return lockUser;
	}

	public void setLockUser(String lockUser) {
		this.lockUser = lockUser;
	}

	public Boolean isCheckedOut() {
		return isCheckedOut;
	}

	public void setCheckedOut(Boolean isCheckedOut) {
		this.isCheckedOut = isCheckedOut;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public DocumentumObject() {
		properties = new ArrayList<>();
		setType("Object");
	}

	public DocumentumObject(String id, String name, String type) {
		this();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public DocumentumObject(String id, String name) {
		this();
		this.id = id;
		this.name = name;
	}

	public ArrayList<DocumentumProperty> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<DocumentumProperty> properties) {
		this.properties = properties;
	}

}
