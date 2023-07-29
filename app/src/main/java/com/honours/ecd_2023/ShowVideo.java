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
import android.widget.Button;
import android.widget.ImageButton;
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


public class ShowVideo extends AppCompatActivity {

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


                    holder.setExoplayer(getApplication(), model.getTitle(), model.getFileURL(), model.getTags(), model.getTopics());

                    holder.setOnClickListener(new ViewHolder.clicklistener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            name = getItem(position).getTitle();
                            url = getItem(position).getFileURL();
                            tag = getItem(position).getTags();
                            Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                            intent.putExtra("nm", name);
                            intent.putExtra("ur", url);
                            intent.putExtra("tg", tag);
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

        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>()
                .setQuery(databaseReference, Video.class)
                .build();

        FirebaseRecyclerAdapter<Video,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {



                    holder.setExoplayer(getApplication(), model.getTitle(), model.getFileURL(), model.getTags(), model.getTopics());

                    holder.setOnClickListener(new ViewHolder.clicklistener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            name = getItem(position).getTitle();
                            url = getItem(position).getFileURL();
                            tag = getItem(position).getTags();
                            Intent intent = new Intent(ShowVideo.this, FullscreenVideo.class);
                            intent.putExtra("nm", name);
                            intent.putExtra("ur", url);
                            intent.putExtra("tg", tag);
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
}