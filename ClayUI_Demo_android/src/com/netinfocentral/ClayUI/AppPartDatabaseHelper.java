package com.netinfocentral.ClayUI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppPartDatabaseHelper extends SQLiteOpenHelper {

    // define class variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClayUI.db";
    public static final String TABLE_NAME = "AppParts";
    public static final String TEMP_TABLE_NAME = "TEMP_AppParts";

    // column definitions
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_APP_PART_NAME = "AppPartName";
    public static final String COLUMN_VERSION = "Version";

    // commands to create the tables
    public static final String TABLE_CREATE =
	    "CREATE TABLE " + TABLE_NAME + " (" +
		    COLUMN_ID + " integer primary key, " +
		    COLUMN_APP_PART_NAME + " text, " +
		    COLUMN_VERSION + " integer);";
    public static final String TEMP_TABLE_CREATE =
	    "CREATE TABLE " + TEMP_TABLE_NAME + " (" +
			    COLUMN_ID + " integer primary key, " +
			    COLUMN_APP_PART_NAME + " text, " +
			    COLUMN_VERSION + " integer);";
    
    // commands to delete the tables
    public static final String TABLE_DELETE =
	    "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    public static final String TEMP_TABLE_DELETE =
            "DROP TABLE IF EXISTS " + TEMP_TABLE_NAME + ";";
    
    
    // default constructor
    AppPartDatabaseHelper(Context context) {
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
