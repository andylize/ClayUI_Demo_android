package com.netinfocentral.ClayUI_Demo_android;

import java.util.Iterator;
import java.util.List;

import com.netinfocentral.ClayUI.AppPart;
import com.netinfocentral.ClayUI.ClayUIAppBase;
import android.app.Activity;
import android.app.ListActivity;
import android.app.TabActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ClayUI_Demo_androidActivity extends Activity {
    
    // define local variables
    ClayUIAppBase appBase;
    AppPart contactsAppPart;
    AppPart productsAppPart;
    LinearLayout contactsLayout;
    LinearLayout productsLayout;   
    
    
    static String BASE_URI_LOCAL = "http://192.168.102.80/ClayUI/";
    static String BASE_URI_INTERNET = "http://nicentral.dyndns.org:8888/ClayUI/";
    
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // TODO disable strict-mode and add threading
    	StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build(); 
		StrictMode.setThreadPolicy(policy);
        
        
        // instantiate layouts
        contactsLayout = (LinearLayout) findViewById(R.id.contactsLayout);
        productsLayout = (LinearLayout) findViewById(R.id.productsLayout);
        
        // instantiate ClayUI AppBase
        appBase = new ClayUIAppBase(1, getApplicationContext(), BASE_URI_LOCAL);
        
        // instantiate app parts
        contactsAppPart = appBase.getAppPart(1);
        productsAppPart = appBase.getAppPart(2);
        
        // fetch app part elements
        contactsAppPart.fetchElements(this);
        contactsAppPart.refreshLayout(contactsLayout, this);
        productsAppPart.fetchElements(this);
        productsAppPart.refreshLayout(productsLayout, this);
        
        
    }
        
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	menu.add(Menu.NONE, 1, Menu.NONE, R.string.syncSchema);
	menu.add(Menu.NONE, 2, Menu.NONE, R.string.syncData);
	menu.add(Menu.NONE, 3, Menu.NONE, R.string.saveContact);
	menu.add(Menu.NONE, 4, Menu.NONE, R.string.saveProduct);

	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// check menu ID
	switch (item.getItemId()) {
	case 1: this.syncSchema();
	    break;
	case 2: this.syncData();
	    break;
	case 3: this.saveContact();
	    break;
	case 4: this.saveProduct();
	    break;	
	}
	
	return false;
    }
    
    @Override // override to open database connection
    protected void onResume() {
	appBase.openDB();
	super.onResume();
    }

    @Override // override to close database connection
    protected void onPause() {
	appBase.closeDB();
	super.onPause();
    }
    
    // method to handle sync schema menu button
    private void syncSchema() {
	// sync with web service
	appBase.syncLayoutStructure();
	contactsAppPart.fetchElements(this);
        contactsAppPart.refreshLayout(contactsLayout, this);
        productsAppPart.fetchElements(this);
        productsAppPart.refreshLayout(productsLayout, this);
        
	//List<AppPart> appParts = appBase.getAllAppParts();
	//ArrayAdapter<AppPart> adapter = (ArrayAdapter<AppPart>) getListAdapter();
        //adapter.clear();
        
        //Iterator<AppPart> iterator = appParts.iterator();
        
        //while (iterator.hasNext())
        //{
    	//adapter.add(iterator.next());
        //}
        
        //adapter.notifyDataSetChanged();
        
        Toast.makeText(getApplicationContext(), "Layout updated", Toast.LENGTH_SHORT).show();
    }
    
    // method to handle sync data menu button
    private void syncData() {
	appBase.saveAppPartDataWeb(contactsAppPart, this);
	appBase.saveAppPartDataWeb(productsAppPart, this);
    }
    
    // method to handle saving current contact data
    private void saveContact() {
	appBase.saveAppPartDataLocal(contactsAppPart, contactsLayout, this);
    }
    
    // method to handle saving current product data
    private void saveProduct() {
	appBase.saveAppPartDataLocal(productsAppPart, productsLayout, this);
    }
}