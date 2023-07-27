package com.honours.ecd_2023;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.firebase.analytics.FirebaseAnalytics;

public class ShowVideo extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    private FirebaseAnalytics mFirebaseAnalytics;
    Button toUpload;

    String name, url;

    PlayerView playerView;

    public ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        recyclerView = findViewById(R.id.recyclerview_Showvideo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("video");
        toUpload = findViewById(R.id.uploadVideoScreen);





        toUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpload();

            }
        });
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

                holder.setExoplayer(getApplication(),model.getName(),model.getVideourl());

                holder.setOnClickListener(new ViewHolder.clicklistener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        name = getItem(position).getName();
                        url = getItem(position).getVideourl();
                        Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                        intent.putExtra("nm" , name);
                        intent.putExtra("ur",url);
                        startActivity(intent);

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
        logVideoSearchedEvent(searchText);
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

        logVideoUploadedEvent();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>()
                .setQuery(databaseReference, Video.class)
                .build();

        FirebaseRecyclerAdapter<Video,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {

                holder.setExoplayer(getApplication(),model.getName(),model.getVideourl());

                holder.setOnClickListener(new ViewHolder.clicklistener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        name = getItem(position).getName();
                        url = getItem(position).getVideourl();
                        Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                        intent.putExtra("nm" , name);
                        intent.putExtra("ur",url);
                        startActivity(intent);
                        logVideoSelectedEvent(name);
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

        return super.onCreateOptionsMenu(menu);
    }
    private void logVideoSearchedEvent(String searchQuery) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, params);
    }
    private void logVideoSelectedEvent(String videoName) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Selected");
        params.putString("video_name", videoName); // Replace with the actual video name
        mFirebaseAnalytics.logEvent("video_selected", params);
    }
    private void logVideoUploadedEvent(){
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Uploaded");
        mFirebaseAnalytics.logEvent("video_uploaded", params);
    }
}