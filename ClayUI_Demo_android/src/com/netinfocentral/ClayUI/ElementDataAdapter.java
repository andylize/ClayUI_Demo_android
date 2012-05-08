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

public class ElementDataAdapter {
 // define class variables
    private SQLiteDatabase db;
    private ElementDatabaseHelper dbHelper;
    private String[] columns = { ElementDatabaseHelper.COLUMN_ID,
	    ElementDatabaseHelper.COLUMN_APP_PART_ID,
	    ElementDatabaseHelper.COLUMN_ELEMENT_NAME,
	    ElementDatabaseHelper.COLUMN_ELEMENT_TYPE,
	    ElementDatabaseHelper.COLUMN_ELEMENT_LABEL,
	    ElementDatabaseHelper.COLUMN_VERSION};
    
    //default constructor
    public ElementDataAdapter(Context context) { 
	dbHelper = new ElementDatabaseHelper(context);
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
	    int result = db.delete(ElementDatabaseHelper.TABLE_NAME, null, null);
	}
	catch (SQLException e) {
	    Log.e(ElementDatabaseHelper.class.getName(), e.getMessage());
	}
	catch (Exception e) {
	    Log.e(ElementDatabaseHelper.class.getName(), e.getMessage());
	}
    }
    
    /** method to add an element record to database
     **
     ** Returns the record id of the new record
     **/
    public Element createElement(long elementID, int appPartID, String elementName, int elementType, String elementLabel, int version) {
	ContentValues values = new ContentValues();
	values.put(ElementDatabaseHelper.COLUMN_ID, elementID);
	values.put(ElementDatabaseHelper.COLUMN_APP_PART_ID, appPartID);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_NAME, elementName);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_TYPE, elementType);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_LABEL, elementLabel);
	values.put(ElementDatabaseHelper.COLUMN_VERSION, version);
	long insertID = db.insert(ElementDatabaseHelper.TABLE_NAME, null, values);
	
	// query the database to get inserted record and return to calling method
	Cursor cursor = db.query(ElementDatabaseHelper.TABLE_NAME, columns, ElementDatabaseHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
	cursor.moveToFirst();
	
	return cursorToElement(cursor);
	
    }
    
    /** method to update an existing element
     * 
     * @return record id of the current updated record
     */
    public Element updateElement(long elementID, int appPartID, String elementName, int elementType, String elementLabel, int version) {
	ContentValues values = new ContentValues();
	values.put(ElementDatabaseHelper.COLUMN_ID, elementID);
	values.put(ElementDatabaseHelper.COLUMN_APP_PART_ID, appPartID);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_NAME, elementName);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_TYPE, elementType);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_LABEL, elementLabel);
	values.put(ElementDatabaseHelper.COLUMN_VERSION, version);
	long updateID = db.update(ElementDatabaseHelper.TABLE_NAME, values, ElementDatabaseHelper.COLUMN_ID + " = " + elementID, null);
	
	// query the database to get inserted record and return to calling method
	Cursor cursor = db.query(ElementDatabaseHelper.TABLE_NAME, columns, ElementDatabaseHelper.COLUMN_ID + " = " + updateID, null, null, null, null);
	cursor.moveToFirst();
	
	return cursorToElement(cursor);
    }
    
    /** method to delete an existing element
     * 
     * @return void
     */
    public void deleteElement(long elementID) {
	db.delete(ElementDatabaseHelper.TABLE_NAME, ElementDatabaseHelper.COLUMN_ID + " = " + elementID, null);
    }
    
    // method to return a list array of AppPart objects
    public List<Element> getAllElements() {
	List<Element> elements = new ArrayList<Element>();
	Cursor cursor = db.query(ElementDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
	
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    Element element = cursorToElement(cursor);
	    elements.add(element);
	    cursor.moveToNext();
	}
	//close cursor
	cursor.close();
	return elements;
    }
    
    // method to convert a record to a AppPart object
    private Element cursorToElement(Cursor cursor)
    {
	Element element = new Element(cursor.getLong(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5));
	return element;
    }
    
    /** Methods to handle syncing TEMP tables with actual elements tables
     * 
     */
    // method to load temp table from array from web service
    public void syncWithTempTable(List<Element> elements) {
	// clear temp table
	this.deleteTempElements();
	
	// create iterator for elements
	Iterator<Element> iterator = elements.iterator();
	
	// loop through iterator and add elements to temp table
	while (iterator.hasNext()) {
	    Element element = (Element)iterator.next();
	    this.createTempElement(element.getRecordID(), element.getAppPartID(), element.getElementName(), element.getElementType(), element.getElementLabel(), element.getVersion());
	}
	// TODO this.sync();
    }
    // method to add elements to temp table
    private void createTempElement(long elementID, int appPartID, String elementName, int elementType, String elementLabel, int version) {
	ContentValues values = new ContentValues();
	values.put(ElementDatabaseHelper.COLUMN_ID, elementID);
	values.put(ElementDatabaseHelper.COLUMN_APP_PART_ID, appPartID);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_NAME, elementName);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_TYPE, elementType);
	values.put(ElementDatabaseHelper.COLUMN_ELEMENT_LABEL, elementLabel);
	values.put(ElementDatabaseHelper.COLUMN_VERSION, version);
	db.insert(ElementDatabaseHelper.TEMP_TABLE_NAME, null, values);
    }
    // method to delete all elements from temp table
    private void deleteTempElements() {
	db.delete(ElementDatabaseHelper.TEMP_TABLE_NAME, null, null);
    }
    
    // method to sync temp tables with elements table
    private void sync() {
	// delete records in elements table which are not in temp table
	String sql =
		"DELETE FROM " + ElementDatabaseHelper.TABLE_NAME + " " + 
		"WHERE " + ElementDatabaseHelper.COLUMN_ID + " NOT IN (SELECT " + ElementDatabaseHelper.COLUMN_ID + " FROM " + ElementDatabaseHelper.TEMP_TABLE_NAME + ");";
	db.execSQL(sql);
	
	// TODO update recrods that exist in both tables
	
	// TODO insert records that do not exist in elements table
//	sql = "INSERT INTO " + ElementDatabaseHelper.TABLE_NAME + " " +
//	      "SELECT "
	
    }
}
