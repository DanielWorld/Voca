package com.namgyuworld.voca.activity.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.namgyuworld.utility.Logger;
import com.namgyuworld.voca.R;
import com.namgyuworld.voca.activity.view.FilebrowserActivity;
import com.namgyuworld.voca.customview.SearchVocaDatabase;
import com.namgyuworld.voca.database.VocaDBOpenHelper;
import com.namgyuworld.voca.service.AutoDownloadAudioService;
import com.namgyuworld.voca.util.AppUtil;
import com.namgyuworld.voca.util.SharedPrefUtil;
import com.namgyuworld.voca.util.convert.StringUtil;
import com.namgyuworld.voca.util.filepath.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Daniel Park on 4/10/15.
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    private final String TAG = SettingActivity.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private SharedPrefUtil mPref;

    ImageView mSearchVocaDBImage;
    RelativeLayout backupDB, loadDB, downloadMp3;
    SearchVocaDatabase mSearchVocaDBView;

    CheckBox vocaSeekBarVisibility;

    final int GET_FILE_PATH_FROM_BROWSER = 12392;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init() {

        mPref = new SharedPrefUtil(getApplicationContext());

        backupDB = (RelativeLayout) findViewById(R.id.backup_voca_db);
        loadDB = (RelativeLayout) findViewById(R.id.load_voca_db);
        downloadMp3 = (RelativeLayout) findViewById(R.id.download_mp3_files);
        mSearchVocaDBView = (SearchVocaDatabase) findViewById(R.id.search_voca_in_DB);
        mSearchVocaDBImage = (ImageView) findViewById(R.id.search_voca_in_DB_img);
        vocaSeekBarVisibility = (CheckBox) findViewById(R.id.voca_seekbar_checkBox);

        // Daniel (2016-08-27 16:42:32): TEST (구글 인앱 결제 관련)
        findViewById(R.id.testView).setOnClickListener(this);

        backupDB.setOnClickListener(this);
        loadDB.setOnClickListener(this);
        downloadMp3.setOnClickListener(this);
        mSearchVocaDBImage.setOnClickListener(this);
        vocaSeekBarVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPref.setVocaSeekBarVisible(isChecked);
            }
        });

        vocaSeekBarVisibility.setChecked(mPref.isVocaSeekBarVisible());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Search voca in database
            case R.id.search_voca_in_DB_img:
                // 1. get selected text
//                mSearchVocaDBView.getText();
                // 2. change main pages index? yes sure you do.
                // All you need to do is just save position?

                String position = new VocaDBOpenHelper(SettingActivity.this).getPositionFromVoca(mSearchVocaDBView.getText().toString());
                if (StringUtil.isNullorEmpty(position)) {
                    // return value is null or empty, then do nothing...
                    return;
                } else {
                    // Save the position in SharedPreferences..
                    mPref.setCurrentPage(Integer.parseInt(position));
                    Toast.makeText(SettingActivity.this, "Move to " + mSearchVocaDBView.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                break;
            // Backup vocabulary database
            case R.id.backup_voca_db:
                new AlertDialog.Builder(SettingActivity.this).setMessage(getString(R.string.setting_backup_db_question))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                backupDatabase();
                            }
                        }).setNegativeButton(getString(R.string.no), null).create().show();
                break;
            case R.id.load_voca_db:
                new AlertDialog.Builder(SettingActivity.this).setMessage(getString(R.string.setting_load_db_question))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                loadDatabase();
                                startActivityForResult(new Intent(SettingActivity.this, FilebrowserActivity.class), GET_FILE_PATH_FROM_BROWSER);
                            }
                        }).setNegativeButton(getString(R.string.no), null).create().show();
                break;
            case R.id.download_mp3_files:
                AutoDownloadAudioService.startService(SettingActivity.this);
                break;

            case R.id.testView:

                break;
        }
    }

    /**
     * Backup vaca db from /data/.. to /Download
     */
    private void backupDatabase() {
        String databasePath = FilePath.getVocaDatabasePath(SettingActivity.this, VocaDBOpenHelper.DB_NAME);
        String downloadPath = FilePath.getVocaFileDownloadPath(SettingActivity.this, VocaDBOpenHelper.DB_NAME);

        InputStream myInput = null;
        OutputStream myOutput = null;

        File databaseFile = new File(databasePath);
        File downloadFile = new File(downloadPath);

        try {
            // No database to back up, then finish
            if (!databaseFile.exists())
                return;

            myInput = new FileInputStream(databaseFile);
            myOutput = new FileOutputStream(downloadFile);

            byte[] buffer = new byte[1024];
            int count;
            while ((count = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, count);
            }

            Toast.makeText(SettingActivity.this, getString(R.string.setting_backup_db_succeed), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LOG.e(TAG, "Database backup error! " + e.getMessage());
            Toast.makeText(SettingActivity.this, getString(R.string.setting_backup_db_failed), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (myOutput != null)
                    myOutput.close();
                if (myInput != null)
                    myInput.close();
            } catch (Exception e) {
                LOG.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Load database file from 'Download' to '/data/...'
     */
    private void loadDatabase(String filePath) {
        String databasePath = FilePath.getVocaDatabasePath(SettingActivity.this, VocaDBOpenHelper.DB_NAME);
//        String downloadPath = FilePath.getVocaFileDownloadPath(SettingActivity.this, VocaDBOpenHelper.DB_NAME);
        String downloadPath = filePath;

        InputStream myInput = null;
        OutputStream myOutput = null;

        File databaseFile = new File(databasePath);
        File downloadFile = new File(downloadPath);

        try {
            // No database to back up, then finish
            if (!databaseFile.exists())
                return;

            myInput = new FileInputStream(downloadFile);
            myOutput = new FileOutputStream(databaseFile);

            byte[] buffer = new byte[1024];
            int count;
            while ((count = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, count);
            }

            Toast.makeText(SettingActivity.this, getString(R.string.setting_load_db_succeed), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LOG.e(TAG, "Database backup error! " + e.getMessage());
            Toast.makeText(SettingActivity.this, getString(R.string.setting_load_db_failed), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (myOutput != null)
                    myOutput.close();
                if (myInput != null)
                    myInput.close();
            } catch (Exception e) {
                LOG.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            // Get file path from FileBrowserActivity
            if(requestCode == GET_FILE_PATH_FROM_BROWSER){
                loadDatabase(data.getStringExtra("loadDBPath"));
            }
        }
    }
}
