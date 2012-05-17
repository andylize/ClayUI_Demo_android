package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class ElementOptionWebServiceHelper extends ClayUIWebServiceHelper {
    // define class level variables
    private static final String SERVICE_URI = "services/GetElementOptions.php?AppID=";
    
    // default constructor
    public ElementOptionWebServiceHelper(int applicationID, String uri) {
	super(applicationID, uri + ElementOptionWebServiceHelper.SERVICE_URI);
    }
    
    // method to return Arraylist of Element Options from JSON delivered by ClayUI Web Service
    public List<ElementOption> getElementOptions() {
	List<ElementOption> options = new ArrayList<ElementOption>();

	try {
	    JSONArray array = new JSONArray(getWebServiceData(uri));
	    Log.i(ElementOptionWebServiceHelper.class.getName(), array.length() + " records retrieved.");

	    // loop through array, pull JSON objects out and push the values into the Element object
	    for (int i=0; i < array.length(); i++) {
		JSONObject jObject = array.getJSONObject(i);
		ElementOption option = new ElementOption(jObject.getLong("ElementOptionID"),
			jObject.getInt("AppPartID"),
			jObject.getInt("ElementID"), 
			jObject.getString("Value"),
			jObject.getString("Description"),
			jObject.getInt("Version"));

		// add to arraylist
		options.add(option);
		Log.i(ElementOptionWebServiceHelper.class.getName(), "Added : " + option.getElementOptionID());
	    }
	}
	catch (Exception e) {
	    Log.e(ElementOptionWebServiceHelper.class.getName(), e.getMessage());
	}
	return options;
    }
}
