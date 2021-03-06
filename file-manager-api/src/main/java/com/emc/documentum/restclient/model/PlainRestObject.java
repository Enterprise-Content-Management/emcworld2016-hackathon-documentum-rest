/*
 * Copyright (c) 2014. EMC Corporation. All Rights Reserved.
 */
package com.emc.documentum.restclient.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * the plain RestObject implementation which has properties only
 * normally used when create/update the RestObject
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlainRestObject extends JsonObject {
	private final String type;
	private final HashMap<String, Object> properties;

	public PlainRestObject(String type, Map<String, Object> properties) {
		this.properties = new HashMap<>(properties);
		this.type = type;
	}
	
	public PlainRestObject(HashMap<String, Object> properties) {
		this(null, properties);
	}
	
	public PlainRestObject(String...properties) {
		if(properties == null || properties.length %2 != 0) {
			throw new IllegalArgumentException("the properties must be key value pair");
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(int i=0;i<properties.length;i+=2) {
			map.put(properties[i], properties[i+1]);
		}
		this.type = null;
		this.properties = map;
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDefinition() {
		return null;
	}

	@Override
	public String getPropertiesType() {
		return null;
	}

}
