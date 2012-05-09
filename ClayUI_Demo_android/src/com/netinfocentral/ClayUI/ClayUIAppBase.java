package com.netinfocentral.ClayUI;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ClayUIAppBase {

    // define class variables
    int applicationID;
    String applicationName;
    Context context;
    String baseUri = "";

    // define local data sources
    private ClayUIDatabaseHelper dbHelper;
    //private AppPartDataAdapter appPartDataAdapter;
    private AppPartUtils appPartUtils;
    private ElementUtils elementUtils;

    // define local web service helpers
    //private AppPartWebServiceHelper appPartWebServiceHelper;

    // define default constructor
    public ClayUIAppBase(int applicationID, Context context, String baseUri) {
	this.applicationID = applicationID;
	this.context = context;
	this.baseUri = baseUri;
	
	// create database if necessary
	dbHelper = new ClayUIDatabaseHelper(this.context);
	
	// instantiate data sources
	//appPartDataAdapter = new AppPartDataAdapter(this.context);
	
	// instantiate data utils
	appPartUtils = new AppPartUtils(this.applicationID, this.context, this.baseUri);
	elementUtils = new ElementUtils(this.applicationID, this.context, this.baseUri);

	// open connections
	//appPartDataAdapter.open();

	// load appPart data
	this.syncLayoutStructure();
    }

    // method to sync ClayUI structure
    public void syncLayoutStructure() {
	appPartUtils.sync();
	appPartUtils.listAppParts();
	elementUtils.sync();
	elementUtils.listElements();
    }
    
    // method to poen database connection
    public void openDB() {
	//this.appPartDataAdapter.open();
    }
    
    public void closeDB() {
	//this.appPartDataAdapter.close();
    }

    // method to get app part structures from web service
//    private void loadAppPartDataSchema() {
//
//	// get app parts
//	this.appPartWebServiceHelper = new AppPartWebServiceHelper(this.applicationID, this.baseUri);
//
//	List<AppPart> appParts = this.appPartWebServiceHelper.getAppParts();
//	Iterator<AppPart> iterator = appParts.iterator();
//
//	while (iterator.hasNext()) {
//	    AppPart appPart = (AppPart)iterator.next();
//	    this.appPartDataAdapter.createAppPart(appPart.getRecordID(), appPart.getAppPartName(), appPart.getVersion());
//	}
//    }
    //method to return list of AppParts
    public List<AppPart> getAllAppParts() {
	return this.appPartUtils.getAppParts();
    }
    // method to return list of Elements
    public List<Element> getAllElements() {
	return this.elementUtils.getElements();
    }
}
