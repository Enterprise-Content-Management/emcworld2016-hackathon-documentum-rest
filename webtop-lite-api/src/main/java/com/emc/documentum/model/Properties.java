package com.emc.documentum.model;

import java.util.HashMap;

public class Properties {

	HashMap <String,Object> properties;

	public Properties(){
		properties = new HashMap<>();
	}
	
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}
	
	public void addProperty(String key, String value){
		this.properties.put(key, value);
	}
	
}