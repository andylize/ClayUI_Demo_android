package com.netinfocentral.ClayUI;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppPart {
    
    // define instance variables
    private long appPartID;
    private String appPartName;
    private int version;
    private List<Element> elements;
    
        
    // main constructor
    public AppPart(long recordID, String appPartName, int version) {
	this.appPartID = recordID;
	this.appPartName = appPartName;
	this.version = version;
    }

    /**
     * @return the recordID
     */
    public long getRecordID() {
        return appPartID;
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
	if (elements == null){
	    return 0;
	}
	else {
	    return this.elements.size();
	}
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
    
    /**
     * Method to refresh layout
     */
    public void refreshLayout(LinearLayout layout, Context context) {
	
	// loop through elements and push to layout
	if (this.elementCount() > 0) {
	    Iterator<Element> iterator = this.elements.iterator();
	    
	    while (iterator.hasNext()) {
		Element element = (Element)iterator.next();
		
		// determine element type and call appropriate method
		switch (element.getElementType()) {
		    case ElementType.CLAYUI_TEXTBOX:
			layout.addView(this.createLabelUIElement(element, context));
			layout.addView(this.createTextBoxUIElement(element, context));
			break;
		    case ElementType.CLAYUI_LABEL:
			layout.addView(this.createLabelUIElement(element, context));
			break;
		    case ElementType.CLAYUI_COMBOBOX: //TODO
			layout.addView(this.createLabelUIElement(element, context));
			break;
		    case ElementType.CLAYUI_RADIOBUTTON: //TODO
			layout.addView(this.createLabelUIElement(element, context));
			break;
		    case ElementType.CLAYUI_CHECKBOX: //TODO
			layout.addView(this.createLabelUIElement(element, context));
			break;
		}		
	    }
	}
	else {
	    Log.e(AppPart.class.getName(), "Attempt to refresh layout with 0 elements. Did you forget to fetch elements?");
	}
    }
    
    /** 
     * Method to fetch all elements for this app part
     */
    public void fetchElements(Context context) {
	ElementDataAdapter adapter = new ElementDataAdapter(context);
	adapter.open();
	this.elements = adapter.getAllElements(this.appPartID);
	adapter.close();
    }
    
    
    @Override
    public String toString() {
    	return "AppPart [recordID="+ this.appPartID + ", appPartName=" + this.appPartName + ", version="+ this.version + "]";
    }
    
    /**
     * METHODS TO generate appropriate form elements
     */
    
    /**
     * Method to create a text box with a label
     */
    private EditText createTextBoxUIElement(Element element, Context context) {
	EditText eText = new EditText(context);
	return eText;
    }
    
    /**
     * Method to create a label
     * 
     */
    private TextView createLabelUIElement(Element element, Context context) {
	TextView tview = new TextView(context);
	tview.setText(element.getElementLabel());
	return tview;
    }
}
