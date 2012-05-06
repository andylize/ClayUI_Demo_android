package com.netinfocentral.ClayUI_Demo_android;

import java.util.Iterator;
import java.util.List;

import com.netinfocentral.ClayUI.AppPart;
import com.netinfocentral.ClayUI.ClayUIAppBase;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ClayUI_Demo_androidActivity extends ListActivity {
    
    // define local variables
    ClayUIAppBase appBase;
    LinearLayout mainLayout;
    
    static String BASE_URI_LOCAL = "http://192.168.102.80/ClayUI/";
    static String BASE_URI_INTERNET = "http://nicentral.dyndns.org:8888/ClayUI/";
    Button syncButton;
    
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // setup local widgets
        syncButton = (Button) findViewById(R.id.syncButton);
        syncButton.setOnClickListener(syncButton_OnClick);
        mainLayout = (LinearLayout) findViewById(R.id.syncData);
        
        // instantiate ClayUI AppBase
        appBase = new ClayUIAppBase(1, this, BASE_URI_LOCAL);
        
        List<AppPart> appParts = appBase.getAllAppParts();
        ArrayAdapter<AppPart> adapter = new ArrayAdapter<AppPart>(this, android.R.layout.simple_list_item_1, appParts);
        setListAdapter(adapter);
    }
    
    // click handler for button
    View.OnClickListener syncButton_OnClick = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
    	
            // sync with web service
            appBase.syncLayoutStructure();
            List<AppPart> appParts = appBase.getAllAppParts();
            ArrayAdapter<AppPart> adapter = (ArrayAdapter<AppPart>) getListAdapter();
            adapter.clear();
            
            Iterator<AppPart> iterator = appParts.iterator();
            
            while (iterator.hasNext())
            {
        	adapter.add(iterator.next());
            }
            
            adapter.notifyDataSetChanged();
            
            Toast.makeText(getApplicationContext(), "Layout updated", Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override // override to open database connection
    protected void onResume() 
    {
	appBase.openDB();
	super.onResume();
    }

    @Override // override to close database connection
    protected void onPause() 
    {
	appBase.closeDB();
	super.onPause();
    }
}