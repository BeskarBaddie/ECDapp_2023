package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
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
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private ExoPlayer player;
    private PlayerView playerView;
    TextView textView;
    private String url;
    private boolean playwhenready = false;
    private int currentwindow= 0;
    private long playbackposition =0;
    boolean fullscreen = false;
    ImageView fullscreenButton;

    ImageButton downloadBtn;

    String name, downloadurl;




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

        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);

        textView.setText(title);

        downloadBtn = findViewById(R.id.download_button_viewholder);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullscreen){
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenVideo.this,R.drawable.baseline_fullscreen_24)
                            );
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenVideo.this,R.drawable.baseline_fullscreen_exit_24)
                    );

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;

                }

            }
        });





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
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        player.setPlayWhenReady(playwhenready);
        player.seekTo(currentwindow,playbackposition);
        player.prepare();



        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Toast.makeText(FullscreenVideo.this, "Video downloading", Toast.LENGTH_SHORT).show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){
                        String permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        requestPermissions(new String[]{permission},PERMISSION_STORAGE_CODE);
                    }else{
                        downloadurl = intent.getExtras().getString("ur");
                        startDownloading(downloadurl);
                    }
                }else{
                    downloadurl = intent.getExtras().getString("ur");
                    startDownloading(downloadurl);
                }




            }
        });
    }

    private void startDownloading(String downloadurl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadurl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

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
    public void onBackPressed(){
        super.onBackPressed();

        player.stop();
        releasePlayer();

        final Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloading(downloadurl);
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}