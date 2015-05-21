package com.namgyuworld.voca.util.filebrowser;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.namgyuworld.voca.R;
import com.namgyuworld.voca.util.filebrowser.FileBrowserAdapter;
import com.namgyuworld.voca.util.filepath.FilePath;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Daniel Park on 2015-05-04.
 */
public class FileBrowserActivity extends Activity{

    // This is RecyclerView test
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> myDataset = new ArrayList<String>();

        setContentView(R.layout.view_filebrowser);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /**
         * Get list of file in Download directory
         */
        try {
            File dir = new File(FilePath.getVocaFileDownloadPath(getApplicationContext(), ""));
            File[] list = dir.listFiles();

            for(int i=0; i <list.length; i++){
                myDataset.add(list[i].getName());
            }
        }catch (Exception e){

        }

        // specify an adapter (see also next example)
        mAdapter = new FileBrowserAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
}
