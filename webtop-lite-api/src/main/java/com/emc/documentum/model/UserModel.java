package com.emc.documentum.model;

import java.util.HashMap;


public class UserModel {

	String name;
	String type;
	String definition;
	HashMap<String,String> properties;
	
	public UserModel(){
		properties = new HashMap<>();
	}
	
	@Override
	public String toString() {
		return "UserModel [name=" + name + ", type=" + type + ", definition=" + definition + ", properties="
				+ properties + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String user) {
		this.name = user;
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
	public HashMap<String, String> getProperties() {
		return properties;
	}
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
}
