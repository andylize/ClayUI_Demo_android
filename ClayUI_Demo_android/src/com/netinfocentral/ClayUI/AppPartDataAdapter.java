package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AppPartDataAdapter {
    
    // define class variables
    private SQLiteDatabase db;
    private ClayUIDatabaseHelper dbHelper;
    private String[] columns = { AppPartDatabaseHelper.COLUMN_ID,
	    AppPartDatabaseHelper.COLUMN_APP_PART_NAME,
	    AppPartDatabaseHelper.COLUMN_VERSION};
    private Context context;
    private int appID;
    private String baseURI;
    
    //default constructor
    public AppPartDataAdapter(Context context, int appID, String baseURI) { 
	this.context = context;
	this.appID = appID;
	this.baseURI = baseURI;
    	dbHelper = new ClayUIDatabaseHelper(this.context, this.appID, this.baseURI);
    }
    // alternate constructor if db is already open
    public AppPartDataAdapter(Context context, int appID, String baseURI, SQLiteDatabase db) {
	this(context, appID, baseURI);
	this.db = db;
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
    	    result = db.delete(AppPartDatabaseHelper.TEMP_TABLE_NAME, null, null);
    	}
    	catch (SQLException e) {
    	    Log.e(AppPartDatabaseHelper.class.getName(), e.getMessage());
    	}
    	catch (Exception e) {
    	    Log.e(AppPartDatabaseHelper.class.getName(), e.getMessage());
    	}
    }
    
    /** method to add an app part record to database
     **
     ** Returns the app part that was created in the database
     **/
    public AppPart createAppPart(int appPartID, String appPartName, int version) {
	ContentValues values = new ContentValues();
	values.put(AppPartDatabaseHelper.COLUMN_ID, appPartID);
	values.put(AppPartDatabaseHelper.COLUMN_APP_PART_NAME, appPartName);
	values.put(AppPartDatabaseHelper.COLUMN_VERSION, version);
	long insertID = db.insert(AppPartDatabaseHelper.TABLE_NAME, null, values);

	// query the database to get inserted record and return to calling method
	Cursor cursor = db.query(AppPartDatabaseHelper.TABLE_NAME, columns, AppPartDatabaseHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
	cursor.moveToFirst();
	
	AppPart appPart = cursorToAppPart(cursor);
	
	cursor.close();
	
	return appPart;

    }
    
    /** method to update an existing app part
     * 
     * Returns the app part object that was updated
     *      
     **/
    public AppPart updateAppPart(int appPartID, String appPartName, int version) {
	ContentValues values = new ContentValues();
	values.put(AppPartDatabaseHelper.COLUMN_ID, appPartID);
	values.put(AppPartDatabaseHelper.COLUMN_APP_PART_NAME, appPartName);
	values.put(AppPartDatabaseHelper.COLUMN_VERSION, version);
	long updateID = db.update(AppPartDatabaseHelper.TABLE_NAME, values, AppPartDatabaseHelper.COLUMN_ID + " = " + appPartID, null);

	// query the database to get inserted record and return to calling method
	Cursor cursor = db.query(AppPartDatabaseHelper.TABLE_NAME, columns, AppPartDatabaseHelper.COLUMN_ID + " = " + updateID, null, null, null, null);
	cursor.moveToFirst();
	
	AppPart appPart = cursorToAppPart(cursor);
	
	cursor.close();
	
	return appPart;
    }
    
    /** method to delete an existing app part
     * 
     * returns null
     */
    public void deleteAppPart(int appPartID) {
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
    
    // method to return an app part object
    public AppPart getAppPart(int appPartID) {
	Cursor cursor = db.query(AppPartDatabaseHelper.TABLE_NAME, columns, AppPartDatabaseHelper.COLUMN_ID + " = " + appPartID, null, null, null, null);
	
	cursor.moveToFirst();
	
	AppPart appPart = cursorToAppPart(cursor);
	
	cursor.close();
	
	return appPart;
	
    }
    
    // method to return if app part has existing data table
    public boolean dataTableExists(String appPartName) {
	Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + appPartName +"'", null);
	
	if (cursor.getCount() < 1) {
	    return false;
	}
	else {
	    return true;
	}
    }
    
    // method to convert a record to a AppPart object
    private AppPart cursorToAppPart(Cursor cursor)	{
	AppPart appPart = new AppPart(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
	return appPart;
    }
    
    /** Methods to handle syncing TEMP tables with actual App Part tables
     * 
     * 
     */
    // method to load temp table from array from web service
    public void syncWithTempTable(List<AppPart> appParts) {
	// clear temp table
	this.deleteTempAppParts();
	
	// create iterator for app parts
	Iterator<AppPart> iterator = appParts.iterator();
	
	// loop through iterator and add app parts to temp table 
	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();	    
	    this.createTempAppPart(appPart.getAppPartID(), appPart.getAppPartName(), appPart.getVersion());
	}
	
	this.sync();
	
	// loop trough iterator again and add update data tables
	iterator = appParts.iterator();
	
	while (iterator.hasNext()) {
	    AppPart appPart = (AppPart)iterator.next();

	    // check if our data table exists.  If not create
	    if (this.dataTableExists(appPart.getAppPartName()) == false) {
		DataTableDatabaseHelper dtHelper = new DataTableDatabaseHelper(this.appID, appPart.getAppPartID(), this.baseURI, this.context);
		db.execSQL(dtHelper.getTableCreate());
		Log.i(AppPartDataAdapter.class.getName(), "Table " + appPart.getAppPartName() + " created.");
	    }
	}
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
	
	Log.i(AppPartDataAdapter.class.getName(), "AppParts table successfully synced.");
    }
    
    /**
     * Methods for saving/retrieving app part data to/from respective data tables
     */

    
    /**
     * method to save the app part data to its respective data table
     * @param appPartName
     */    
    public void saveAppPartData(String appPartName, LinearLayout layout, Context context) {	
	ElementDataAdapter elementDataAdapter = new ElementDataAdapter(context);
	
	List<String> elementNames = new ArrayList<String>();	
	List<String> appPartValues = new ArrayList<String>();
	
	// loop through layout components and get values for items with storable data
	for (int i=0; i<layout.getChildCount(); i++) {
	    
	    // check the widget type
	    if (layout.getChildAt(i).getId() >= 0){
    	   	if (layout.getChildAt(i) instanceof EditText){
    	   	    EditText editText = (EditText) layout.findViewById(layout.getChildAt(i).getId());
    	   	    appPartValues.add(editText.getText().toString());
    	   	    elementDataAdapter.open();
    	   	    Element element = elementDataAdapter.getElement(layout.getChildAt(i).getId());
    	   	    elementDataAdapter.close();
    	   	    elementNames.add(element.getElementID() + "." + element.getElementName());
    	   	}else if (layout.getChildAt(i) instanceof CheckBox) {
    	   	    CheckBox checkbox = (CheckBox) layout.findViewById(layout.getChildAt(i).getId());
    	   	    if (checkbox.isChecked()) {
    	   		appPartValues.add("1");
    	   	    }else {
    	   		appPartValues.add("0");
    	   	    }
    	   	    elementDataAdapter.open();
    	   	    Element element = elementDataAdapter.getElement(layout.getChildAt(i).getId());
    	   	    elementDataAdapter.close();
    	   	    elementNames.add(element.getElementID() + "." + element.getElementName());
    	   	}else if (layout.getChildAt(i) instanceof TextView) {
    	   	    TextView textView = (TextView) layout.findViewById(layout.getChildAt(i).getId());
    	   	    appPartValues.add(textView.getText().toString());
    	   	    elementDataAdapter.open();
    	   	    Element element = elementDataAdapter.getElement(layout.getChildAt(i).getId());
    	   	    elementDataAdapter.close();
    	   	    elementNames.add(element.getElementID() + "." + element.getElementName());
    	   	}else if (layout.getChildAt(i) instanceof Spinner) {
    	   	    Spinner spinner = (Spinner) layout.findViewById(layout.getChildAt(i).getId());
    	   	    appPartValues.add(spinner.getSelectedItem().toString());
    	   	    elementDataAdapter.open();
    	   	    Element element = elementDataAdapter.getElement(layout.getChildAt(i).getId());
    	   	    elementDataAdapter.close();
    	   	    elementNames.add(element.getElementID() + "." + element.getElementName());
    	   	}else if (layout.getChildAt(i) instanceof RadioGroup) {
    	   	    RadioGroup group = (RadioGroup) layout.findViewById(layout.getChildAt(i).getId());
    	   	    appPartValues.add(Integer.toString(group.getCheckedRadioButtonId()));
    	   	    elementDataAdapter.open();
    	   	    Element element = elementDataAdapter.getElement(layout.getChildAt(i).getId());
    	   	    elementDataAdapter.close();
    	   	    elementNames.add(element.getElementID() + "." + element.getElementName());
    	   	}
	    }
	}
	
	// iterate through list arrays to build insert query
	ContentValues values = new ContentValues();
	// get fields (element names) first.
	Iterator<String> iterator = elementNames.iterator();
	int i = 0;
	while (iterator.hasNext()) {
	    String columnName = (String)iterator.next();
	    values.put("'" + columnName + "'", appPartValues.get(i));
	    i++;
	}
	long insertID = db.insert(appPartName, null, values);
	Log.i(AppPartDataAdapter.class.getName(), "AppPart (" + appPartName + ") record saved: " + insertID);
	Toast.makeText(context, appPartName + " data saved.", Toast.LENGTH_SHORT).show();	
    }
    
    /**
     * Method to return array list of DataTableRecords
     * @param appPartName
     * @param context
     * @return
     */
    public List<DataTableRecord> getAppPartData(String appPartName) {
	
	// array list to hold data table record objects
	List<DataTableRecord> dataTableRecords = new ArrayList<DataTableRecord>();
	
	// query data table to get values that have not been sent up to web
	Cursor cursor = db.query(appPartName, null, "sentToWeb = 0", null, null, null, null);
	
	cursor.moveToFirst();
	
	while (!cursor.isAfterLast()) {
	    List<String> columns = new ArrayList<String>();
	    List<String> values = new ArrayList<String>();
	    
	    // get values in record
	    for (int i = 0; i<cursor.getColumnCount(); i++) {
		if (cursor.getColumnName(i).equals("sentToWeb") == false && cursor.getColumnName(i).equals("_ID") == false) {
		    columns.add(cursor.getColumnName(i));
		    values.add(cursor.getString(i));
		}
	    }
	    DataTableRecord record = new DataTableRecord(columns, values);
	    dataTableRecords.add(record);
	    cursor.moveToNext();
	}
	cursor.close();
	return dataTableRecords;
    }
    
    public void updateDataTableSentToWebStatus(String appPartName) {
	String sql = "UPDATE " + appPartName + " SET sentToWeb = 1 WHERE sentToWeb = 0;";
	db.execSQL(sql);
    }
}
