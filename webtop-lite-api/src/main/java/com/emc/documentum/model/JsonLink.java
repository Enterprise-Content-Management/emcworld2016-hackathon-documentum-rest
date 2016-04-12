/*
 * Copyright (c) 2014. EMC Corporation. All Rights Reserved.
 */
package com.emc.documentum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonLink {
	@JsonProperty
	private String rel;
	@JsonProperty
	private String hreftemplate;
	@JsonProperty
	private String href;
	@JsonProperty
	private String title;
	@JsonProperty
	private String type;

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getHreftemplate() {
		return hreftemplate;
	}

	public void setHreftemplate(String hreftemplate) {
		this.hreftemplate = hreftemplate;
	}

	public boolean isTemplate() {
		return hreftemplate != null;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "JsonLink [rel=" + rel + ", hreftemplate=" + hreftemplate + ", href=" + href + ", title=" + title
				+ ", type=" + type + "]";
	}

}
