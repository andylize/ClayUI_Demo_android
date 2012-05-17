package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

// class to handle the work of binding the helper objects for the ElementOptions
public class ElementOptionUtils {
    //define member variables
    private int applicationID;
    private Context context;
    private String baseUri = "";
    
    // define local data sources
    private ElementOptionDataAdapter elementOptionDataAdapter;
    
    // define local web service helpers
    private ElementOptionWebServiceHelper elementOptionWebServiceHelper;
    
    // default constructor
    public ElementOptionUtils(int applicationID, Context context, String baseUri) {
	this.applicationID = applicationID;
	this.context = context;
	this.baseUri = baseUri;
	
	// instantiate data sources
	elementOptionDataAdapter = new ElementOptionDataAdapter(context);
	
	// instantiate web service helper
	elementOptionWebServiceHelper = new ElementOptionWebServiceHelper(this.applicationID, this.baseUri);
	
	// open connections
	elementOptionDataAdapter.open();
    }
    
    // method to synchornize local databases with ClayUI
    public void sync() {
	// get element options from web -- insert into local table
	elementOptionDataAdapter.syncWithTempTable(elementOptionWebServiceHelper.getElementOptions());
    }
    
    // method to list all element options in logcat
    public void listElementOptions() {
	List<ElementOption> elementOptions = elementOptionDataAdapter.getAllElementOptions();
	
	Iterator<ElementOption> iterator = elementOptions.iterator();
	
	while (iterator.hasNext()) {
	    ElementOption option = (ElementOption)iterator.next();
	    Log.i("listElementOption", option.toString());
	}
    }
    
    // method to return list array of Elements
    public List<ElementOption> getElementOptions() {
	return this.elementOptionDataAdapter.getAllElementOptions();
    }
}