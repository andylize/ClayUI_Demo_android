package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.widget.ArrayAdapter;

public class Element {
    // define instance variables
    private int elementID;
    private int appPartID;
    private String elementName;
    private int elementType;
    private String elementLabel;
    private int listOrder;
    private int version;
    private ArrayList<String> elementOptions;
           
    // default constructor
    public Element(int elementID, int appPartID, String elementName, int elementType, String elementLabel, int listOrder, int version) {
	this.elementID = elementID;
	this.appPartID = appPartID;
	this.elementName = elementName;
	this.elementType = elementType;
	this.elementLabel = elementLabel;
	this.listOrder = listOrder;
	this.version = version;
	this.elementOptions = new ArrayList<String>();
    }
    
    /**
     * @return the recordID
     */
    public int getElementID() {
        return elementID;
    }
    
    /**
     * @return the appPartID
     */
    public int getAppPartID() {
        return appPartID;
    }
    
    /**
     * @return the elementName
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * @return the elementType
     */
    public int getElementType() {
        return elementType;
    }

    /**
     * @return the elementLabel
     */
    public String getElementLabel() {
        return elementLabel;
    }
    
    /** 
     * @return the listOrder
     */
    public int getListOrder() {
	return listOrder;
    }
    
    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    
    /**
     *  @return the array list of element options
     */
    public ArrayList<String> getElementOptions() {
	if (this.elementOptions == null || this.elementOptions.isEmpty()) {
	    elementOptions.add("Empty Option");
	    return this.elementOptions;
	}
	else {
	    return this.elementOptions;
	}
    }
    
    /** method to check if the element options have been fetched
     * 
     * @return true if the options are populated
     */
    public Boolean hasOptions() {
	if (this.elementOptions == null || this.elementOptions.isEmpty()) {
	    return false;
	}
	else {
	    return true;
	}
    }
    
    /** Method to get all element options for element
     * 
     * @param context
     */
    public void fetchElementOptions(Context context) {
	
	// clear arraylist if it is not empty
	if (this.elementOptions != null && this.elementOptions.isEmpty() == false) { this.elementOptions.clear(); }
	
	ElementOptionDataAdapter adapter = new ElementOptionDataAdapter(context);
	adapter.open();
	List<ElementOption> options = adapter.getAllElementOptions(this.elementID);
	
	Iterator<ElementOption> iterator = options.iterator();
	
	while (iterator.hasNext()) {
	    ElementOption option = (ElementOption)iterator.next();
	    elementOptions.add(option.getDescription());
	}	
	adapter.close();
    }

    @Override
    public String toString() {
	return "Element [recordID="+ this.elementID + ", appPartID=" + this.appPartID + ", elementName=" + this.elementName +
		 ", elementType=" + this.elementType + ", elementLabel=" + this.elementLabel + ", listOrder=" + this.listOrder + ", version="+ this.version + "]";
    }
}
