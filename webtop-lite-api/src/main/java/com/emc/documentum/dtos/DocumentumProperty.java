package com.emc.documentum.dtos;

import com.emc.documentum.constants.Cardinality;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DocumentumProperty {

	private String localName;

	private String displayName;

	@JsonFormat(with = { JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
	private Object value;

	private Cardinality cardinality;

	public DocumentumProperty(String localName, String displayName, Object value, Cardinality cardinality) {
		this();
		this.localName = localName;
		this.displayName = displayName;
		this.value = value;
		this.cardinality = cardinality;
	}

	public DocumentumProperty(String localName, Object value, String displayName) {
		this();
		this.localName = localName;
		this.displayName = displayName;
		this.value = value;
	}

	public DocumentumProperty(String localName, Object value, Cardinality cardinality) {
		this();
		this.localName = localName;
		this.displayName = localName;
		this.value = value;
		this.cardinality = cardinality;
	}

	public DocumentumProperty(String localName, Object value) {
		this();
		this.localName = localName;
		this.displayName = localName;
		this.value = value;
	}

	public DocumentumProperty() {
		this.cardinality = Cardinality.Single;
		value = null;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Cardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}

}
