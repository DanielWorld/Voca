package com.namgyuworld.voca.activity.voca;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.namgyuworld.voca.MyApplication;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.customview.TopMenuBarForWebview;
import com.namgyuworld.voca.util.Consts;
import com.namgyuworld.voca.util.Logger;
import com.namgyuworld.voca.util.convert.StringUtil;
import com.namgyuworld.voca.util.convert.Regex;
import com.namgyuworld.voca.util.download.DownloadAudio;
import com.namgyuworld.voca.util.filepath.FilePath;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Search Vocabulary from Google dictionary
 * <br><br>
 * Created by Daniel Park on 2015-04-09.
 */
public class SearchVocaActivity extends Activity {

    final String URL = "http://www.google.com/search?q=define:";

    private final String TAG = SearchVocaActivity.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private Map<String, String> mHeaders = new HashMap<String, String>(); // It is used to put Header to WebView
    private String word; // The 'word' that you wanna search for.

    private WebView mWebView;
    private ProgressDialog pd; // Progress dialog
    private TextView tv;

    private TopMenuBarForWebview mTopMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_voca);

        Bundle data = getIntent().getExtras();
        word = data.getString(Consts.SEARCH_WORD_KEY);


        mTopMenuBar = (TopMenuBarForWebview) findViewById(R.id.topMenuBar_for_webview);
        mTopMenuBar.setWord(word); // save word which you're searching for.

        LinearLayout mLayout = (LinearLayout) findViewById(R.id.search_voca_layout);
        tv = (TextView) findViewById(R.id.result_of_voca_search);

        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }
        });
//        mHeaders.put("Accept-Language", "en-kr");
        mHeaders.put("Accept-Language", "en");
        mWebView.loadUrl(URL + word);
        mLayout.addView(mWebView);

        getSearchResult();

        // Start Google Analytics tracking
        startGoogleAnalytics();
    }
    /**
     * Start Google Analytics tracking
     */
    private void startGoogleAnalytics(){
        // Google Analytics Tracker activated
        Tracker t = ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
        // Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);
        t.setScreenName("Search Voca from online");
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onStart() {
        // Start Google Analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        // Stop Google Analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    private void startProgressDialog() {
        pd = ProgressDialog.show(SearchVocaActivity.this, null, "Under construction...");
    }

    private void stopProgressDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }

    /**
     * Get the result of searching word..
     */
    private void getSearchResult() {

        startProgressDialog(); // Start progress dialog

        RequestQueue mRequestQueue;
        mRequestQueue = Volley.newRequestQueue(SearchVocaActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, URL + word, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String indentSpace = "       ";  // This is indent or tab empty space
                String result = "";
                /*
-				 * Let's parsing the html code using JSoup Library
-				 */
                Document doc = Jsoup.parse(response);
                // Voca Container (Which contains vocabulary window)
                Elements container = doc.getAllElements().select("div.lr_container.mod");

                /**
                 * Okay, sometimes you typed misspell word. in that case, don't worry. <br>
                 *     This VOCA app automatically fixes the issue.
                 */
                // real voca
                Elements realWord = container.select("div.vk_ans");

                try {
                    word = realWord.get(0).text();
                    word = new Regex().convertToOnlyAlphabets(word);
                }catch(Exception e){

                }

                // Voca pronunciation characters
                Elements vocaPronunciation = container.select("div.lr_dct_ent_ph");
                result += "\n" + vocaPronunciation.text() + "\n";

                // Audio src
                String audioSrc = container.select("audio[data-dobid=\"aud\"]").attr("src");

                // If audio source URL is not empty or not null, then start download
                if (!StringUtil.isNullorEmpty(audioSrc)) {
                    try {
                        String realSource = "http:" + audioSrc;

                        // /sdcard/Android/data/<package-name>/files/audio/<word>.mp3
                        String audioDownDirPath = FilePath.getVocaMP3ExternalPath(SearchVocaActivity.this, word);
                        DownloadAudio.startDownload(realSource, audioDownDirPath);
                    } catch (Exception e) {
                        LOG.e(TAG, e.getMessage());
                    }
                }

                Elements parts = container.select("div.lr_dct_sf_h");
                Elements mainFrame = container.select("ol.lr_dct_sf_sens");
                int frameCounts = parts.size(); // how many field you have? noun) , verb)...

                // inside of mainFrame there are another fields
                for (int i = 0; i < frameCounts; i++) {
                    result += "\n" + parts.get(i).text() + ")" + "\n";
                    Elements aaa = mainFrame.get(i).select("div.lr_dct_sf_sen.vk_txt");
                    for (int j = 0; j < aaa.size(); j++) {
                        Elements vocaMeanings = aaa.get(j).select("div[data-dobid=\"dfn\"]");
                        Elements vocaExamples = aaa.get(j).select("div.vk_gy");

//                        for(Element tt: vocaMeanings){
//                            result += tt.text() + "\n";
//                        }
                        for (int k = 0; k < vocaMeanings.size(); k++) {
                            result += indentSpace + (k + 1) + ". " + vocaMeanings.get(k).text() + "\n";
                        }
                        for (Element bb : vocaExamples) {
                            result += "\n" + indentSpace + bb.text();
                        }
                    }
                    result += "\n";
                }
                tv.setText(word + result);
                // update real word
                mTopMenuBar.setWord(word);
                mTopMenuBar.setVocaContents(result);

                stopProgressDialog(); // Stop progress dialog
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog(); // Stop progress dialog
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept-Language", "en-kr"); // en is not working... because I live in Korea. so en-kr is better way.
                return headers;
            }
        };
        mRequestQueue.add(request);
    }
}
