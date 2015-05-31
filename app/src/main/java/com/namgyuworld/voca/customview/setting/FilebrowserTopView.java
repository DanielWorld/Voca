package com.namgyuworld.voca.customview.setting;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.namgyuworld.utility.file.FilePath;
import com.namgyuworld.voca.R;

/**
 * Created by danielpark on 5/31/15.
 */
public class FilebrowserTopView extends RelativeLayout {

    private Handler mHandler;

    private TextView currentPathView;
    private ImageButton goToParentDir;

    public FilebrowserTopView(Context context) {
        super(context);
        init(context);
    }

    public FilebrowserTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilebrowserTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FilebrowserTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setHandler(Handler h){
        mHandler = h;
    }

    public void changeContent(Object obj){
        currentPathView.setText(obj.toString());
    }


    private void init(final Context context){
        View v = LayoutInflater.from(context).inflate(R.layout.view_filebrowser_topview, null);
        currentPathView = (TextView) v.findViewById(R.id.currentFilePath_view);
        goToParentDir = (ImageButton) v.findViewById(R.id.goToParentDir);
        goToParentDir.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                String parentPath= FilePath.getFileParentPath(context, currentPathView.getText().toString());
                currentPathView.setText(parentPath.toString());

                Message msg = Message.obtain();
                msg.obj = parentPath;
                msg.what = 234567;
                mHandler.sendMessage(msg);
            }
        });
        addView(v);

        // Set default file path
        currentPathView.setText(FilePath.getFilePublicDownloadPath(context));
    }
}
