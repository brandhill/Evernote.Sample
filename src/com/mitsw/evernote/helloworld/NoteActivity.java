package com.mitsw.evernote.helloworld;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;
import com.mitsw.evernote.helloworld.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NoteActivity extends ParentActivity {
    private ProgressBar mPgbLoading;
    private TextView mTxtMsg;
    private String mGuid;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mPgbLoading = (ProgressBar) findViewById(R.id.pgb_loading);
        mTxtMsg = (TextView) findViewById(R.id.txt_notebook);
        mPgbLoading.setVisibility(View.GONE);
        mTxtMsg.setVisibility(View.GONE);
        
        mGuid = getIntent().getStringExtra(INTENT_GUID);
        mName = getIntent().getStringExtra(INTENT_NAME);

        if ((mGuid == null && mGuid.equals("")) || mName == null) {
            mTxtMsg.setText("No guid Or Notebook name is null");
            mTxtMsg.setVisibility(View.VISIBLE);
        } else {
            getActionBar().setTitle(mName);
            getNote();
            Log.i("Guid", mGuid);
        }
    }

    private void getNote() {
        mPgbLoading.setVisibility(View.VISIBLE);
        try {
            mEvernoteSession.getClientFactory().createNoteStoreClient()
            
                .getNote(mGuid, true, true, false, false, new OnClientCallback<Note> () {

                    @Override
                    public void onSuccess(Note data) {
                        mPgbLoading.setVisibility(View.GONE);
                        mTxtMsg.setText("Note Title: " + data.getTitle() + "\n" +
                                "Note Content: " + data.getContent());

                        mTxtMsg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onException(Exception exception) {
                        mPgbLoading.setVisibility(View.GONE);
                        exception.printStackTrace();
                    }
                    
                });
        } catch (TTransportException e) {
            mPgbLoading.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }
}
