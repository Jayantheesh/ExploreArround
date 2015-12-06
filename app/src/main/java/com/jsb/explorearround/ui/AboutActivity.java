package com.jsb.explorearround.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.jsb.explorearround.R;

/**
 * Created by JSB on 11/10/15.
 */
public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "About";
    private Toolbar toolbar;
    private TextView mAbout;

    public static void actionAboutActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, AboutActivity.class);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.d(TAG, "onCreate");

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAbout = (TextView) findViewById(R.id.about_text);
        mAbout.setText("Explore Around App Version=" + getResources().getString(R.string.version_name));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

}
