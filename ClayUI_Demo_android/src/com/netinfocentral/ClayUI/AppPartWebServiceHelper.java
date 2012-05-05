package com.netinfocentral.ClayUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

public class AppPartWebServiceHelper {

    // define class level variables
    private int applicationID;
    private String uri = "services/GetAppParts.php?AppID=";

    // default constructor
    public AppPartWebServiceHelper(int applicationID, String uri) {
	this.applicationID = applicationID;
	this.uri = uri + this.uri + this.applicationID;
    }

    // return Arraylist of AppParts from JSON delivered by ClayUI Web service
    public List<AppPart> getAppParts() {
	List<AppPart> appParts = new ArrayList<AppPart>();

	try {
	    JSONArray array = new JSONArray(getWebServiceData(this.uri));
	    Log.i(AppPartWebServiceHelper.class.getName(), array.length() + " records retrieved.");

	    // loop through array, pull JSON objects out and push the values into object
	    for (int i=0; i < array.length(); i++) {
		JSONObject jObject = array.getJSONObject(i);
		AppPart appPart = new AppPart(jObject.getLong("AppPartID"), 
			jObject.getString("Description"),
			jObject.getInt("Version"));

		// add to arraylist
		appParts.add(appPart);
		Log.i(AppPartWebServiceHelper.class.getName(), "Added : " + appPart.getAppPartName());
	    }
	}
	catch (Exception e)	 {
	    Log.e(AppPartWebServiceHelper.class.getName(), e.getMessage());
	} 

	return appParts;
    }

    // generic web service retrieval system to return JSON string
    private String getWebServiceData(String uri) {

	StringBuilder builder = new StringBuilder();
	HttpClient client = new DefaultHttpClient();
	try {
	    HttpGet httpGet = new HttpGet(uri);
	    HttpResponse response = client.execute(httpGet);
	    StatusLine statusLine = response.getStatusLine();
	    int statusCode = statusLine.getStatusCode();

	    if (statusCode == 200) {
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(content));
		String line;
		while ((line = reader.readLine()) != null) {
		    builder.append(line);
		}
	    }
	    else {
		Log.e(ParseException.class.toString(), "Failed to download file");
	    }
	}
	catch (ClientProtocolException e) {
	    Log.e("ClientProtocolException", e.getMessage());
	}
	catch (IOException e) {
	    Log.e("IOException", e.getMessage());
	}
	catch (Exception e) {
	    Log.e("Exception", e.getMessage());
	}
	return builder.toString();		
    }

}
