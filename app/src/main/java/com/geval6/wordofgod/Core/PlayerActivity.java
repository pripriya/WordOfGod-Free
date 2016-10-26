package com.geval6.wordofgod.Core;


import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geval6.wordofgod.DataModals.Chapter;
import com.geval6.wordofgod.DataModals.Gospel;
import com.geval6.wordofgod.R;
import com.geval6.wordofgod.Utilities.ComponentsManager.ComponentsManager;
import com.geval6.wordofgod.Utilities.RequestManager.RequestIdentifier;
import com.geval6.wordofgod.Utilities.RequestManager.RequestListener;
import com.geval6.wordofgod.Utilities.RequestManager.WebRequest;
import com.geval6.wordofgod.Utilities.SettingsManager.SettingsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlayerActivity extends AppCompatActivity implements RequestListener {

    private WebView webView;
    private SeekBar seekBar;
    private TextView currentDurationTextView, remainingDurationTextView;
    private TextView titleTextView, gospelArtistTextView;
    private ImageView actionImageView;
    private MediaPlayer mediaPlayer;

    private Gospel gospel = ComponentsManager.gospel;
    private Chapter chapter = ComponentsManager.chapter;
    private ArrayList<HashMap> smilContent;
    Handler mHandler = new Handler();
    private Runnable moveSeekBarThread;
    private String highlightedSpan;
    private LinearLayout progressCircle;
    private Typeface medium, regular;

    private Rect spanRect = new Rect(0, 0, 0, 0);

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
        progressCircle = (LinearLayout) view.findViewById(R.id.progressCircle);

        actionBar.setCustomView(view);
    }

    private void prepareLayoutViews() {
        setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webview);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        currentDurationTextView = (TextView) findViewById(R.id.currentDurationTextView);
        remainingDurationTextView = (TextView) findViewById(R.id.remainingDurationTextView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        gospelArtistTextView = (TextView) findViewById(R.id.gospelArtistTextView);
        actionImageView = (ImageView) findViewById(R.id.actionImageView);
        actionImageView.setImageResource(R.drawable.playhidden);
        actionImageView.setEnabled(false);
        prepareFont();
        prepareWebViewListeners();
        prepareSeekbarViewListener();
        prepareActionImageViewListener();

        executeGetSmilContent();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(SettingsManager.hostRoot + SettingsManager.content + chapter.html);
        titleTextView.setText(chapter.title);


        if (gospel.artist != null) {
            gospelArtistTextView.setText((gospel.name + " - " + gospel.artist.name));

        } else {
            gospelArtistTextView.setText(gospel.name);

        }
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(SettingsManager.hostRoot + SettingsManager.content + chapter.audio));
            this.mediaPlayer.prepareAsync();
            mediaPlayer.start();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        prepareMediaPlayerListener();
    }


    private void prepareFont() {
        medium = Typeface.createFromAsset(getAssets(), "WorkSans-Medium.otf");
        regular = Typeface.createFromAsset(getAssets(), "WorkSans-Regular.otf");
        titleTextView.setTypeface(medium);
        gospelArtistTextView.setTypeface(regular);
        currentDurationTextView.setTypeface(regular);
        remainingDurationTextView.setTypeface(regular);
    }

    private void prepareWebViewListeners() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public void onBeginRequest() {
        progressCircle.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRequestCompleted(RequestIdentifier requestIdentifier, Object... object) {
        if (requestIdentifier == RequestIdentifier.getSpan) {
            if (object != null && object.length == 1 && object[0] instanceof HashMap) {
                HashMap hashMap = (HashMap) object[0];
                smilContent = (ArrayList) hashMap.get("pars");
            }
        }
    }

    private void prepareSeekbarViewListener() {
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    mediaPlayer.seekTo(progress);
                    mHandler.removeCallbacks(moveSeekBarThread);
                    mHandler.postDelayed(moveSeekBarThread, 1000);
                    actionImageView.setImageResource(R.drawable.pause);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void prepareActionImageViewListener() {
        actionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });
    }

    private void togglePlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            actionImageView.setImageResource(R.drawable.play);
        } else {

            int length = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            mediaPlayer.seekTo(seekBar.getProgress());
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            seekBar.setMax(mediaPlayer.getDuration());
            mHandler.removeCallbacks(moveSeekBarThread);
            mHandler.postDelayed(moveSeekBarThread, 1000);
            actionImageView.setImageResource(R.drawable.pause);
        }
    }

    private void prepareMediaPlayerListener() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                progressCircle.setVisibility(View.INVISIBLE);
                seekBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                actionImageView.setEnabled(true);
                togglePlayPause();
                mediaPlayer.seekTo(seekBar.getProgress());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                mHandler.removeCallbacks(moveSeekBarThread);
                mHandler.postDelayed(moveSeekBarThread, 1000);
            }
        });
        mediaHandler();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mediaPlayer.getCurrentPosition() > 0) {
                    PlayerActivity.this.finish();
                }
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    private void mediaHandler() {
        this.mHandler = new Handler();
        this.moveSeekBarThread = new Runnable() {
            int Duration;
            int Durationminutes;
            int Durationseconds;
            int Posminutes;
            int Posseconds;

            @Override
            public void run() {

                if (mediaPlayer != null) {

                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    String spanToHighlight = getSpanId(currentPosition);
                    if (spanToHighlight != highlightedSpan) {
                        if (highlightedSpan != null) {
                            dehighlightSpan(highlightedSpan);
                        }
                        highlightSpan(spanToHighlight);
                    }
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    Posseconds = currentPosition % 60;
                    Posminutes = (currentPosition / 60) % 60;
                    Duration = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                    Durationseconds = (this.Duration / 1000) % 60;
                    Durationminutes = (this.Duration / 60000) % 60;
                    currentDurationTextView.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(this.Posminutes), Integer.valueOf(this.Posseconds)}));
                    remainingDurationTextView.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(this.Durationminutes), Integer.valueOf(this.Durationseconds)}));
                    mHandler.postDelayed(this, 1000);
                } else {
                    Log.i("Media Player*****", "Media Player is null");
                }
            }
        };
    }

    private void executeGetSmilContent() {
        HashMap parameters = new HashMap();
        parameters.put("smil", chapter.smil);
        WebRequest webRequest = new WebRequest(RequestIdentifier.getSpan, parameters, PlayerActivity.this, PlayerActivity.this);
        webRequest.execute();
    }

    private void highlightSpan(final String span) {
        if (span != highlightedSpan) {
            String getSpanJavascript = "document.getElementById('" + span + "')";
            webView.evaluateJavascript(getSpanJavascript + ".className = 'highlight';", null);
            highlightedSpan = span;
            webView.evaluateJavascript(getSpanJavascript + ".offsetTop", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    try {
                        if (!value.equals("null") && !value.isEmpty()) {
                            spanRect = new Rect(0, Integer.parseInt(value), webView.getWidth(), spanRect.right);
                            dummy();
                        }
                    } catch (Exception e) {
                        Log.i("Error", e.toString());
                    }
                }
            });
            webView.evaluateJavascript(getSpanJavascript + ".offsetHeight", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    try {
                        if (!value.equals("null") && !value.isEmpty()) {
                            spanRect = new Rect(0, spanRect.top, webView.getWidth(), Integer.parseInt(value));
                            dummy();
                        }
                    } catch (Exception e) {
                        Log.i("Error", e.toString());
                    }
                }
            });
        }
    }

    private void dehighlightSpan(String span) {
        String getSpanJavascript = "document.getElementById('" + span + "')";
        webView.evaluateJavascript(getSpanJavascript + ".className = '';", null);
        this.highlightedSpan = null;
    }

    private void dummy() {
        Rect visibleRect = new Rect(0, webView.getScrollY(), webView.getWidth(), webView.getScrollY() + webView.getHeight());
        if (visibleRect.contains(spanRect)) {
            Log.i("------", "Should Not Scroll");
        } else {

            Log.i("------", "SCROLL");
        }
    }

    private String getSpanId(int seconds) {
        if (smilContent != null) {
            Iterator it = this.smilContent.iterator();
            while (it.hasNext()) {
                Object span = it.next();
                float _start = Float.valueOf(((HashMap) span).get("beginClip").toString()).floatValue();
                float _end = Float.valueOf(((HashMap) span).get("endClip").toString()).floatValue();
                if (_start < ((float) seconds) && _end > ((float) seconds)) {
                    return ((HashMap) span).get("spanId").toString().toLowerCase();
                }
            }
        }
        return null;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            mediaPlayer = null;
        } else if (mediaPlayer == null) {
            mediaPlayer.release();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            int length = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }
        if (mediaPlayer.isPlaying()) {
            actionImageView.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}


