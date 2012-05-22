package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class ElementWebServiceHelper extends ClayUIWebServiceHelper {
    
    // define member variables
    private static final String SERVICE_URI = "services/GetElements.php?AppID=";
    
    // default constructor
    public ElementWebServiceHelper(int applicationID, String uri) {
	super(applicationID, uri + ElementWebServiceHelper.SERVICE_URI);
    }
    
    // return Arraylist of Elements from JSON delivered by ClayUI Web Service
    public List<Element> getElements() {
	List<Element> elements = new ArrayList<Element>();
	
	try {
	    JSONArray array = new JSONArray(getWebServiceData(uri));
	    Log.i(ElementWebServiceHelper.class.getName(), array.length() + " records retrieved.");
	    
	    // loop through array, pull JSON objects out and push the values into the Element object
	    for (int i=0; i < array.length(); i++) {
		JSONObject jObject = array.getJSONObject(i);
		Element element = new Element(jObject.getInt("ElementID"),
			jObject.getInt("AppPartID"),
			jObject.getString("ElementName"), 
			jObject.getInt("ElementType"),
			jObject.getString("Label"),
			jObject.getInt("ListOrder"),
			jObject.getInt("Version"));
		
		// add to arraylist
		elements.add(element);
		Log.i(ElementWebServiceHelper.class.getName(), "Added : " + element.getElementName());
	    }
	}
	catch (Exception e) {
	    Log.e(ElementWebServiceHelper.class.getName(), e.getMessage());
	}
	return elements;
    }
}
