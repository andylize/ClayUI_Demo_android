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
import android.os.AsyncTask;
import android.util.Log;

public abstract class ClayUIWebServiceHelper {
    
    protected int applicationID;
    protected String uri;
    protected String jsonResult;
    
    public ClayUIWebServiceHelper(int applicationID, String uri) {
	this.applicationID = applicationID;
	this.uri = uri + this.applicationID;
    }
    
    // generic web service retrieval system to return JSON string
    protected String getWebServiceData(String uri) {
	
//	DownloadJSONDataTask task = new DownloadJSONDataTask();
//	task.execute(new String [] {uri});
//	return this.jsonResult;
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
		Log.e(ClayUIWebServiceHelper.class.getName(), "Failed to download file");
	    }
	}
	catch (ClientProtocolException e) {
	    Log.e(ClayUIWebServiceHelper.class.getName(), e.getMessage());
	}
	catch (IOException e) {
	    Log.e(ClayUIWebServiceHelper.class.getName(), e.getMessage());
	}
	catch (Exception e) {
	    Log.e(ClayUIWebServiceHelper.class.getName(), e.getMessage());
	}
	return builder.toString();		
    }
    
    // class to retrieve JSON data from backtround thread
    private class DownloadJSONDataTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... uris) {
	    
	    String jsonResponse = "";
	    
	    for (String uri : uris) {
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
			Log.e(ClayUIWebServiceHelper.class.getName(), "Failed to download files.");
		    }
		}catch (ClientProtocolException e) {
		    Log.e(ClayUIWebServiceHelper.class.getName(), e.getMessage());
		}
		catch (IOException e) {
		    Log.e(ClayUIWebServiceHelper.class.getName(), e.getMessage());
		}
		catch (Exception e) {
		    Log.e(ClayUIWebServiceHelper.class.getName(), e.getMessage());
		}
		jsonResponse = builder.toString();
	    }
	    return jsonResponse;
	}
	
	@Override
	protected void onPostExecute(String result) {
	    jsonResult = result;
	}
	
    }
}
