package com.honours.ecd_2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity implements VideoListAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private VideoListAdapter adapter;
    private List<File> videoFilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of video files from the internal storage
        videoFilesList = getFilesFromInternalStorage();

        List<Video> videoList = new ArrayList<>();
        for (File file : videoFilesList) {
            Video video = new Video(file.getName(), file.getAbsolutePath(), "Tag", "Topics"); // Assuming Video has a constructor like this
            videoList.add(video);
        }



        // Initialize and set up the RecyclerView adapter
        adapter = new VideoListAdapter(this, videoList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // Method to retrieve video files from internal storage
    private List<File> getFilesFromInternalStorage() {
        List<File> videoFiles = new ArrayList<>();
        File internalStorageDir = getApplicationContext().getFilesDir();
        File[] files = internalStorageDir.listFiles();

        // Add all video files to the list
        if (files != null) {
            for (File file : files) {
                //if (file.isFile() && file.getName().endsWith(".mp4"))
                if (file.isFile()) {
                    videoFiles.add(file);
                }
            }
        }
        return videoFiles;
    }

    // RecyclerView item click event handler
    @Override
    public void onItemClick(int position) {
        // Handle item click here
        // When an item is clicked, open the FullscreenVideo activity and pass the selected video's URI to it.
        File selectedVideoFile = videoFilesList.get(position);
        Uri videoUri = Uri.fromFile(selectedVideoFile);

        Intent intent = new Intent(this, FullscreenVideo.class);
        intent.putExtra("videoData", videoUri.toString());
        intent.putExtra("nm", selectedVideoFile.getName());
        startActivity(intent);
    }
}
