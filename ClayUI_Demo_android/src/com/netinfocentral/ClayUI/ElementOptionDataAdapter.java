package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.webkit.WebChromeClient.CustomViewCallback;

// class to handle data retrieval of element options
public class ElementOptionDataAdapter {
    // define class variables
    private SQLiteDatabase db;
    private ElementOptionDatabaseHelper dbHelper;
    private String[] columns = {ElementOptionDatabaseHelper.COLUMN_ID,
	    ElementOptionDatabaseHelper.COLUMN_APP_PART_ID,
	    ElementOptionDatabaseHelper.COLUMN_ELEMENT_ID,
	    ElementOptionDatabaseHelper.COLUMN_VALUE,
	    ElementOptionDatabaseHelper.COLUMN_DESCRIPTION,
	    ElementOptionDatabaseHelper.COLUMN_VERSION};
    
    // default constructor
    public ElementOptionDataAdapter(Context context) {
	this.dbHelper = new ElementOptionDatabaseHelper(context);
    }
    
    // method to open the database
    public void open() throws SQLException {
	db = dbHelper.getWritableDatabase();
    }
    
    // method to close the database
    public void cloase() {
	dbHelper.close();
    }
    
    // method to clear the database
    public void clearDatabase() {
	try {
	    int result = db.delete(ElementOptionDatabaseHelper.TABLE_NAME, null, null);
	    result = db.delete(ElementOptionDatabaseHelper.TEMP_TABLE_NAME, null, null);
	}
	catch (SQLException e) {
	    Log.e(ElementOptionDatabaseHelper.class.getName(), e.getMessage());
	}
	catch(Exception e) {
	    Log.e(ElementOptionDatabaseHelper.class.getName(), e.getMessage());
	}
    }
    
    /** method to add an element option record to database
     * 
     * Returns the element option that was created in database
     */
    public ElementOption createElementOption(long elementOptionID, int appPartID, int elementID, String value, String description, int version) {
	ContentValues values = new ContentValues();
	values.put(ElementOptionDatabaseHelper.COLUMN_ID, elementOptionID);
	values.put(ElementOptionDatabaseHelper.COLUMN_APP_PART_ID, appPartID);
	values.put(ElementOptionDatabaseHelper.COLUMN_ELEMENT_ID, elementID);
	values.put(ElementOptionDatabaseHelper.COLUMN_VALUE, value);
	values.put(ElementOptionDatabaseHelper.COLUMN_DESCRIPTION, description);
	values.put(ElementOptionDatabaseHelper.COLUMN_VERSION, version);
	long insertID = db.insert(ElementOptionDatabaseHelper.TABLE_NAME, null, values);
	
	// query the database to get the inserted record and return object to method
	Cursor cursor = db.query(ElementOptionDatabaseHelper.TABLE_NAME, columns, ElementOptionDatabaseHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
	cursor.moveToFirst();
	
	return cursorToElementOption(cursor);
    }
    
    /** method to update an existing element option
     * 
     * returns the element option object that was updated
     */
    public ElementOption updateElementOption(long elementOptionID, int appPartID, int elementID, String value, String description, int version) {
	ContentValues values = new ContentValues();
	values.put(ElementOptionDatabaseHelper.COLUMN_ID, elementOptionID);
	values.put(ElementOptionDatabaseHelper.COLUMN_APP_PART_ID, appPartID);
	values.put(ElementOptionDatabaseHelper.COLUMN_ELEMENT_ID, elementID);
	values.put(ElementOptionDatabaseHelper.COLUMN_VALUE, value);
	values.put(ElementOptionDatabaseHelper.COLUMN_DESCRIPTION, description);
	values.put(ElementOptionDatabaseHelper.COLUMN_VERSION, version);
	long updateID = db.update(ElementOptionDatabaseHelper.TABLE_NAME, values, ElementOptionDatabaseHelper.COLUMN_ID + " = " + elementOptionID, null);
	
	// query the database to get the inserted record and return object to method
	Cursor cursor = db.query(ElementOptionDatabaseHelper.TABLE_NAME, columns, ElementOptionDatabaseHelper.COLUMN_ID + " = " + updateID, null, null, null, null);
	cursor.moveToFirst();
	
	return cursorToElementOption(cursor);
    }
    
    /** method to delete an existing element option
     * 
     * returns void
     */
    public void deleteElementOption(long elementOptionID) {
	db.delete(ElementOptionDatabaseHelper.TABLE_NAME, ElementOptionDatabaseHelper.COLUMN_ID + " = " + elementOptionID, null);
    }
    
    /** Method to return a list array of element option objects
     * 
     *  returns list array of element option objects
     */
    public List<ElementOption> getAllElementOptions() {
	List<ElementOption> elementOptions = new ArrayList<ElementOption>();
	Cursor cursor = db.query(ElementOptionDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
	
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    ElementOption option = cursorToElementOption(cursor);
	    elementOptions.add(option);
	    cursor.moveToNext();
	}
	//close cursor
	cursor.close();
	return elementOptions;
    }  
    
    
    /** method to return an element option object based on a cursor
     * 
     * Returns element option object
     */
    private ElementOption cursorToElementOption(Cursor cursor) {
	ElementOption option = new ElementOption(cursor.getLong(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
	return option;
    }
    
    /** TODO **/
    
    /** Methods to handle syncing TEMP tables with actual elements tables
     * 
     */
}
