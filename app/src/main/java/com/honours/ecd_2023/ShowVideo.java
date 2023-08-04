package com.honours.ecd_2023;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ShowVideo extends AppCompatActivity {

    private Spinner spinnerTags;
    private String selectedTopic;

    private static final int PERMISSION_STORAGE_CODE = 1000;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;

    Button toUpload;

    ImageButton downloadBtn;

    String name, url, downloadurl, tag;

    PlayerView playerView;

    public ExoPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);


        recyclerView = findViewById(R.id.recyclerview_Showvideo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("content");
        toUpload = findViewById(R.id.uploadVideoScreen);

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

                // Call the filterVideos method with the selected tag
                filterVideos(selectedTopic);
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

    private void filterVideos(String topic){
        Query query;
        if ("All".equals(topic)) {
            // Fetch all videos if "All" is selected
            query = databaseReference.orderByChild("tags");
        } else {
            // Fetch videos with the selected tag
            query = databaseReference.orderByChild("topics").equalTo(topic);
        }


        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>()
                .setQuery(query, Video.class)
                .build();

        FirebaseRecyclerAdapter<Video,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {


                holder.setExoplayer(getApplication(), model.getTitle(), model.getFileURL(), model.getTags(), model.getTopics());
                holder.setDownloadButtonIcon(getApplication(), model.getTitle());
                holder.setOnClickListener(new ViewHolder.clicklistener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        name = getItem(position).getTitle();
                        url = getItem(position).getFileURL();
                        tag = getItem(position).getTags();
                        if(tag.equals("video")){
                            Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                            intent.putExtra("nm", name);
                            intent.putExtra("ur", url);
                            intent.putExtra("tg", tag);
                            startActivity(intent);

                        }
                        if(tag.equals("pdf")){
                            Intent intent = new Intent(ShowVideo.this, PDFViewerActivity.class);
                            intent.putExtra("nm", name);
                            intent.putExtra("ur", url);
                            intent.putExtra("tg", tag);
                            startActivity(intent);

                        }



                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new ViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);




    }



    private void firebaseSearch(String searchText){
        String query = searchText.toLowerCase();
        Query firebaseQuery = databaseReference.orderByChild("search").startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>()
                .setQuery(firebaseQuery, Video.class)
                .build();

        FirebaseRecyclerAdapter<Video,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {


                    holder.setExoplayer(getApplication(), model.getTitle(), model.getFileURL(), model.getTags(), model.getTopics());
                holder.setDownloadButtonIcon(getApplication(), model.getTitle());
                    holder.setOnClickListener(new ViewHolder.clicklistener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            name = getItem(position).getTitle();
                            url = getItem(position).getFileURL();
                            tag = getItem(position).getTags();
                            if(tag.equals("video")){
                                Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                                intent.putExtra("nm", name);
                                intent.putExtra("ur", url);
                                intent.putExtra("tg", tag);
                                startActivity(intent);

                            }
                            if(tag.equals("pdf")){
                                Intent intent = new Intent(ShowVideo.this, PDFViewerActivity.class);
                                intent.putExtra("nm", name);
                                intent.putExtra("ur", url);
                                intent.putExtra("tg", tag);
                                startActivity(intent);

                            }

                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new ViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);




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

        Intent intent = new Intent(ShowVideo.this,VideoContent.class);
        startActivity(intent);


    }


    @Override
    protected void onStart() {
        super.onStart();


        Query query = databaseReference.orderByChild("tags");


        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>()
                .setQuery(query, Video.class)
                .build();

        FirebaseRecyclerAdapter<Video,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {

                holder.setExoplayer(getApplication(), model.getTitle(), model.getFileURL(), model.getTags(), model.getTopics());
                holder.setDownloadButtonIcon(getApplication(), model.getTitle());
                holder.setOnClickListener(new ViewHolder.clicklistener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        name = getItem(position).getTitle();
                        url = getItem(position).getFileURL();
                        tag = getItem(position).getTags();
                        if(tag.equals("video")){
                            Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                            intent.putExtra("nm", name);
                            intent.putExtra("ur", url);
                            intent.putExtra("tg", tag);
                            startActivity(intent);

                        }
                        if(tag.equals("pdf")){
                            Intent intent = new Intent(ShowVideo.this, PDFViewerActivity.class);
                            intent.putExtra("nm", name);
                            intent.putExtra("ur", url);
                            intent.putExtra("tg", tag);
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                return new ViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);




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
                runOnUiThread(() -> Toast.makeText(ShowVideo.this, "Download failed", Toast.LENGTH_SHORT).show());
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
                            Toast.makeText(ShowVideo.this, "Download completed", Toast.LENGTH_SHORT).show();
                            // Now you can use the videoFile for offline playback within the app
                            // E.g., you can pass the videoFile's path to the ExoPlayer to play the video.
                        });
                    } catch (IOException e) {
                        // Handle download error
                        runOnUiThread(() -> Toast.makeText(ShowVideo.this, "Error while saving the file", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Handle download error
                    runOnUiThread(() -> Toast.makeText(ShowVideo.this, "Download failed", Toast.LENGTH_SHORT).show());
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
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        MenuItem downloadsMenuItem = menu.findItem(R.id.settings_item);
        downloadsMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Open the activity where the downloads are saved
                Intent downloadsIntent = new Intent(ShowVideo.this, VideoListActivity.class);
                startActivity(downloadsIntent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}