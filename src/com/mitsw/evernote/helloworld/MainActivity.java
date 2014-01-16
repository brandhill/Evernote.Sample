package com.mitsw.evernote.helloworld;

import com.evernote.client.android.InvalidAuthenticationException;
import com.mitsw.evernote.helloworld.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ParentActivity {
    private Button mBtnLogin, mBtnGetNotebooks;
    private TextView mTxtToken;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTxtToken = (TextView) findViewById(R.id.txt_evernote_token);
        
        mBtnGetNotebooks = (Button) findViewById(R.id.btn_get_notebooks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        if(mEvernoteSession.isLoggedIn()) {
            mBtnLogin.setText(getString(R.string.logout));
            mBtnLogin.setOnClickListener(logoutListener);
            mTxtToken.setText(mEvernoteSession.getAuthToken());
            
            mBtnGetNotebooks.setVisibility(View.VISIBLE);
            mBtnGetNotebooks.setOnClickListener(getNotebooksListener);
        } else {
            mBtnLogin.setText(getString(R.string.login));
            mBtnLogin.setOnClickListener(loginListener);
            mTxtToken.setText(getString(R.string.hello_world));
            mBtnGetNotebooks.setVisibility(View.GONE);
        }
    }
    
    private OnClickListener getNotebooksListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), NotebooksActivity.class);
            startActivity(intent);
        }
    };
    
    private OnClickListener logoutListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                mEvernoteSession.logOut(getApplicationContext());
                onResume();
            } catch (InvalidAuthenticationException e) {
                e.printStackTrace();
            }
        }
    };
    
    private OnClickListener loginListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            mEvernoteSession.authenticate(getApplicationContext());
        }
    };
}
