package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

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
	appPartDataAdapter = new AppPartDataAdapter(this.context, this.applicationID, this.baseUri);
	
	// instantiate web service helper
	appPartWebServiceHelper = new AppPartWebServiceHelper(this.applicationID, this.baseUri, this.context);
	
    }
    
    // method to synchronize local database with ClayUI 
    public void sync() {
	// get app parts from web -- insert into temp table
	appPartDataAdapter.open();
	appPartDataAdapter.syncWithTempTable(appPartWebServiceHelper.getAppParts());
	appPartDataAdapter.close();
    }
    
    public void saveAppPartDataLocal(String appPartName, LinearLayout layout) {
	appPartDataAdapter.open();
	appPartDataAdapter.saveAppPartData(appPartName, layout);
	appPartDataAdapter.close();
    }
    
    // method to list all appParts in logCat
    public void listAppParts() {
	appPartDataAdapter.open();
	List<AppPart> appParts = appPartDataAdapter.getAllAppParts();
	appPartDataAdapter.close();
	Iterator<AppPart> iterator = appParts.iterator();

	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();
	    Log.i("listAppParts", appPart.toString());
	}
    }
    
    public List<AppPart> getAppParts() {
	appPartDataAdapter.open();
	List<AppPart> appParts = this.appPartDataAdapter.getAllAppParts();
	this.appPartDataAdapter.close();
	return appParts;
    }
    
    public AppPart getAppPart(int appPartID) {
	this.appPartDataAdapter.open();
	AppPart appPart = this.appPartDataAdapter.getAppPart(appPartID);
	this.appPartDataAdapter.close();
	return appPart;
    }
    
}
