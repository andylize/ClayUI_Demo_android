package com.netinfocentral.ClayUI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ElementDatabaseHelper extends SQLiteOpenHelper {

 // define class variables
  	private static final int DATABASE_VERSION = 1;
  	private static final String DATABASE_NAME = "ClayUI.db";
  	public static final String TABLE_NAME = "Elements";
  	public static final String TEMP_TABLE_NAME = "TEMP_Elements";
  	
  	// column definitions
  	public static final String COLUMN_ID = "_id";
  	public static final String COLUMN_APP_PART_ID = "AppPartID";
  	public static final String COLUMN_ELEMENT_NAME = "ElementName";
  	public static final String COLUMN_ELEMENT_TYPE = "ElementType";
  	public static final String COLUMN_ELEMENT_LABEL = "ElementLabel";
  	public static final String COLUMN_LIST_ORDER = "ListOrder";
  	public static final String COLUMN_VERSION = "Version";
  	
  	// command to create the table
  	public static final String TABLE_CREATE =
  		"CREATE TABLE " + TABLE_NAME + " (" +
  		COLUMN_ID + " integer primary key, " +
  		COLUMN_APP_PART_ID + " integer, " +
  		COLUMN_ELEMENT_NAME + " text, " +
  		COLUMN_ELEMENT_TYPE + " integer, " +
  		COLUMN_ELEMENT_LABEL + " text, " +
  		COLUMN_LIST_ORDER + " integer, " +
  		COLUMN_VERSION + " integer);";
  	public static final String TEMP_TABLE_CREATE =
  		"CREATE TABLE " + TEMP_TABLE_NAME + " (" +
  		COLUMN_ID + " integer primary key, " +
  		COLUMN_APP_PART_ID + " integer, " +
  		COLUMN_ELEMENT_NAME + " text, " +
  		COLUMN_ELEMENT_TYPE + " integer, " +
  		COLUMN_ELEMENT_LABEL + " text, " +
  		COLUMN_LIST_ORDER + " integer, " +
  		COLUMN_VERSION + " integer);";
  	
  	// commands to delete the tables
  	public static final String TABLE_DELETE = 
  		"DROP TABLE IF EXISTS " + TABLE_NAME + ";";
  	public static final String TEMP_TABLE_DELETE =
  		"DROP TABLE IF EXISTS " + TEMP_TABLE_NAME + ";";
  	
  	// default constructor
  	ElementDatabaseHelper(Context context) {
  	    //this.databaseName = applicationName;
  	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  	}
  	
  	// create database if it does not exist
  	@Override
  	public void onCreate(SQLiteDatabase db) {
  	    // do nothing.  This is handled by ClayUIDatabaseHelper
  	}
  	
  	// upgrade database if necessary
  	@Override
  	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  	    // do nothing.  This is handled by ClayUIDatabaseHelper  	    
  	}

}
