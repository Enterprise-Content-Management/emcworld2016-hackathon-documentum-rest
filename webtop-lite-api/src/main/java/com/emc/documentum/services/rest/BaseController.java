package com.emc.documentum.services.rest;

import net.minidev.json.JSONObject;

public abstract class BaseController {

	
	String commonResponse() {
		JSONObject resultJson = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("error", null);
		resultJson.put("result", json);
		return resultJson.toJSONString();
	}
	
	
	String errorResponse(String error) {
		JSONObject resultJson = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("success", false);
		json.put("error", error);
		resultJson.put("result", json);
		return resultJson.toJSONString();
	}
	
}
