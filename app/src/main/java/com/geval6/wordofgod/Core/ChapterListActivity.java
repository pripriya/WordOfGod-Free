package com.geval6.wordofgod.Core;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geval6.wordofgod.Adapter.ChaptersAdapter;
import com.geval6.wordofgod.Adapter.GosepelChapterListAdapter;
import com.geval6.wordofgod.DataModals.Chapter;
import com.geval6.wordofgod.DataModals.Collection;
import com.geval6.wordofgod.DataModals.Gospel;
import com.geval6.wordofgod.R;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.RequestManager.RequestIdentifier;
import com.geval6.wordofgod.Utilities.RequestManager.RequestListener;
import com.geval6.wordofgod.Utilities.RequestManager.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ChapterListActivity extends AppCompatActivity implements RequestListener {

    LinearLayout progressCircle;
    GridView gridView;
    Gospel gospel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInternetConnectivity();
    }

    private void checkInternetConnectivity() {

        if (ComponentsManager.getConnectivityStatus(this) == true) {
            prepareActionBar();
            prepareLayoutViews();
        } else if (ComponentsManager.getConnectivityStatus(this) == false) {
           Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void prepareActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.actionbar_layout, null);
        actionBar.setCustomView(view);
    }

    private void prepareLayoutViews() {
        setContentView(R.layout.activity_gosepl_chapterlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = (GridView) findViewById(R.id.gridview);
        progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
        executeRequest();
    }

    private void executeRequest() {
        HashMap parameters = new HashMap();
        parameters.put("gospel_id", getIntent().getStringExtra("id"));
        WebRequest webRequest = new WebRequest(RequestIdentifier.gospelChapters, parameters, ChapterListActivity.this, ChapterListActivity.this);
        webRequest.execute();
    }

    @Override
    public void onBeginRequest() {
        progressCircle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestCompleted(RequestIdentifier requestIdentifier, Object... object) {
        if (object != null) {

            ArrayList<Chapter> chapters = (ArrayList<Chapter>) object[0];

            GosepelChapterListAdapter gosepelChapterListAdapter = new GosepelChapterListAdapter(ChapterListActivity.this, chapters);
            gospel = ComponentsManager.gospel;
            gridView.setAdapter(gosepelChapterListAdapter);

            progressCircle.setVisibility(View.INVISIBLE);
        } else {
            Log.i("error", "error");
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        onBackPressed();
        return true;
    }
}
