package com.honours.ecd_2023;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.Collections;

public class FullscreenVideo extends AppCompatActivity {

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
        player.addMediaItems(Collections.singletonList(mediaItem));
        //player.prepare();
        player.setPlayWhenReady(playwhenready);
        player.seekTo(currentwindow,playbackposition);
        player.prepare();
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
        }
    }
}