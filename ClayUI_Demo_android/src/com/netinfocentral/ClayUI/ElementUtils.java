package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

// class to handle the work of binding the helper objects for the Elements
public class ElementUtils {
    
    //define member variables
    private int applicationID;
    private Context context;
    private String baseUri = "";
    
    // define local data sources
    private ElementDataAdapter elementDataAdapater;
    
    // define local web service helpers
    private ElementWebServiceHelper elementWebServiceHelper;
    
    // default constrcutor
    public ElementUtils(int applicationID, Context context, String baseUri) {
	this.applicationID = applicationID;
	this.context = context;
	this.baseUri = baseUri;
	
	// instantiate data sources
	elementDataAdapater = new ElementDataAdapter(this.context);
	
	// instantiate web service helper
	elementWebServiceHelper = new ElementWebServiceHelper(this.applicationID, this.baseUri);
    }
    
    // method to synchronize local databases with ClayUI
    public void sync() {
	// get elements from web -- insert into table
	this.elementDataAdapater.open();
	elementDataAdapater.syncWithTempTable(elementWebServiceHelper.getElements());
	this.elementDataAdapater.close();
    }
    
    // method to list all elements in logCat
    public void listElements() {
	this.elementDataAdapater.open();
	List<Element> elements = elementDataAdapater.getAllElements();
	this.elementDataAdapater.close();
	Iterator<Element> iterator = elements.iterator();
	
	while (iterator.hasNext()) {
	    Element element = (Element)iterator.next();
	    Log.i("listElements", element.toString());
	}
    }
    
    // method to return list array of Elements
    public List<Element> getElements() {
	this.elementDataAdapater.open();
	List<Element> element = this.elementDataAdapater.getAllElements();
	this.elementDataAdapater.close();
	return element;
    }
}
