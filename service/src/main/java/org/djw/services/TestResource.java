package org.djw.services;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class TestResource extends ServerResource {
	final static Logger logger = Logger.getLogger(TestResource.class);
	@Get
	public Representation represent() throws JSONException {
		Representation rep = null;
		String Error = "";
		if (getRequest().getAttributes().get("Error") != null ) Error = (String) getRequest().getAttributes().get("Error");
		JSONObject jResponse = new JSONObject();
		JSONObject jTestResponse = new JSONObject();
		if (Error.equals("")){
			jTestResponse.put("Message", "This is a test message");
			jResponse.put("StatusCode", 0);
			jResponse.put("body", jTestResponse);
		} else {
			JSONObject errorJson = new JSONObject();
			int errorCode = 200;
			String errorMessage = "This is a test error message";
			JSONObject jError = errorJson.put("errorCode", errorMessage);
			jTestResponse.put("ErrorCode", "11111");
			jTestResponse.put("ErrorMessage", "This is an error message");
			jResponse.put("StatusCode", 1);
			jResponse.put("Message", "This is an error status");
			jResponse.put("body",jError);
		}
		rep = new JsonRepresentation(jResponse);
		return rep;
	}
	
	@Post("json:json")
	public Representation acceptJson(JsonRepresentation represent) throws ResourceException {
		Representation rep = null;
		try {
			JSONObject jsonobject = represent.getJsonObject();
			String requestString = jsonobject.getString("request");
			JSONObject json = new JSONObject(requestString);
//			String test = json.getString("test");

			getResponse().setStatus(Status.SUCCESS_ACCEPTED);
			JSONStringer jsReply = new JSONStringer();
			jsReply.object();
			jsReply.key("PostedFields").value(json);
			jsReply.endObject();
			JSONObject jReply = new JSONObject(jsReply.toString());
			JSONObject responseJson = new JSONObject();
			responseJson. put("StatusCode",0);
			responseJson.put("StatusMessage","");
			responseJson.put("Body",jReply);
			JSONObject jResponse = responseJson;
			rep = new JsonRepresentation(jResponse);
			getResponse().setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			e.printStackTrace();
			JSONStringer jsReply = new JSONStringer();
			try {
				jsReply.object();
				jsReply.key("CODE").value("Error");
				jsReply.key("DESC").value(e.getMessage());
				jsReply.endObject();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
		return rep;
	}
}