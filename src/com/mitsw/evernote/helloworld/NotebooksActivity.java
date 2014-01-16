package com.mitsw.evernote.helloworld;

import java.util.ArrayList;
import java.util.List;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;
import com.mitsw.evernote.helloworld.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NotebooksActivity extends ParentActivity {
    private ListView mLsvNotebooks;
    private ProgressBar mPgbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks);

        mLsvNotebooks = (ListView) findViewById(R.id.lsv_notes);
        mPgbLoading = (ProgressBar) findViewById(R.id.pgb_loading);

        getNotebooks();
    }

    private void getNotebooks() {
        mPgbLoading.setVisibility(View.VISIBLE);
        try {
            mEvernoteSession.getClientFactory().createNoteStoreClient()
                    .listNotebooks(new OnClientCallback<List<Notebook>>() {

                        @Override
                        public void onSuccess(final List<Notebook> data) {
                            mPgbLoading.setVisibility(View.GONE);
                            
                            List<String> notebooks = new ArrayList<String>();
                            
                            for(int i = 0; i < data.size(); i++) {
                                notebooks.add(data.get(i).getName());
                            }
                            
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.listitem_text,
                                    R.id.txt_note,
                                    notebooks);
                            mLsvNotebooks.setAdapter(adapter);
                            mLsvNotebooks.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
                                    intent.putExtra(INTENT_GUID, data.get(position).getGuid());
                                    intent.putExtra(INTENT_NAME, data.get(position).getName());
                                    startActivity(intent);
                                }
                            });
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
