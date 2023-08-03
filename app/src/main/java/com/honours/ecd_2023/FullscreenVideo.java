package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.widget.Button;
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
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    private FirebaseAnalytics firebaseAnalytics;

    Button downloadBtn;

    Button checkButton;

    String title, downloadurl, check;




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

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Intent intent = getIntent();
        url = intent.getExtras().getString("ur");
        title = intent.getExtras().getString("nm");

        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);

        textView.setText(title);

        downloadBtn = findViewById(R.id.download_button_viewholder);
        checkButton = findViewById(R.id.check_button);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDownloads();
            }
        });


        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullscreen){
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenVideo.this,R.drawable.ic_fullscreen_open)
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
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenVideo.this,R.drawable.ic_fullscreen_close)
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

        //DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                //Util.getUserAgent(this, "YourAppName")); // Replace "YourAppName" with your app's name.
        //return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
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
                Toast.makeText(FullscreenVideo.this, "Download Started", Toast.LENGTH_SHORT).show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){
                        String permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        requestPermissions(new String[]{permission},PERMISSION_STORAGE_CODE);
                    }else{
                        downloadurl = intent.getExtras().getString("ur");
                        startDownloading(downloadurl,title);
                    }
                }else{
                    downloadurl = intent.getExtras().getString("ur");
                    startDownloading(downloadurl,title);
                }




            }
        });
    }

    private void startDownloading(String downloadurl, String title) {
        if (downloadurl == null) {
            // Handle the case when download URL is null
            Toast.makeText(this, "Download URL is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = title + ".mp4";
        File internalStorageDir = getApplicationContext().getFilesDir();
        File videoFile = new File(internalStorageDir, fileName);

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(downloadurl)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle download failure
                runOnUiThread(() -> Toast.makeText(FullscreenVideo.this, "Download failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try (InputStream inputStream = response.body().byteStream();
                         OutputStream outputStream = new FileOutputStream(videoFile)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        runOnUiThread(() -> {
                            // File downloaded successfully, do additional processing if needed
                            Toast.makeText(FullscreenVideo.this, "Download completed", Toast.LENGTH_SHORT).show();
                            // Now you can use the videoFile for offline playback within the app
                            // E.g., you can pass the videoFile's path to the ExoPlayer to play the video.
                        });
                    } catch (IOException e) {
                        // Handle download error
                        runOnUiThread(() -> Toast.makeText(FullscreenVideo.this, "Error while saving the file", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Handle download error
                    runOnUiThread(() -> Toast.makeText(FullscreenVideo.this, "Download failed", Toast.LENGTH_SHORT).show());
                }
            }
        });

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

    public void goToDownloads() {

        Intent intent = new Intent(FullscreenVideo.this,VideoListActivity.class);
        startActivity(intent);


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloading(downloadurl, title);
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}