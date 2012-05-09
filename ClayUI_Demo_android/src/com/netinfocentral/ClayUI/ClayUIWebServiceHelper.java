package com.netinfocentral.ClayUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.ParseException;
import android.util.Log;

public abstract class ClayUIWebServiceHelper {
    
    protected int applicationID;
    protected String uri;
    
    public ClayUIWebServiceHelper(int applicationID, String uri) {
	this.applicationID = applicationID;
	this.uri = uri + this.applicationID;
    }
    
    // generic web service retrieval system to return JSON string
    protected String getWebServiceData(String uri) {

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
