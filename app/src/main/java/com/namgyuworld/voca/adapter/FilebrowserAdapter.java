package com.namgyuworld.voca.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.namgyuworld.utility.file.FilePath;
import com.namgyuworld.utility.file.FileUtil;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.model.FilebrowserModel;
import com.namgyuworld.voca.util.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by danielpark on 5/31/15.
 */
public class FilebrowserAdapter extends RecyclerView.Adapter<FilebrowserAdapter.ViewHolder> {

    private final String TAG = FilebrowserAdapter.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private Context mContext;
    private Handler mHandler;

    private ArrayList<FilebrowserModel> mDataset = new ArrayList<FilebrowserModel>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private RelativeLayout panel;
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtBehind;
        public ImageView icon;

        public ViewHolder(View v) {
            super(v);
            panel = (RelativeLayout) v.findViewById(R.id.filebrowser_detail_panel);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.modified_dateLine);
            txtBehind = (TextView) v.findViewById(R.id.sizeLine);
            icon = (ImageView) v.findViewById(R.id.icon);
        }
    }

    public void add(int position, FilebrowserModel item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(FilebrowserModel item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Update data with Model
     *
     * @param item
     */
    public void update(FilebrowserModel item) {
        if (item.isDirectory()) {
            mDataset = currentFileList(item.getFilePath());
            notifyDataSetChanged();

            // Let Main Handler know that current file path has changed.
//            mHandler.sendEmptyMessage(123456);

            Message msg = Message.obtain();
            msg.obj = item.getFilePath();
            msg.what = 123456;
            mHandler.sendMessage(msg);
        }
        // if This isn't
        else {
            if(FileUtil.getFileExtension(item.getFilePath()).equals("db")){
                LOG.d("This is db file");

                Message msg = Message.obtain();
                msg.obj = item.getFilePath();
                msg.what = 345678;
                mHandler.sendMessage(msg);
            }
            else{
                LOG.d("This is not db file");
            }
        }
    }

    /**
     * Update data with filePath
     *
     * @param filePath
     */
    public void update(String filePath) {
        File f = new File(filePath);

        if (f.isDirectory()) {
            mDataset = currentFileList(filePath);
            notifyDataSetChanged();

            // Let Main Handler know that current file path has changed.
//            mHandler.sendEmptyMessage(123456);

//            Message msg = Message.obtain();
//            msg.obj = filePath;
//            msg.what = 123456;
//            mHandler.sendMessage(msg);
        }
    }

    /**
     * Make sure to set handler to communicate with other views
     *
     * @param h
     */
    public void setHandler(Handler h) {
        this.mHandler = h;
    }

    public FilebrowserAdapter(Context ctx) {
        this.mContext = ctx;
        mDataset = currentFileList(FilePath.getFilePublicDownloadPath(ctx));
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FilebrowserAdapter(ArrayList<FilebrowserModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FilebrowserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_filebrowser_detail, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final FilebrowserModel name = mDataset.get(position);
        holder.panel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                update(name);
            }
        });
        holder.txtHeader.setText(mDataset.get(position).getFileName());
        holder.txtFooter.setText("size " + FileUtil.convertFileSize(mDataset.get(position).getSize()));
        holder.txtBehind.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(mDataset.get(position).getModifyDate()) + "");

        if (mDataset.get(position).isDirectory()) {
            holder.icon.setImageResource(R.drawable.directory);
        } else {
            holder.icon.setImageResource(R.drawable.file);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * List of All file in specified path
     *
     * @param filePath the path of file which to be listed.
     * @return the List
     */
    private ArrayList<FilebrowserModel> currentFileList(String filePath) {

        ArrayList<FilebrowserModel> arrayList = new ArrayList<FilebrowserModel>();

        try {
            File file = new File(filePath);
            File[] list = file.listFiles();
            for (File i : list) {
                arrayList.add(new FilebrowserModel(i.getName(), i.lastModified(), i.length(), i.isDirectory(), i.getAbsolutePath()));
            }
        }catch(Exception e){
            LOG.e(e.getMessage() + "\n" + e.getStackTrace());
        }

        return arrayList;
    }
}