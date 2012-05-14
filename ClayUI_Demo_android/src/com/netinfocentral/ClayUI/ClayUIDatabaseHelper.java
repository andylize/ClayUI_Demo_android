package com.netinfocentral.ClayUI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClayUIDatabaseHelper {

    // define database values
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClayUI.db";
    
    // define AppPart DDL strings
    private static final String CREATE_APP_PARTS_TABLE = AppPartDatabaseHelper.TABLE_CREATE;
    private static final String CREATE_APP_PARTS_TEMP_TABLE = AppPartDatabaseHelper.TEMP_TABLE_CREATE;
    private static final String DELETE_APP_PARTS_TABLE = AppPartDatabaseHelper.TABLE_DELETE;
    private static final String DELETE_APP_PARTS_TEMP_TABLE = AppPartDatabaseHelper.TEMP_TABLE_DELETE;
    
    // define Element DDL strings
    private static final String CREATE_ELEMENTS_TABLE = ElementDatabaseHelper.TABLE_CREATE;
    private static final String CREATE_ELEMENTS_TEMP_TABLE = ElementDatabaseHelper.TEMP_TABLE_CREATE;
    private static final String DELETE_ELEMENTS_TABLE = ElementDatabaseHelper.TABLE_DELETE;
    private static final String DELETE_ELEMENTS_TEMP_TABLE = ElementDatabaseHelper.TEMP_TABLE_DELETE;
    
    // define ElementOption DDL strings
    private static final String CREATE_ELEMENT_OPTIONS_TABLE = ElementOptionDatabaseHelper.TABLE_CREATE;
    private static final String CREATE_ELEMENT_OPTIONS_TEMP_TABLE = ElementOptionDatabaseHelper.TEMP_TABLE_CREATE;
    private static final String DELETE_ELEMENT_OPTIONS_TABLE = ElementOptionDatabaseHelper.TABLE_DELETE;
    private static final String DELETE_ELEMENT_OPTIONS_TEMP_TABLE = ElementOptionDatabaseHelper.TEMP_TABLE_DELETE;
    
    // define class variables
    private final Context context;
    private DatabaseHelper dbHelper;
    
    // default constructor
    ClayUIDatabaseHelper(Context context) {
	this.context = context;
	this.dbHelper = new DatabaseHelper(this.context);
    }
    
    public SQLiteDatabase getWritableDatabase() {
	return dbHelper.getWritableDatabase();
    }
    
    public void close() {
	dbHelper.close();
    }
    
    // abstract class to handle creation and upgrade of database
    private static class DatabaseHelper extends SQLiteOpenHelper {
	
	public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    
	    // create AppParts tables (also creates the database if it does not exist)
	    db.execSQL(CREATE_APP_PARTS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Database Created");
	    Log.i(DatabaseHelper.class.getName(), "Table " + AppPartDatabaseHelper.TABLE_NAME + " created.");
	    db.execSQL(CREATE_APP_PARTS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + AppPartDatabaseHelper.TEMP_TABLE_NAME + " created.");
	    
	    // create elements tables
	    db.execSQL(CREATE_ELEMENTS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementDatabaseHelper.TABLE_NAME + " created.");
	    db.execSQL(CREATE_ELEMENTS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementDatabaseHelper.TEMP_TABLE_NAME + " created.");
	    
	    // create element options tables
	    db.execSQL(CREATE_ELEMENT_OPTIONS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementOptionDatabaseHelper.TABLE_NAME + " created.");
	    db.execSQL(CREATE_ELEMENT_OPTIONS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementOptionDatabaseHelper.TEMP_TABLE_NAME + " created.");
	    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(AppPartDatabaseHelper.class.getName(),
		    "Upgrading database from version " + oldVersion + " to "
		    + newVersion + ", which will destroy all old data");
	    
	    // delete app parts tables 
	    db.execSQL(DELETE_APP_PARTS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + AppPartDatabaseHelper.TABLE_NAME + " deleted.");
	    db.execSQL(DELETE_APP_PARTS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + AppPartDatabaseHelper.TEMP_TABLE_NAME + " deleted.");
	    
	    // delete elements tables
	    db.execSQL(DELETE_ELEMENTS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementDatabaseHelper.TABLE_NAME + " deleted.");
	    db.execSQL(DELETE_ELEMENTS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementDatabaseHelper.TEMP_TABLE_NAME + " deleted.");
	    
	    // delete element options tables
	    db.execSQL(DELETE_ELEMENT_OPTIONS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementOptionDatabaseHelper.TABLE_NAME + " deleted.");
	    db.execSQL(DELETE_ELEMENT_OPTIONS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Table " + ElementOptionDatabaseHelper.TEMP_TABLE_NAME + " deleted.");	    
	    
	    // recreate tables
	    onCreate(db);	    
	}	
    }
}