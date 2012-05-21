package com.netinfocentral.ClayUI;

// class to hold data table schema info
public class DataTableSchema {
    
    // define member variables
    private String columnName;
    private String dataType;
    private int length;
    private boolean isPrimaryKey;
    
    // define default constructor
    public DataTableSchema(String columnName, String dataType, int length, int isPrimaryKey) {
	this.columnName = columnName;
	this.dataType = dataType;
	this.length = length;
	if (Math.abs(isPrimaryKey)==1) {
	    this.isPrimaryKey = true;
	}
	else {
	    this.isPrimaryKey = false;
	}
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @return the isPrimaryKey
     */
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
    
    @Override
    public String toString() {
	return "DataTableSchema [columnName=" + this.columnName + ", dataType=" + this.dataType + ", length=" + this.length + ", isPrimaryKey=" + this.isPrimaryKey + "]";
    }
}
