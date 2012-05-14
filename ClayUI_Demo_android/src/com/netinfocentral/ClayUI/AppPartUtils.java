package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

//class to handle the work of binding the helper objects for the AppParts
public class AppPartUtils {
    
    // define class variables
    private int applicationID;
    private Context context;
    private String baseUri = "";

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
	appPartWebServiceHelper = new AppPartWebServiceHelper(this.applicationID, this.baseUri, this.context);

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
	    Log.i("listAppParts", appPart.toString());
	}
    }
    
    public List<AppPart> getAppParts() {
	return this.appPartDataAdapter.getAllAppParts();
    }
    
    public AppPart getAppPart(long appPartID) {
	return this.appPartDataAdapter.getAppPart(appPartID);
    }
    
}
