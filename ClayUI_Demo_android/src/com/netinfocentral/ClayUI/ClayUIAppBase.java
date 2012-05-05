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
    private AppPartDataAdapter appPartDataAdapter;

    // define local web service helpers
    private AppPartWebServiceHelper appPartWebServiceHelper;

    // define default constructor
    public ClayUIAppBase(int applicationID, Context context, String baseUri) {
	this.applicationID = applicationID;
	this.context = context;
	this.baseUri = baseUri;

	// instantiate data sources
	appPartDataAdapter = new AppPartDataAdapter(this.context);

	// open connections
	appPartDataAdapter.open();

	// load appPart data
	this.loadAppPartDataSchema();
    }

    // method to sync ClayUI structure
    public void syncLayoutStructure() {

    }

    // method to get app part structures from web service
    private void loadAppPartDataSchema() {

	// get app parts
	this.appPartWebServiceHelper = new AppPartWebServiceHelper(this.applicationID, this.baseUri);

	List<AppPart> appParts = this.appPartWebServiceHelper.getAppParts();
	Iterator<AppPart> iterator = appParts.iterator();

	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();
	    this.appPartDataAdapter.createAppPart(appPart.getAppPartName(), appPart.getVersion());
	}
    }

    // method to create test data
    public void createTestData() {

	Date dt = new Date();

	appPartDataAdapter.createAppPart("TestAppPart_" + dt.getTime(), 1);

    }

    public void listAppParts() {
	List<AppPart> appParts = appPartDataAdapter.getAllAppParts();

	Iterator<AppPart> iterator = appParts.iterator();

	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();
	    Log.i("listAppParts", appPart.getAppPartName() + " " + appPart.getVersion());
	}
    }
}
