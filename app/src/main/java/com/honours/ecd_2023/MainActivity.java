package com.honours.ecd_2023;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button button;
    ProgressBar progressBar;
    EditText editText;
    private Uri videoUri;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoview_main);
        button = findViewById(R.id.button_upload_main);
        progressBar = findViewById(R.id.progressbar_main);
        editText = findViewById(R.id.et_video_name);
        mediaController = new MediaController(this);

        videoView.setMediaController(mediaController);
        videoView.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO || resultCode == RESULT_OK ||
                data != null || data.getData() !=null){
            videoUri = data.getData();

            videoView.setVideoURI(videoUri);
        }
    }

    public void ChooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO);


    }

    public void ShowVideo(View view) {
    }
}