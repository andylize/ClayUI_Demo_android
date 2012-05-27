package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

public class DataTableRecord {
    
    // define member variables
    private List<String> columns;
    private List<String> values;
    
    // default constructor
    public DataTableRecord(List<String> columns, List<String> values) {
	this.columns = columns;
	this.values = values;
    }

    /**
     * @return the columns
     */
    public List<String> getColumns() {
        return columns;
    }
    
    /**
     * 
     * @return CSV string of columns
     */
    public String getColumnsCSV() {
	return this.getCSV(this.columns);
    }

    /**
     * @return the values
     */
    public List<String> getValues() {
        return values;
    }
    
    /**
     * 
     * @return CSV string of values
     */
    public String getValuesCSV() {
	return this.getCSV(this.values);
    }
    
    /**
     * Generic method to convert a List array of Strings to a CSV
     * 
     * @param value
     * @return CSV string
     */
    private String getCSV(List<String> value) {
	Iterator <String> iterator = value.iterator();
	
	String retval = "";
	
	while (iterator.hasNext()) {
	    retval = retval + (String)iterator.next() + ", ";
	}
	// trim trailing ", " and return
	if (retval.length() > 0) {
	    retval = retval.substring(0, retval.length() -2);
	}
	return retval;
    }
    
    
}
