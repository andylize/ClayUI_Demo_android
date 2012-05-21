package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

// web service helper for retrieving data table 
public class DataTableWebServiceHelper extends ClayUIWebServiceHelper {
    
    // define member variables
    private int appPartID;
    private final static String SERVICE_URI = "services/GetDataTableSchema.php?AppID=";
    private final static String SERVICE_URI_2 = "&AppPartID=";
    private Context context;
    
    // define default method
    public DataTableWebServiceHelper(int applicationID, int appPartID, String uri, Context context) {
	super(applicationID, uri + DataTableWebServiceHelper.SERVICE_URI);
	this.uri = this.uri + DataTableWebServiceHelper.SERVICE_URI_2 + appPartID;
	this.context = context;
    }
    
    // return arraylist of a data table schem form JSON delivered from ClayIO Web Service
    public List<DataTableSchema> getTableSchema() {
	List<DataTableSchema> tableSchema = new ArrayList<DataTableSchema>();
	
	try {
	    JSONArray array = new JSONArray(getWebServiceData(this.uri));
	    Log.i(DataTableWebServiceHelper.class.getName(), array.length() + " records retrieved.");
	    
	    // loop through array, pull JSON objects out and push the values into object
	    for (int i=0; i <array.length(); i++) {
		JSONObject jObject = array.getJSONObject(i);
		DataTableSchema schema = new DataTableSchema(jObject.getString("column_name"), 
			jObject.getString("data_type"), 
			jObject.getInt("length"), 
			jObject.getInt("is_primary_key"));
		
		// add schema to arraylist
		tableSchema.add(schema);
		Log.i(DataTableWebServiceHelper.class.getName(), "Added : " + schema.getColumnName());
	    }
	}
	catch (Exception e) {
	    Log.e(DataTableWebServiceHelper.class.getName(), e.getMessage());
	}
	return tableSchema;
    }
}


