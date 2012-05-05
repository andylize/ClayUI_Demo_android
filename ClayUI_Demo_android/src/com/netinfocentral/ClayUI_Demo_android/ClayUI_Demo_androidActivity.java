package com.netinfocentral.ClayUI_Demo_android;

import com.netinfocentral.ClayUI.ClayUIAppBase;

import android.app.Activity;
import android.os.Bundle;

public class ClayUI_Demo_androidActivity extends Activity {
    
    // define local variables
    ClayUIAppBase appBase;
    static String BASE_URI_LOCAL = "http://192.168.102.80/ClayUI/";
    static String BASE_URI_INTERNET = "http://nicentral.dyndns.org:8888/ClayUI/";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // instantiate ClayUI AppBase
        appBase = new ClayUIAppBase(1, this, BASE_URI_LOCAL);


        appBase.listAppParts();
    }
}