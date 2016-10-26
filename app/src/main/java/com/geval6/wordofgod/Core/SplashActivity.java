package com.geval6.wordofgod.Core;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.geval6.wordofgod.R;

public class SplashActivity extends AppCompatActivity {
    private Typeface medium;
    TextView copyrightTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActionBar();
        prepareLayoutViews();
        executeThread();
    }

    private void prepareActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void prepareLayoutViews() {
        setContentView(R.layout.splash_activity);
        copyrightTextview= (TextView) findViewById(R.id.copyrightTextview);
        prepareFont();
    }

    private void prepareFont(){
        medium= Typeface.createFromAsset(getAssets(),"WorkSans-Medium.otf");
        copyrightTextview.setTypeface(medium);
    }

    private void executeThread() {

        Thread thread = new Thread() {
            public void run() {

                try {
                    sleep(5 * 1000);
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    Log.i("Exception", e.toString());
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

