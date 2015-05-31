package com.namgyuworld.voca.activity.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.namgyuworld.voca.R;
import com.namgyuworld.voca.adapter.FilebrowserAdapter;
import com.namgyuworld.voca.customview.setting.FilebrowserTopView;

import java.util.logging.LogRecord;

/**
 * Created by danielpark on 5/31/15.
 */
public class FilebrowserActivity extends Activity{

    public Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                // Data has changed
                case 123456:
                    mFilebrowserTopView.changeContent(msg.obj);
                    break;
                case 234567:
                    mFilebrowserAdapter.update(msg.obj.toString());
                    break;
                case 345678:
                    // Get db file path to load DB and go back to previous SettingActivity
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("loadDBPath", msg.obj.toString());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    break;
            }

        }

    };

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FilebrowserAdapter mFilebrowserAdapter;
    private FilebrowserTopView mFilebrowserTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filebrowser);

        mFilebrowserTopView = (FilebrowserTopView) findViewById(R.id.filebrowser_topview);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mFilebrowserAdapter = new FilebrowserAdapter(FilebrowserActivity.this);
        mAdapter = mFilebrowserAdapter;
        mRecyclerView.setAdapter(mAdapter);

        // set Handler to child views
        mFilebrowserAdapter.setHandler(mHander);
        mFilebrowserTopView.setHandler(mHander);
    }
}
