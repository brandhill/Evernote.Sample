package com.mitsw.evernote.helloworld;

import java.util.ArrayList;
import java.util.List;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;
import com.mitsw.evernote.helloworld.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NotesActivity extends ParentActivity {
    
    String TAG = NotesActivity.class.getSimpleName();
    
    private ProgressBar mPgbLoading;
    private TextView mTxtMsg;
    private ListView mLsvNotes;
    private Button mBtnAdd;

    private String mGuid;
    private String mName;
    private List<Note> mNotes;
    private List<String> mNotesName;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mPgbLoading = (ProgressBar) findViewById(R.id.pgb_loading);
        mTxtMsg = (TextView) findViewById(R.id.txt_notes_message);
        mLsvNotes = (ListView) findViewById(R.id.lsv_notes);
        
        mBtnAdd = (Button)findViewById(R.id.btn_add_notebook);
        

        
        mBtnAdd.setOnClickListener(new OnClickListener() {
            
         
            
            @Override
            public void onClick(View v) {
                try {
                    
                    Note note = new Note();
                    note.setTitle("Android test note");
                    
                    String content =
                            EvernoteUtil.NOTE_PREFIX +
                                "<p>Hello World.</p>" +
                                EvernoteUtil.NOTE_SUFFIX;
                    
                     content =
                            EvernoteUtil.NOTE_PREFIX +
                                "<p>This note was uploaded from Android. It should contains an image.</p>" +
                                "<en-todo/>An item that I haven't completed yet.<br/><en-todo/>An completed item." +
                                EvernoteUtil.NOTE_SUFFIX;

                    note.setContent(content);
                    
                    
                    mEvernoteSession
                    .getClientFactory()
                    .createNoteStoreClient().createNote(note, new OnClientCallback<Note>() {

                        @Override
                        public void onSuccess(Note data) {
                            //Log.d(TAG, "onSuccess");
                            getNotes();
                        }

                        @Override
                        public void onException(Exception exception) {
                            //Log.d(TAG, "onException : ",exception );
                            
                        }
                        
                        
                        
                    });
                } catch (TTransportException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
        
        
        mPgbLoading.setVisibility(View.GONE);
        mTxtMsg.setVisibility(View.GONE);

        mGuid = getIntent().getStringExtra(INTENT_GUID);
        mName = getIntent().getStringExtra(INTENT_NAME);
        
        //Log.d(TAG, "mGuid, mName :" + mGuid+","+mName);

        if ((mGuid == null && mGuid.equals("")) || mName == null) {
            mTxtMsg.setText("No guid Or Notebook name is null");
            mTxtMsg.setVisibility(View.VISIBLE);
        } else {
            getActionBar().setTitle(mName);
            getNotes();
            Log.i("Guid", mGuid);
        }
    }

    private void getNotes() {
        if (mNotes == null) {
            mNotes = new ArrayList<Note>();
        } else {
            mNotes.clear();
        }

        if (mNotesName == null) {
            mNotesName = new ArrayList<String>();
        } else {
            mNotesName.clear();
        }

        NoteFilter filter = new NoteFilter();
        filter.setNotebookGuid(mGuid);
        findNotes(filter, 0);
    }

    private void findNotes(final NoteFilter filter, final int offset) {

        mPgbLoading.setVisibility(View.VISIBLE);
        try {
            mEvernoteSession
                .getClientFactory()
                .createNoteStoreClient()
                .findNotes(filter, offset, 10,
                        new OnClientCallback<NoteList>() {

                            @Override
                            public void onSuccess(NoteList data) {
                                int currectOffset = data.getStartIndex() + data.getNotesSize();
                                int remain = data.getTotalNotes() - currectOffset;

                                for (Note note : data.getNotes()) {
                                    mNotes.add(note);
                                    mNotesName.add(note.getTitle());
                                }

                                if (remain > 0) {
                                    findNotes(filter, currectOffset);
                                } else {
                                    if (mAdapter == null) {
                                        setAdapter();
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mPgbLoading.setVisibility(View.GONE);
                                }
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

    private void setAdapter() {
        mAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.listitem_text, R.id.txt_note, mNotesName);
        mLsvNotes.setAdapter(mAdapter);
        mLsvNotes.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra(INTENT_GUID, mNotes.get(position).getGuid());
                intent.putExtra(INTENT_NAME, mNotes.get(position).getTitle());
                startActivity(intent);
            }
        });
    }
}
