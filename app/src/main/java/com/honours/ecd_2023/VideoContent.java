package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.analytics.FirebaseAnalytics;
public class VideoContent extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button button;
    ProgressBar progressBar;
    EditText editText;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Video video;
    UploadTask uploadTask;
    String user ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_content);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //video = new Video();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("content");
        user = retrieveUserName();
        videoView = findViewById(R.id.videoview_main);
        button = findViewById(R.id.button_upload_main);
        progressBar = findViewById(R.id.progressbar_main);
        editText = findViewById(R.id.et_video_name);
        mediaController = new MediaController(this);

        videoView.setMediaController(mediaController);
        videoView.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadVideo();

            }
        });

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

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void ShowVideo(View view) {

        Intent intent = new Intent(VideoContent.this,ShowVideo.class);
        startActivity(intent);


    }

    private void UploadVideo(){

        String videoName = editText.getText().toString();
        String search = editText.getText().toString().toLowerCase();

        if(videoUri != null || !TextUtils.isEmpty(videoName)){

            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExt(videoUri));
            uploadTask = reference.putFile(videoUri);

            Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }) .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(VideoContent.this, "Data Saved ", Toast.LENGTH_SHORT).show();
                    video.setTitle(videoName);
                   // video.setFileURL(downloadUrl);
                    video.setSearch(search);
                    video.setTags("video");
                    video.setTopics("Community Content");

                    String i = databaseReference.push().getKey();
                    databaseReference.child(i).setValue(video);

                    logVideoUploadedEvent(videoName, user);

                    }else{
                        Toast.makeText(VideoContent.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }else{
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }

    }
    private void logVideoUploadedEvent(String videoName, String user) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Uploaded");
        params.putString("video_name", videoName);
        params.putString("username", user);
        mFirebaseAnalytics.logEvent("video_uploaded", params);
    }

    private String retrieveUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }

}