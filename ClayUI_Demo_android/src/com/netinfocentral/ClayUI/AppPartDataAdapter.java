package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppPartDataAdapter {
    
    // define class variables
    private SQLiteDatabase db;
    private ClayUIDatabaseHelper dbHelper;
    private String[] columns = { AppPartDatabaseHelper.COLUMN_ID,
	    AppPartDatabaseHelper.COLUMN_APP_PART_NAME,
	    AppPartDatabaseHelper.COLUMN_VERSION};
    
    //default constructor
    public AppPartDataAdapter(Context context) { 
    	dbHelper = new ClayUIDatabaseHelper(context);
    }
    
    // method to open the database
    public void open() throws SQLException {
    	db = dbHelper.getWritableDatabase();
    }
    
    // method to close the database
    public void close() {
    	dbHelper.close();
    }
    
    // clear database
    public void clearDatabase() {
    	try {
    	    int result = db.delete(AppPartDatabaseHelper.TABLE_NAME, null, null);
    	}
    	catch (SQLException e) {
    	    Log.e(AppPartDatabaseHelper.class.getName(), e.getMessage());
    	}
    	catch (Exception e) {
    	    Log.e(AppPartDatabaseHelper.class.getName(), e.getMessage());
    	}
    }
    
    /** method to add a record to database
     **
     ** Returns the record id of the new record
     **/
    public AppPart createAppPart(long appPartID, String appPartName, int version) {
	ContentValues values = new ContentValues();
	values.put(AppPartDatabaseHelper.COLUMN_ID, appPartID);
	values.put(AppPartDatabaseHelper.COLUMN_APP_PART_NAME, appPartName);
	values.put(AppPartDatabaseHelper.COLUMN_VERSION, version);
	long insertID = db.insert(AppPartDatabaseHelper.TABLE_NAME, null, values);

	// query the database to get inserted record and return to calling method
	Cursor cursor = db.query(AppPartDatabaseHelper.TABLE_NAME, columns, AppPartDatabaseHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
	cursor.moveToFirst();

	return cursorToAppPart(cursor);

    }
    
    /** method to update an existing app part
     * 
     * Returns the record id of the current updated record 
     *      
     **/
    public AppPart updateAppPart(long appPartID, String appPartName, int version) {
	ContentValues values = new ContentValues();
	values.put(AppPartDatabaseHelper.COLUMN_ID, appPartID);
	values.put(AppPartDatabaseHelper.COLUMN_APP_PART_NAME, appPartName);
	values.put(AppPartDatabaseHelper.COLUMN_VERSION, version);
	long updateID = db.update(AppPartDatabaseHelper.TABLE_NAME, values, AppPartDatabaseHelper.COLUMN_ID + " = " + appPartID, null);

	// query the database to get inserted record and return to calling method
	Cursor cursor = db.query(AppPartDatabaseHelper.TABLE_NAME, columns, AppPartDatabaseHelper.COLUMN_ID + " = " + updateID, null, null, null, null);
	cursor.moveToFirst();

	return cursorToAppPart(cursor);
    }
    
    /** method to delete an existing app part
     * 
     * returns null
     */
    public void deleteAppPart(long appPartID) {
	db.delete(AppPartDatabaseHelper.TABLE_NAME, AppPartDatabaseHelper.COLUMN_ID + " = " + appPartID, null);
    }
    
    
    
    // method to return a list array of AppPart objects
    public List<AppPart> getAllAppParts() {
	List<AppPart> appParts = new ArrayList<AppPart>();
	Cursor cursor = db.query(AppPartDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

	cursor.moveToFirst();
	
	while (!cursor.isAfterLast()) {
	    AppPart appPart = cursorToAppPart(cursor);
	    appParts.add(appPart);
	    cursor.moveToNext();
	}
	
	//close cursor
	cursor.close();
	return appParts;
    }
    
    // method to convert a record to a AppPart object
    private AppPart cursorToAppPart(Cursor cursor)	{
	AppPart appPart = new AppPart(cursor.getLong(0), cursor.getString(1), cursor.getInt(2));
	return appPart;
    }
    
    /** Methods to handle syncing TEMP tables with actual App Part tables
     * 
     * 
     */
    // method to load temp table from array
    public void syncWithTempTable(List<AppPart> appParts) {
	// clear temp table
	this.deleteTempAppParts();
	
	// create iterator for app parts
	Iterator<AppPart> iterator = appParts.iterator();
	
	// loop through iterator and add update 
	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();
	    this.createTempAppPart(appPart.getRecordID(), appPart.getAppPartName(), appPart.getVersion());
	}
	
	this.sync();
    }
    // method to add app parts to temp table
    private void createTempAppPart(long appPartID, String appPartName, int version) {
	ContentValues values = new ContentValues();
	values.put(AppPartDatabaseHelper.COLUMN_ID, appPartID);
	values.put(AppPartDatabaseHelper.COLUMN_APP_PART_NAME, appPartName);
	values.put(AppPartDatabaseHelper.COLUMN_VERSION, version);
	db.insert(AppPartDatabaseHelper.TEMP_TABLE_NAME, null, values);
    }
    // method to delete all app parts from temp table
    private void deleteTempAppParts() {
	db.delete(AppPartDatabaseHelper.TEMP_TABLE_NAME, null, null);
    }
    
    // method to sync temp tables with app part table
    private void sync() {
	// delete records in app part table which are not in the temp table
	String sql = 
		"DELETE FROM " + AppPartDatabaseHelper.TABLE_NAME + " "+
		"WHERE " + AppPartDatabaseHelper.COLUMN_ID + " NOT IN (SELECT " + AppPartDatabaseHelper.COLUMN_ID + " FROM " + AppPartDatabaseHelper.TEMP_TABLE_NAME + ");";
	db.execSQL(sql);
	
	// update records that exist in both tables
	sql = "UPDATE " + AppPartDatabaseHelper.TABLE_NAME + " " +
	      "SET " + AppPartDatabaseHelper.COLUMN_APP_PART_NAME + " = (SELECT t." + AppPartDatabaseHelper.COLUMN_APP_PART_NAME + " FROM " + AppPartDatabaseHelper.TEMP_TABLE_NAME + " t " +
	      	"WHERE t." + AppPartDatabaseHelper.COLUMN_ID + " = " + AppPartDatabaseHelper.TABLE_NAME + "." + AppPartDatabaseHelper.COLUMN_ID + "), " + 
	      AppPartDatabaseHelper.COLUMN_VERSION + " = (SELECT t." + AppPartDatabaseHelper.COLUMN_VERSION + " FROM " + AppPartDatabaseHelper.TEMP_TABLE_NAME + " t " +
	      	"WHERE t." + AppPartDatabaseHelper.COLUMN_ID + " = " + AppPartDatabaseHelper.TABLE_NAME + "." + AppPartDatabaseHelper.COLUMN_ID + ") " +
	      "WHERE EXISTS (SELECT t." + AppPartDatabaseHelper.COLUMN_APP_PART_NAME + " FROM " + AppPartDatabaseHelper.TEMP_TABLE_NAME + " t " + 
	      "WHERE t." + AppPartDatabaseHelper.COLUMN_ID + " = " + AppPartDatabaseHelper.TABLE_NAME + "." + AppPartDatabaseHelper.COLUMN_ID + ");";
	db.execSQL(sql);
	
	// insert records that do not exist in app parts table
	sql = "INSERT INTO " + AppPartDatabaseHelper.TABLE_NAME + " " +
	      "SELECT " +
	      AppPartDatabaseHelper.COLUMN_ID + ", " +
	      AppPartDatabaseHelper.COLUMN_APP_PART_NAME + ", " +
	      AppPartDatabaseHelper.COLUMN_VERSION + " FROM " + AppPartDatabaseHelper.TEMP_TABLE_NAME + " t " +
	      "WHERE t." + AppPartDatabaseHelper.COLUMN_ID + " NOT IN " + 
	      "(SELECT " + AppPartDatabaseHelper.COLUMN_ID + " FROM " + AppPartDatabaseHelper.TABLE_NAME + ");";
	db.execSQL(sql);
	
	Log.i(AppPartDataAdapter.class.getName(), "AppParts Table Successfully synced.");
    }
	
}
