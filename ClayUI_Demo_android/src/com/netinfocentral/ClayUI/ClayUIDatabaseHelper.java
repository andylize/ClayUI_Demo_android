package com.netinfocentral.ClayUI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClayUIDatabaseHelper {

    // define database values
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClayUI.db";
    
    // define AppPart create string
    private static final String CREATE_APP_PARTS_TABLE = AppPartDatabaseHelper.TABLE_CREATE;
    private static final String CREATE_APP_PARTS_TEMP_TABLE = AppPartDatabaseHelper.TEMP_TABLE_CREATE;
    private static final String DELETE_APP_PARTS_TABLE = AppPartDatabaseHelper.TABLE_DELETE;
    private static final String DELETE_APP_PARTS_TEMP_TABLE = AppPartDatabaseHelper.TEMP_TABLE_DELETE;
    
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
	    
	    // create AppParts table
	    db.execSQL(CREATE_APP_PARTS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "Database Created");
	    Log.i(DatabaseHelper.class.getName(), "AppParts Table Created");
	    db.execSQL(CREATE_APP_PARTS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "AppParts TEMP Table Created");
	    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(AppPartDatabaseHelper.class.getName(),
		    "Upgrading database from version " + oldVersion + " to "
		    + newVersion + ", which will destroy all old data");

	    db.execSQL(DELETE_APP_PARTS_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "AppParts Table Deleted");
	    db.execSQL(DELETE_APP_PARTS_TEMP_TABLE);
	    Log.i(DatabaseHelper.class.getName(), "AppParts TEMP Table Deleted");
	    onCreate(db);	    
	}	
    }
}