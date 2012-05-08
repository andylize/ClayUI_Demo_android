package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

public class AppPart {
    
    // define instance variables
    private long recordID;
    private String appPartName;
    private int version;
    private List<Element> elements;
    
        
    // main constructor
    public AppPart(long recordID, String appPartName, int version) {
	this.recordID = recordID;
	this.appPartName = appPartName;
	this.version = version;
    }

    /**
     * @return the recordID
     */
    public long getRecordID() {
        return recordID;
    }

    /**
     * @return the appPartName
     */
    public String getAppPartName() {
        return appPartName;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * @return the number of elements in the app part
     */
    public int elementCount() {
	return this.elements.size();
    }    
    
    /**
     * Method to add an element
     */
    public void addElement(Element element) {
	this.elements.add(element);
    }
    
    /** 
     * Method to bulk add elements from listarray
     */
    public void addElements(List<Element> elements) {
	Iterator<Element> iterator = elements.iterator();
	
	while (iterator.hasNext()){
	    this.elements.add((Element)iterator.next());
	}
    }
    
    /**
     * Method to clear elements from list array
     */
    public void clearElements() {
	this.elements.clear();
    }
    
    /**
     * @return List object of type Element
     */
    public List<Element> getElements() {
	return elements;
    }
    
    
    @Override
    public String toString() {
    	return "AppPart [recordID="+ this.recordID + ", appPartName=" + this.appPartName + ", version="+ this.version + "]";
    }	
}
