package com.netinfocentral.ClayUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class AppPart {
    
    // define member variables
    private int appPartID;
    private String appPartName;
    private int version;
    private List<Element> elements;
    
        
    // main constructor
    public AppPart(int appPartID, String appPartName, int version) {
	this.appPartID = appPartID;
	this.appPartName = appPartName;
	this.version = version;
    }

    /**
     * @return the recordID
     */
    public int getAppPartID() {
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
	
	int viewID = 0; // variable to hold the ID for the element on the form
	
	// clear existing layout items
	layout.removeAllViews();
	
	// loop through elements and push to layout
	if (this.elementCount() > 0) {
	    Iterator<Element> iterator = this.elements.iterator();
	    
	    while (iterator.hasNext()) {
		Element element = (Element)iterator.next();
		
		// determine element type and call appropriate method
		switch (element.getElementType()) {
		    case ElementType.CLAYUI_TEXTBOX:
			layout.addView(this.createLabelUIElement(element, context));
			layout.addView(this.createTextBoxUIElement(element, context, element.getElementID()));
			break;
		    case ElementType.CLAYUI_LABEL:
			layout.addView(this.createLabelUIElement(element, context, element.getElementID()));
			break;
		    case ElementType.CLAYUI_COMBOBOX:
			layout.addView(this.createLabelUIElement(element, context));
			layout.addView(this.createComboBoxUIElement(element, context, element.getElementID()));
			break;
		    case ElementType.CLAYUI_RADIOBUTTON:
			layout.addView(this.createLabelUIElement(element, context));
			layout.addView(this.createRadioGroup(element, context, element.getElementID()));
			break;
		    case ElementType.CLAYUI_CHECKBOX:
			layout.addView(this.createCheckBox(element, context, element.getElementID()));
			break;
		}
		viewID++;
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
     * Method to create a text box
     */
    private EditText createTextBoxUIElement(Element element, Context context, int viewID) {
	EditText eText = new EditText(context);
	eText.setId(viewID);
	return eText;
    }
    
    /**
     * Method to create a label with an ID
     * 
     */
    private TextView createLabelUIElement(Element element, Context context, int viewID) {
	TextView tview = new TextView(context);
	tview.setId(viewID);
	tview.setText(element.getElementLabel());
	return tview;
    }
    /**
     * Method to create a label without an ID
     * 
     */
    private TextView createLabelUIElement(Element element, Context context) {
	TextView tview = new TextView(context);
	tview.setText(element.getElementLabel());
	return tview;
    }
    
    /**
     * Method to create a spinner (combo box)
     * 
     */
    private Spinner createComboBoxUIElement(Element element, Context context, int viewID) {
	Spinner spinner = new Spinner(context);
	spinner.setId(viewID);
		
	// fetch element options
	if (element.hasOptions() == false) {
	    element.fetchElementOptions(context);
	}
	
	ArrayList<String> options = element.getElementOptions();
	
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);
	spinner.setAdapter(adapter);
	
	return spinner;
	
    }
    
    /**
     * Method to create a radio button group with radio buttons
     */
    private RadioGroup createRadioGroup(Element element, Context context, int viewID) {
	RadioGroup group = new RadioGroup(context);
	group.setId(viewID);
	
	group.setOrientation(RadioGroup.HORIZONTAL);
	
	// fetch element options for each radio button
	if (element.hasOptions() == false) {
	    element.fetchElementOptions(context);
	}
	
	// set iterator from element options
	Iterator<String> iterator = element.getElementOptions().iterator();
	
	int rbID = 0;
	
	while(iterator.hasNext()) {
	    RadioButton rb = new RadioButton(context);
	    rb.setText((String)iterator.next());
	    rb.setId(rbID);
	    group.addView(rb);
	    rbID++;
	}
	
	return group;
	
    }
    
    /**
     * Method to create a checkbox 
     */
    private CheckBox createCheckBox(Element element, Context context, int viewID) {
	CheckBox checkbox = new CheckBox(context);
	checkbox.setId(viewID);
	checkbox.setText(element.getElementLabel());
	return checkbox;
	
    }
}
