package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class AppPartUtils {
    
 // define class variables
    int applicationID;
    String applicationName;
    Context context;
    String baseUri = "";

    // define local data sources
    private AppPartDataAdapter appPartDataAdapter;

    // define local web service helpers
    private AppPartWebServiceHelper appPartWebServiceHelper;
    
    // default constructor
    public AppPartUtils(int applicationID, Context context, String baseUri) {
	this.applicationID = applicationID;
	this.context = context;
	this.baseUri = baseUri;

	// instantiate data sources
	appPartDataAdapter = new AppPartDataAdapter(this.context);
	
	// instantiate web service helper
	appPartWebServiceHelper = new AppPartWebServiceHelper(this.applicationID, this.baseUri);

	// open connections
	appPartDataAdapter.open();	
    }
    
    // method to synchronize local database with ClayUI 
    public void sync() {
	// get app parts from web -- insert into temp table
	appPartDataAdapter.syncWithTempTable(appPartWebServiceHelper.getAppParts());
    }
    
    // method to list all appParts in logCat
    public void listAppParts() {
	List<AppPart> appParts = appPartDataAdapter.getAllAppParts();

	Iterator<AppPart> iterator = appParts.iterator();

	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();
	    Log.i("listAppParts", "ID: " + appPart.getRecordID() + " " + appPart.getAppPartName() + " " + appPart.getVersion());
	}
    }
    
}
