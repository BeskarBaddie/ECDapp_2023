package com.honours.ecd_2023;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ShowAssignedContent extends AppCompatActivity {

    private Spinner spinnerTags;
    private String selectedTopic;

    private ApiService apiService;

    private static final int PERMISSION_STORAGE_CODE = 1000;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;

    List<Video> videoList = null;


    private VideoListAdapter adapter;
    Button toUpload;

    Button assigned_content;

    ImageButton downloadBtn;

    String name, url, downloadurl, tag;

    PlayerView playerView;

    public ExoPlayer player;

    private List<Video> contentList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recyclerview_Showvideo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toUpload = findViewById(R.id.uploadVideoScreen);
        assigned_content = findViewById(R.id.assigned_content);

        spinnerTags = findViewById(R.id.spinner_tags);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tag_options, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTags.setAdapter(adapter);

        // Set up the Spinner listener
        spinnerTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected tag
                selectedTopic = parent.getItemAtPosition(position).toString();

                if (videoList != null) { // Add this null check
                    filterVideos(selectedTopic);
                } else {
                    // Handle the case where videoList is null (e.g., API call failed)
                    // Display an appropriate message to the user or take necessary action
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected (optional)
            }
        });





        toUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpload();

            }
        });
    }

    private void openFullscreenActivity(Video video) {
        Intent intent = new Intent(ShowAssignedContent.this, FullscreenVideo.class);
        intent.putExtra("nm", video.getTitle());
        intent.putExtra("videoData", video.getFile());
        // ... Add more data as needed
        startActivity(intent);
    }

    private void openFullscreenActivityPDF(Video video) {
        Intent intent = new Intent(ShowAssignedContent.this, PDFViewerActivity.class);
        intent.putExtra("nm", video.getTitle());
        intent.putExtra("ur", video.getFile());
        // ... Add more data as needed
        startActivity(intent);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This is the ID of the back button in the action bar
            onBackPressed(); // Call the onBackPressed() method to handle the back button action
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void filterVideos(String topic){

        adapter = new VideoListAdapter(ShowAssignedContent.this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
        // Call the API or fetch videos based on the selected topic

        // After fetching the videos, filter them based on the selected topic
        List<Video> filteredVideoList = new ArrayList<>();
        for (Video video : videoList) {
            if (video.getTopics().equals(topic)) {
                filteredVideoList.add(video);
            }
        }

        // Update your RecyclerView adapter with the filtered video list
        adapter.updateVideoList(filteredVideoList);
        adapter.notifyDataSetChanged();

        // Set an item click listener for the filtered video list
        adapter.setOnItemClickListener(position -> {
            Video video = filteredVideoList.get(position);
            if (video.getTags().equals("video")) {
                openFullscreenActivity(video); // Open full-screen activity with the clicked video
            }
            if (video.getTags().equals("pdf")) {
                openFullscreenActivityPDF(video); // Open full-screen activity with the clicked video
            }
        });


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (viewHolder instanceof ViewHolder) {
                ViewHolder exoViewHolder = (ViewHolder) viewHolder;
                if (exoViewHolder.player != null) {
                    exoViewHolder.player.stop();
                    exoViewHolder.player.release();
                }
            }
        }

        final Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }



    public void goToUpload() {

        Intent intent = new Intent(ShowAssignedContent.this,VideoContent.class);
        startActivity(intent);


    }

    private String retrieveAuthToken() {
      SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
      return sharedPreferences.getString("auth_token", null);
    }



    @Override
    protected void onStart() {
        super.onStart();

        try{

            String token = retrieveAuthToken();

            Call<List<Video>> content = ApiService.getInterface().getAssignedContent("Token " + token);

            adapter = new VideoListAdapter(ShowAssignedContent.this, new ArrayList<>());
            recyclerView.setAdapter(adapter);

            try{

            content.enqueue(new retrofit2.Callback<List<Video>>() {
                @Override
                public void onResponse(Call<List<Video>> call, retrofit2.Response<List<Video>> response) {
                    if(response.isSuccessful()){
                        String message = "Response succesful";
                        Toast.makeText(ShowAssignedContent.this, message, Toast.LENGTH_LONG).show();

                        videoList = response.body();

                        if (videoList != null && !videoList.isEmpty()) {
                            // Update your RecyclerView adapter with the new videoList
                            adapter.updateVideoList(videoList);

                            // Optionally, notify the adapter about the data change
                            adapter.notifyDataSetChanged();

                            adapter.setOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Video video = videoList.get(position);
                                    if(video.getTags().equals("video")) {
                                        openFullscreenActivity(video); // Open full-screen activity with the clicked video
                                    }
                                    if(video.getTags().equals("pdf")) {
                                        openFullscreenActivityPDF(video); // Open full-screen activity with the clicked video
                                    }

                                }
                            });
                        } else {
                            String message1 = "No videos found";
                            Toast.makeText(ShowAssignedContent.this, message1, Toast.LENGTH_LONG).show();
                        }

                    }else{
                        try {
                            String errorBody = response.errorBody().string();
                            // Display or log the error body for debugging purposes
                            Toast.makeText(ShowAssignedContent.this, "Error: " + errorBody, Toast.LENGTH_LONG).show();
                            System.out.println("THE ERROR IS "+ errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Video>> call, Throwable t) {

                    String message = t.getLocalizedMessage();
                    Toast.makeText(ShowAssignedContent.this, message, Toast.LENGTH_LONG).show();

                }
            });}catch(Exception ex ){
                System.out.println("THE ERROR IS " + ex);
            }

        }catch(Exception ex)
        {
            System.out.println("THE ERROR IS " + ex);
        }



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
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }


            public void onFailure(Call call, IOException e) {
                // Handle download failure
                runOnUiThread(() -> Toast.makeText(ShowAssignedContent.this, "Download failed", Toast.LENGTH_SHORT).show());
            }


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
                            Toast.makeText(ShowAssignedContent.this, "Download completed", Toast.LENGTH_SHORT).show();
                            // Now you can use the videoFile for offline playback within the app
                            // E.g., you can pass the videoFile's path to the ExoPlayer to play the video.
                        });
                    } catch (IOException e) {
                        // Handle download error
                        runOnUiThread(() -> Toast.makeText(ShowAssignedContent.this, "Error while saving the file", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Handle download error
                    runOnUiThread(() -> Toast.makeText(ShowAssignedContent.this, "Download failed", Toast.LENGTH_SHORT).show());
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase_video);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        MenuItem downloadsMenuItem = menu.findItem(R.id.settings_item);
        downloadsMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Open the activity where the downloads are saved
                Intent downloadsIntent = new Intent(ShowAssignedContent.this, VideoListActivity.class);
                startActivity(downloadsIntent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}