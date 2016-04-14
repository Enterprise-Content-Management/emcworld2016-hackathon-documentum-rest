package com.emc.documentum.filemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.emc.documentum.exceptions.DocumentumException;

import net.minidev.json.JSONObject;

public abstract class BaseController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DocumentumException.class)
    public String onDocumentumException(DocumentumException e) {
        return errorResponse(e.getLocalizedMessage());
    }

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
