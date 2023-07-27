package com.honours.ecd_2023;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collections;

public class FullscreenVideo extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private ExoPlayer player;
    private PlayerView playerView;
    TextView textView;
    private String url;
    private boolean playwhenready = false;
    private int currentwindow= 0;
    private long playbackposition =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Fullscreen");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        playerView = findViewById(R.id.exoplayer_fullscreen);
        textView = findViewById(R.id.tv_fullscreen);

        Intent intent = getIntent();
        url = intent.getExtras().getString("ur");
        String title = intent.getExtras().getString("nm");

        textView.setText(title);



    }

    private MediaSource buildMediaSource(Uri uri){
        DataSource.Factory datasourcefactory =
                new DefaultHttpDataSource.Factory();//might be a problem 13 mins
        return new ProgressiveMediaSource.Factory(datasourcefactory).createMediaSource(MediaItem.fromUri(uri));//problem
    }

    private void initializeplayer(){
        player = new ExoPlayer.Builder(getApplication()).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(url);//might be a problem
        String videoName = getVideoNameFromMediaItem(mediaItem);
        if (videoName!= null) {
            textView.setText(videoName);
        }
        player.addMediaItems(Collections.singletonList(mediaItem));
        //player.prepare();
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        player.setPlayWhenReady(playwhenready);
        player.seekTo(currentwindow,playbackposition);
        player.prepare();
        player.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                float percentageWatched = (player.getCurrentPosition() * 100f) / player.getDuration();
                logVideoWatchedPercentageEvent(percentageWatched, videoName);
            }
        });

        logVideoStartedEvent(videoName);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Util.SDK_INT>=26){
            initializeplayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.SDK_INT>=26 || player == null){
            //initializeplayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT>26){
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (Util.SDK_INT>=26){
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player !=null){

            playwhenready = player.getPlayWhenReady();
            playbackposition = player.getCurrentPosition();
            currentwindow = player.getCurrentWindowIndex();
            player = null;
            logVideoWatchedCompletelyEvent(getVideoNameFromMediaItem(player.getCurrentMediaItem()));
        }
    }
    public void onBackPressed(){
        super.onBackPressed();

        player.stop();
        releasePlayer();

        final Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
    private void logVideoStartedEvent(String videoName) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Started");
        params.putString("video_name", videoName); // Replace with the actual video name
        mFirebaseAnalytics.logEvent("video_started", params);
        Log.d("Video Started", "Video Started" +params.toString());
    }
    private void logVideoWatchedCompletelyEvent(String videoName) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Watched Completely");
        params.putString("video_name", videoName); // Replace with the actual video name
        mFirebaseAnalytics.logEvent("video_watched_completely", params);
        Log.d("Video Watched Completely", "Video Watched Completely" +params.toString());
    }
    private void logVideoWatchedPercentageEvent(float percentage, String videoName) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Watched Percentage");
        params.putString("video_name", videoName); // Replace with the actual video name
        params.putFloat("percentage_watched", percentage);
        mFirebaseAnalytics.logEvent("video_watched_percentage", params);
        Log.d("Video Watched Percentage", "Video Watched Percentage" +params.toString());
    }
    private String getVideoNameFromMediaItem(MediaItem mediaItem) {
        if (mediaItem != null && mediaItem.mediaMetadata != null) {
            return mediaItem.mediaMetadata.title.toString();
        }
        Log.d("Video Name", "Video Name" +mediaItem.mediaMetadata.title.toString());
        return null;
    }
}