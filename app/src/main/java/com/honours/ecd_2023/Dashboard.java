package com.honours.ecd_2023;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity implements ButtonAdapter.OnItemClickListener {



    CardView videoCard;
    private FirebaseAnalytics mFirebaseAnalytics;
    CardView assignedContent;

    Button btnLogout;

    CardView allContent;

    private String clickedItemText;

    RecyclerView rv;
    ArrayList<String> dataSource;
    LinearLayoutManager linearLayoutManager;
    ButtonAdapter buttonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        assignedContent = findViewById(R.id.card_assigned_content);
        allContent = findViewById(R.id.card_all_content);


        rv = findViewById(R.id.horizontal_recycler_view);
        //Setting dataSource
        dataSource = new ArrayList<>();
        dataSource.add("Logout");
        dataSource.add("Website");
        dataSource.add("Downloads");
        dataSource.add("About");



        linearLayoutManager = new LinearLayoutManager(Dashboard.this,LinearLayoutManager.HORIZONTAL,false);
        buttonAdapter = new ButtonAdapter(dataSource, this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(buttonAdapter);




        allContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToAllContent();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "video_card");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "video_button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        assignedContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAssignedContent();
            }
        });

        btnLogout = findViewById(R.id.btn_logout); // Initialize the logout button

        btnLogout.setOnClickListener(v -> logout());




    }


    public void goToDownloads() {

        Intent intent = new Intent(Dashboard.this,VideoListActivity.class);
        startActivity(intent);


    }
    



    private void logout() {
        // Clear the stored credentials
        clearCredentials();

        // Navigate back to the login screen
        Intent intent = new Intent(Dashboard.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Prevents the user from going back to the dashboard using the back button
    }

    private void clearCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove the stored credentials
        editor.remove("auth_token");
        editor.remove("username");

        // Commit the changes
        editor.apply();
    }



    public void goToAllContent() {

        Intent intent = new Intent(Dashboard.this,ShowVideo.class);
        startActivity(intent);


    }

    public void goToAssignedContent() {

        Intent intent = new Intent(Dashboard.this,ShowAssignedContent.class);
        startActivity(intent);


    }

    @Override
    public void onItemClick(String itemText) {
        switch (itemText) {
            case "Logout":
                logout();
                break;
            case "Website":

                break;
            case "Downloads":
                goToDownloads();
                break;
            case "About":

                break;
            default:
                // Handle other cases if needed
                break;
        }

    }
}