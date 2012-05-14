package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class AppPartWebServiceHelper extends ClayUIWebServiceHelper {

    // define member variables
    private static final String SERVICE_URI = "services/GetAppParts.php?AppID=";
    private Context context;
    
    // default constructor
    public AppPartWebServiceHelper(int applicationID, String uri, Context context) {
	super(applicationID, uri + AppPartWebServiceHelper.SERVICE_URI);
	this.context = context;
    }

    // return Arraylist of AppParts from JSON delivered by ClayUI Web Service
    public List<AppPart> getAppParts() {
	List<AppPart> appParts = new ArrayList<AppPart>();

	try {
	    JSONArray array = new JSONArray(getWebServiceData(uri));
	    Log.i(AppPartWebServiceHelper.class.getName(), array.length() + " records retrieved.");

	    // loop through array, pull JSON objects out and push the values into object
	    for (int i=0; i < array.length(); i++) {
		JSONObject jObject = array.getJSONObject(i);
		AppPart appPart = new AppPart(jObject.getLong("AppPartID"), 
			jObject.getString("AppPartName"),
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
}
