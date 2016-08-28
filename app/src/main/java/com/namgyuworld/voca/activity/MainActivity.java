package com.namgyuworld.voca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.namgyuworld.voca.MyApplication;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.activity.voca.SearchVocaActivity;
import com.namgyuworld.voca.adapter.MainPagerAdapter;
import com.namgyuworld.voca.customview.TopMenuBar;
import com.namgyuworld.voca.customview.VocaSeekBar;
import com.namgyuworld.voca.database.VocaDBOpenHelper;
import com.namgyuworld.voca.model.VocaPOJO;
import com.namgyuworld.voca.util.Consts;
import com.namgyuworld.voca.util.SharedPrefUtil;
import com.namgyuworld.voca.util.convert.ZeroUtil;
import com.namgyuworld.voca.util.logger.Logger;

import java.util.List;

/**
 * Created by duniv017 on 2015-04-09.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Logger LOG = Logger.getInstance();

    private ViewPager pager = null;
    private MainPagerAdapter pagerAdapter = null;
    private TopMenuBar mTopMenuBar;

    private List<VocaPOJO> mList;

    private VocaSeekBar mVocaSeekBar;

    // Decide to exit the application or not.
    private boolean isBackKeyPressed = false;
    // Is no more showing toast message?
    private boolean isNoMoreToast = false;
    private Toast warningToast;

    SharedPrefUtil mPref;

    // Man.. I had no other choices, but handler.
    public static Handler mHanlder;

    // button that turn to next page or previous page.
//    private ImageView mVocaRight, mVocaLeft;

    //-----------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {

        LOG.i("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        warningToast = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT);

        init();

        /**
         * This is not good way for performance, but you gotta save all record while you're ruffling pages..
         */
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Wow. This sucks.. I could have found a better solution.
                mPref.setCurrentPage(position);
                try {
                    // Make sure to deliver word to TopMenuBar.class
                    mTopMenuBar.setCurrentWord(mList.get(position).getVocaWord());

                    // Change Seekbar as well
                    mVocaSeekBar.setProgress(position);
                } catch (Exception e) {
                    LOG.e(e.getMessage());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // No over scroll, which means no blue effects are available
        pager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // Seek bar
        if (mPref.isVocaSeekBarVisible()) {
            mVocaSeekBar.setVisibility(View.VISIBLE);
        } else {
            mVocaSeekBar.setVisibility(View.GONE);
        }

        mHanlder = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                super.handleMessage(msg);

                switch (msg.what) {
                    case Consts.DELETE_VOCA:
                        LOG.i("Delete the current vocabulary. and refresh the page");
                        // Remove this vocabulary from database.
                        new VocaDBOpenHelper(MainActivity.this).removeVoca(pager.getCurrentItem());
                        break;
                    case Consts.REFRESH_VOCA:
                        RefreshVocaPage();
                        break;
                }
            }
        };

        // Start Google Analytics tracking
        startGoogleAnalytics();

    }

    private void init(){
        mPref = new SharedPrefUtil(getApplicationContext());

        // Top Menu bar
        mTopMenuBar = (TopMenuBar) findViewById(R.id.topMenuBar);

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);

        // Seek bar
        mVocaSeekBar = (VocaSeekBar) findViewById(R.id.voca_seekBar);

        // ImageView
        findViewById(R.id.voca_left).setOnClickListener(this);
        findViewById(R.id.voca_right).setOnClickListener(this);

        // Add voca
        findViewById(R.id.voca_add).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // get total view page
        int totalViews = pagerAdapter.getCount();
        // get current page index
        int currentViewIndex = pager.getCurrentItem();

        switch (v.getId()){
            case R.id.voca_left:
                if(currentViewIndex > 0)
                    pager.setCurrentItem(currentViewIndex - 1);
                break;
            case R.id.voca_right:
                if(currentViewIndex < totalViews - 1)
                    pager.setCurrentItem(currentViewIndex + 1);
                break;
            case R.id.voca_add:
                Intent i = new Intent(this, SearchVocaActivity.class);
//                i.putExtra(Consts.SEARCH_WORD_KEY, mEditText.getText().toString());
                startActivity(i);
                break;
        }
    }

    /**
     * Start Google Analytics tracking
     */
    private void startGoogleAnalytics(){
        // Google Analytics Tracker activated
        Tracker t = ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
        // Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);
        t.setScreenName("Main Voca");
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onStart() {
        LOG.i("onStart()");
        // Start Google Analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();

    }

    @Override
    protected void onStop() {
        LOG.i("onStop()");
        // Stop Google Analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LOG.i("onResume()");
        RefreshVocaPage();
        LOG.i("pagerAdapter getCount(): " + pagerAdapter.getCount());

        // Voca seekbar state
        mVocaSeekBar.setMax(ZeroUtil.convertMinusToZero(pagerAdapter.getCount() - 1));
        if (mPref.isVocaSeekBarVisible()) {
            mVocaSeekBar.setVisibility(View.VISIBLE);
        } else {
            mVocaSeekBar.setVisibility(View.GONE);
        }
        mVocaSeekBar.setProgress(mPref.getCurrentPage());
        mVocaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pager.setCurrentItem(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Refresh vocabulary and paste it on view
     */
    public void RefreshVocaPage() {
        pagerAdapter.removeAllView(pager);
        try {
            mList = new VocaDBOpenHelper(MainActivity.this).getAllVocaList();
            LayoutInflater inflater = getLayoutInflater();
            LinearLayout[] fLayouts = new LinearLayout[mList.size()];

            for (int i = 0; i < mList.size(); i++) {
                fLayouts[i] = (LinearLayout) inflater.inflate(R.layout.fragment_main, null);
                TextView vocaView = (TextView) fLayouts[i].findViewById(R.id.page_word_view);
                TextView vocaContents = (TextView) fLayouts[i].findViewById(R.id.page_word_content_view);
                vocaView.setTextSize((float) mPref.getVocaFontSize());
                vocaView.setText(mList.get(i).getVocaWord());
                vocaContents.setTextSize((float) mPref.getVocaFontContentSize());
                vocaContents.setText(mList.get(i).getVocaContents());
//                LOG.i(TAG, mList.get(i).getVocaWord() + "\n" + mList.get(i).getVocaContents());
//                pagerAdapter.addView(fLayouts[i], 0);
                pagerAdapter.addView(fLayouts[i]);
            }
            pagerAdapter.notifyDataSetChanged();

            try {
                // Go to the certain page that saved before.
                pager.setCurrentItem(mPref.getCurrentPage());

                // Make sure to deliver word to TopMenuBar.class
                mTopMenuBar.setCurrentWord(mList.get(mPref.getCurrentPage()).getVocaWord());
            } catch (Exception e) {
                LOG.e(e.getMessage());
                // If something is wrong, just forget to that page
            }
        } catch (Exception e) {
            LOG.e(e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        int pageIndex = pagerAdapter.addView(newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem(pageIndex, true);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

    @Override
    public void onBackPressed() {
        if (isBackKeyPressed) {
            // Save current page
//            mPref.setCurrentPage(pager.getCurrentItem());
            /**
             * No need to save current page, cause you've already done that before terminating the application.
             */
            warningToast.cancel();
            isNoMoreToast = true;
            super.onBackPressed();
        }

        isBackKeyPressed = true;

        if(!isNoMoreToast)
            warningToast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackKeyPressed = false;
            }
        }, 2000);
    }
}