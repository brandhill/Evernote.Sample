package com.mitsw.evernote.helloworld;

import com.evernote.client.android.EvernoteSession;

import android.app.Activity;
import android.os.Bundle;

public class ParentActivity extends Activity {
    
    private static final String CONSUMER_KEY = "YOUR_KEY_HERE";
    private static final String CONSUMER_SECRET = "YOUR_SECRET_HERE";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    protected EvernoteSession mEvernoteSession;
    
    protected static final String INTENT_GUID = "guid";
    protected static final String INTENT_NAME = "name";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mEvernoteSession = EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE);
    }

}
