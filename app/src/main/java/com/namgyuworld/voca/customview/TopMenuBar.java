package com.namgyuworld.voca.customview;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.namgyuworld.voca.MainActivity;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.activity.voca.SearchVocaActivity;
import com.namgyuworld.voca.activity.settings.SettingActivity;
import com.namgyuworld.voca.util.Consts;
import com.namgyuworld.voca.util.Logger;
import com.namgyuworld.voca.util.convert.StringUtil;
import com.namgyuworld.voca.util.filepath.FilePath;
import com.namgyuworld.voca.util.media.MediaPlayers;

import java.io.File;
import java.util.Locale;

/**
 * Top Menu bar
 * <br><br>
 * Created by Daniel Park on 2015-04-09.
 */
public class TopMenuBar extends LinearLayout implements View.OnClickListener {
    private final String TAG = TopMenuBar.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private Context mContext;
    private EditText mEditText;
    private String mCurrentVoca;

    // TTS voice
    private TextToSpeech mTTS;

    /**
     * InterstitialAd (Google Admob)
     */
    InterstitialAd mInterstitialAd;

    public TopMenuBar(Context context) {
        super(context);
        initialize(context);
    }

    public TopMenuBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TopMenuBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopMenuBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(final Context mContext) {
        this.mContext = mContext;
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_topmenubar, null);

        mEditText = (EditText) v.findViewById(R.id.voca_type_word);  // The field that be filled with vocabulary to search for

        v.findViewById(R.id.voca_sound).setOnClickListener(this); // Vocabulary sound
        v.findViewById(R.id.voca_search).setOnClickListener(this); // Vocabulary search
        v.findViewById(R.id.voca_delete).setOnClickListener(this); // Vocabulary search
        v.findViewById(R.id.voca_setting).setOnClickListener(this); // Vocabulary setting
        v.findViewById(R.id.delete_voca_type_word).setOnClickListener(this); // Delete Vocabulary typed word
        addView(v);

        // InterstitialAd (Google Admob)
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-9259248617591148/6779675913"); // This is real admob unit id
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // This is test admob unit id
        requestNewInterstitial(mContext);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial(mContext);
                goToSearchVocaActivity(mContext);
            }
        });
    }

    /**
     * Set current page's word
     *
     * @param currentWord
     */
    public void setCurrentWord(String currentWord) {
        this.mCurrentVoca = currentWord;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voca_sound:
//                Toast.makeText(mContext, "voca sound", Toast.LENGTH_SHORT).show();
                String audioPath = FilePath.getVocaMP3ExternalPath(mContext, mCurrentVoca);
                File file = new File(audioPath);

                if (file.exists() && !file.isDirectory() && file.length() != 0) {
                    // OKAY, mp3 file exists
                    MediaPlayers.AudioPlay(audioPath);
                } else {
                    // Sorry. mp3 file wasn't found. Use TTS instead.
                    mTTS = new TextToSpeech(mContext.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                mTTS.setLanguage(Locale.US);
                                mTTS.setSpeechRate(0.8f); // Slow speech rate. default value is 1.0f
                                mTTS.speak(mCurrentVoca, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                }
                break;
            case R.id.voca_search:
                goToSearchVocaActivity(mContext);
                break;
            case R.id.voca_delete:
                new AlertDialog.Builder(mContext).setMessage(mContext.getResources().getString(R.string.delete_voca_check))
                        .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.mHanlder.sendEmptyMessage(Consts.DELETE_VOCA);
                            }
                        }).setNegativeButton(mContext.getResources().getString(R.string.no), null).create().show();

                break;
            case R.id.voca_setting:
//                Toast.makeText(mContext, "voca setting", Toast.LENGTH_SHORT).show();
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.delete_voca_type_word:
                mEditText.setText("");
                // After clear EditText, show up keyboard
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                /**
                 * How to hide keyboard?
                 * InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                 */
                break;
        }

    }

    private void requestNewInterstitial(Context ctx) {
        // When you try to get addTestDevice, just by using Logcat - tag : 'Ads', you get hashed device id
        // If it matches with test device, ad will show test image page.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    /**
     * Go to {@link com.namgyuworld.voca.activity.voca.SearchVocaActivity}
     *
     * @param mContext
     */
    public void goToSearchVocaActivity(Context mContext) {
        if (!StringUtil.isNullorEmpty(mEditText.getText().toString())) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent i = new Intent(mContext, SearchVocaActivity.class);
                i.putExtra(Consts.SEARCH_WORD_KEY, mEditText.getText().toString());
                mContext.startActivity(i);
            }
        }
        else{
            Toast.makeText(mContext, "빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
