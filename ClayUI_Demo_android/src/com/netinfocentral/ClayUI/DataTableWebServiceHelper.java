package com.netinfocentral.ClayUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BufferedHeader;
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
    private final static String POST_URI = "services/PutTableData.php?AppID=";
    private String postUri = "";
    private Context context;
    
    // define default method
    public DataTableWebServiceHelper(int applicationID, int appPartID, String uri, Context context) {
	super(applicationID, uri + DataTableWebServiceHelper.SERVICE_URI);
	this.appPartID = appPartID;
	this.uri = this.uri + DataTableWebServiceHelper.SERVICE_URI_2 + this.appPartID;
	this.postUri = uri + DataTableWebServiceHelper.POST_URI + this.applicationID + DataTableWebServiceHelper.SERVICE_URI_2 + this.appPartID;
	
	this.context = context;
    }
    
    // return arraylist of a data table schema form JSON delivered from ClayIO Web Service
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
    
    // generic post method
    public int sendTableData(List<String> columns, List<String> values) {
	
	// return value
	int retval = 0;
	
	// create string from columns list array
	String columnCSV = "'";
	Iterator<String> columnIterator = columns.iterator();
	while (columnIterator.hasNext()) {
	    columnCSV = columnCSV + (String)columnIterator.next() + ", ";
	}
	// trim trailing ", "
	columnCSV = columnCSV.substring(0, columnCSV.length() -2) + "'";
	
	// create string from values list array
	String valuesCSV = "'";
	Iterator<String> valueIterator = values.iterator();
	while (valueIterator.hasNext()) {
	    valuesCSV = valuesCSV + "''" + (String)valueIterator.next() + "'', ";
	}
	// trim trailing "', "
	valuesCSV = valuesCSV.substring(0, valuesCSV.length() -2) + "'";
	
	// push values to JSON and post to web service
	try {
	    HttpClient client = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(this.postUri);
	    JSONObject jObject = new JSONObject();
	    jObject.put("appID", String.valueOf(this.applicationID));
	    jObject.put("appPartID", String.valueOf(this.appPartID));
	    jObject.put("columnsCSV", columnCSV);
	    jObject.put("valuesCSV", valuesCSV);
	    JSONArray jArray = new JSONArray();
	    jArray.put(jObject);
	    
	    // post data
	    httppost.setHeader("json", jObject.toString());
	    httppost.getParams().setParameter("jsonpost", jArray);
	    HttpResponse response = client.execute(httppost);
	    
	    if (response != null) {
		InputStream is = response.getEntity().getContent();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
		    while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		    }
		}
		catch(IOException e) {
		    Log.e(DataTableWebServiceHelper.class.getName(), e.getMessage());
		    retval = 1;
		}
		catch(Exception e) {
		    Log.e(DataTableWebServiceHelper.class.getName(), e.getMessage());
		    retval = 1;
		}
		finally {
		    try {
			is.close();
		    }
		    catch (IOException e) {
			Log.e(DataTableWebServiceHelper.class.getName(), e.getMessage());
			retval = 1;
		    }
		    catch(Exception e) {
			Log.e(DataTableWebServiceHelper.class.getName(), e.getMessage());
			retval = 1;
		    }
		}
		Log.i(DataTableWebServiceHelper.class.getName(), sb.toString());		
	    }
	    
	    retval = 0;
	}
	catch (Throwable t) {
	    Log.e(DataTableWebServiceHelper.class.getName(), t.getMessage());
	    retval = 1;
	}
	return retval;
    }
}


