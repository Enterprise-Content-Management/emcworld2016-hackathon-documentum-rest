/*
 * Copyright (c) 2014. EMC Corporation. All Rights Reserved.
 */
package com.emc.documentum.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonEntry {
	@JsonProperty
	private String id;
	@JsonProperty
	private String title;
	@JsonProperty
	private String updated;
	@JsonProperty
	private String summary;
	@JsonProperty
	private Content content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getContentSrc() {
		return content == null ? null : content.getSrc();
	}

	public String getContentType() {
		return content == null ? null : content.getType();
	}

	public JsonObject getContentObject() {
		return content;
	}

	public static class Content extends JsonObject {
		private String src;
		@JsonProperty("content-type")
		private String contentType;
		
		public String getSrc() {
			return src;
		}

		public void setSrc(String src) {
			this.src = src;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}


	}
}