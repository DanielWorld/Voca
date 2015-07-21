package com.namgyuworld.voca.customview;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.namgyuworld.utility.Logger;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.database.VocaDBOpenHelper;
import com.namgyuworld.voca.util.convert.StringUtil;

/**
 * Created by Daniel Park on 2015-04-09.
 */
public class TopMenuBarForWebview extends LinearLayout implements View.OnClickListener {
    private final String TAG = TopMenuBarForWebview.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private Context mContext;
    private TextView tv;
    private String contents = "";

    private EditText mEditText;

    private Handler h;

    public TopMenuBarForWebview(Context context) {
        super(context);
        initialize(context);
    }

    public TopMenuBarForWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TopMenuBarForWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopMenuBarForWebview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    public void setHandler(Handler h){
        this.h = h;
    }

    private void initialize(Context mContext) {
        this.mContext = mContext;
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_topmenubar_webview, null);
        v.findViewById(R.id.voca_add).setOnClickListener(this);
        v.findViewById(R.id.voca_add_custom).setOnClickListener(this);
        tv = (TextView) v.findViewById(R.id.voca_that_we_need_to_search_for);
        tv.setOnClickListener(this);

        mEditText = (EditText) v.findViewById(R.id.voca_search_in_webview);
        v.findViewById(R.id.voca_search_in_webview_btn).setOnClickListener(this);

        addView(v);
    }

    /**
     * Word that you received from voca-page
     *
     * @param word
     */
    public void setWord(String word) {
        tv.setText(StringUtil.setNullToEmpty(word));
        LOG.i(TAG, word);
    }

    public void setVocaContents(String contents) {
        this.contents = StringUtil.setNullToEmpty(contents);
        LOG.i(TAG, contents);
    }

    public String getWord() {
        return this.tv.getText().toString();
    }

    public String getContents() {
        return this.contents;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voca_add:
            case R.id.voca_that_we_need_to_search_for:
                new VocaDBOpenHelper(mContext).saveVoca(getWord(), getContents());
                Toast.makeText(mContext, tv.getText().toString() + " is saved in DB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voca_add_custom:
                View view = LayoutInflater.from(mContext).inflate(R.layout.view_add_voca_custom, null);

                final EditText vocaEdit = (EditText) view.findViewById(R.id.editText);
                final EditText contentEdit = (EditText) view.findViewById(R.id.editText2);

                // Show new dialog message
                new AlertDialog.Builder(mContext).setView(view)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(!StringUtil.isNullorEmpty(vocaEdit.getText().toString()) && !StringUtil.isNullorEmpty(contentEdit.getText().toString())) {

                                    new VocaDBOpenHelper(mContext).saveVoca(vocaEdit.getText().toString(), contentEdit.getText().toString());
                                    Toast.makeText(mContext, vocaEdit.getText().toString() + " is saved in DB", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(mContext, "Fill in the blank", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
                break;
            case R.id.voca_search_in_webview_btn:

                if(StringUtil.isNullorEmpty(mEditText.getText().toString())) {
                    return;
                }
                else {

                    Message msg = Message.obtain();
                    msg.what = 12345;
                    msg.obj = mEditText.getText().toString();
                    // then clear mEditText
                    mEditText.setText("");

                    h.sendMessage(msg);
                }
                break;
        }
    }
}
