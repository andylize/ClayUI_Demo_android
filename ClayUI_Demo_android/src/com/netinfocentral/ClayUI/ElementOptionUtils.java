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
    }
    
    // method to synchornize local databases with ClayUI
    public void sync() {
	// get element options from web -- insert into local table
	this.elementOptionDataAdapter.open();
	elementOptionDataAdapter.syncWithTempTable(elementOptionWebServiceHelper.getElementOptions());
	this.elementOptionDataAdapter.close();
    }
    
    // method to list all element options in logcat
    public void listElementOptions() {
	this.elementOptionDataAdapter.open();
	List<ElementOption> elementOptions = elementOptionDataAdapter.getAllElementOptions();
	this.elementOptionDataAdapter.close();
	
	Iterator<ElementOption> iterator = elementOptions.iterator();
	
	while (iterator.hasNext()) {
	    ElementOption option = (ElementOption)iterator.next();
	    Log.i("listElementOption", option.toString());
	}
    }
    
    // method to return list array of Elements
    public List<ElementOption> getElementOptions() {
	this.elementOptionDataAdapter.open();
	List<ElementOption> options = this.elementOptionDataAdapter.getAllElementOptions();
	this.elementOptionDataAdapter.close();
	return options;
    }
}
