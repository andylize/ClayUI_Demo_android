package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// class to help create/update the data table for an app part
public class DataTableDatabaseHelper extends SQLiteOpenHelper {
    
    // define member variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClayUI.db";
    private int applicationID;
    private int appPartID;
    private String uri;
    private Context context;
    private List<DataTableSchema> webSchema;
    private DataTableWebServiceHelper webServiceHelper;
    private String tableCreate;
    private String tableDelete;
    
    // default constructor
    DataTableDatabaseHelper(int applicationID, int appPartID, String uri, Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	this.applicationID = applicationID;
	this.appPartID = appPartID;
	this.uri = uri;
	this.context = context;
	this.webServiceHelper = new DataTableWebServiceHelper(this.applicationID, this.appPartID, this.uri, this.context);
	this.webSchema = this.webServiceHelper.getTableSchema();
	this.setCreateStatement();
	this.setDeleteStatement();
    }
    
    // method to dynamically generate a create table statement
    private void setCreateStatement() {
	AppPartDataAdapter adapter = new AppPartDataAdapter(this.context, this.applicationID, this.uri);
	adapter.open();
	AppPart appPart = adapter.getAppPart(this.appPartID);
	adapter.close();
	
	Iterator<DataTableSchema> iterator = webSchema.iterator();
	
	this.tableCreate = "CREATE TABLE " + appPart.getAppPartName() + " (";
	
	// loop through iterator and generate create table string
	while (iterator.hasNext()) {
	    DataTableSchema schema = (DataTableSchema)iterator.next();
	    this.tableCreate = this.tableCreate + schema.getColumnName() + " ";
	    
	    if (schema.getDataType().equals("int")) {
		this.tableCreate = this.tableCreate + "integer ";
	    }else if (schema.getDataType().equals("decimal")) {
		this.tableCreate = this.tableCreate + "numeric ";
	    }else {
		this.tableCreate = this.tableCreate + "text ";
	    }
	    
	    if (schema.isPrimaryKey()) {
		this.tableCreate = this.tableCreate + "primary key";
	    }
	    
	    this.tableCreate = this.tableCreate + ", ";
	}
	
	// trim off trailing ', '
	this.tableCreate = this.tableCreate.substring(0, this.tableCreate.length() - 2) + ");";
    }
    
    /**
     * @return tableCreate
     */
    public String getTableCreate() {
	return this.tableCreate;
    }
    
    // method to dynamically generate a drop table statement
    private void setDeleteStatement() {
	AppPartDataAdapter adapter = new AppPartDataAdapter(this.context, this.applicationID, this.uri);
	adapter.open();
	AppPart appPart = adapter.getAppPart(this.appPartID);
	adapter.close();
	this.tableDelete = "DROP TABLE IF EXISTS " + appPart.getAppPartName() + ";";
    }
    
    /** 
     * 
     * @return
     */
    public String getTableDelete() {
	return this.tableDelete;
    }
    
    /**
     * 
     * @param appPartID
     * @return true if table exists in sqlite database
     */
    private boolean tableExists(int appPartID) {
	AppPartDataAdapter adapter = new AppPartDataAdapter(this.context, this.applicationID, this.uri);
	adapter.open();
	AppPart appPart = adapter.getAppPart(this.applicationID);
	boolean doesExist = adapter.dataTableExists(appPart.getAppPartName());
	adapter.close();
	return doesExist;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
	// do nothing.  This is handled by ClayUIDatabaseHelper
	
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// do nothing.  This is handled by ClayUIDatabaseHelper

    }

}
