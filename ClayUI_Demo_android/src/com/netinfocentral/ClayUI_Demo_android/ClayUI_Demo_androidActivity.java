package com.netinfocentral.ClayUI_Demo_android;

import com.netinfocentral.ClayUI.ClayUIAppBase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClayUI_Demo_androidActivity extends Activity {
    
    // define local variables
    ClayUIAppBase appBase;
    
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
        
        
        // instantiate ClayUI AppBase
        appBase = new ClayUIAppBase(1, this, BASE_URI_LOCAL);
    }
    
    // click handler for button
    View.OnClickListener syncButton_OnClick = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
    	
            // sync with web service
            appBase.syncLayoutStructure();
            
            Toast.makeText(getApplicationContext(), "Layout updated", Toast.LENGTH_SHORT).show();
        }
    };
}