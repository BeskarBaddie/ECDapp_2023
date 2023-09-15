package com.honours.ecd_2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements ButtonAdapter.OnItemClickListener {

    // Declare UI components
    CardView videoCard;
    private FirebaseAnalytics mFirebaseAnalytics;
    CardView assignedContent;
    Button btnLogout;
    CardView allContent;
    private String clickedItemText;

    // RecyclerView components
    RecyclerView rv;
    ArrayList<String> dataSource;
    LinearLayoutManager linearLayoutManager;
    ButtonAdapter buttonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize UI elements
        assignedContent = findViewById(R.id.card_assigned_content);
        allContent = findViewById(R.id.card_all_content);
        rv = findViewById(R.id.horizontal_recycler_view);

        // Initialize the data source for RecyclerView
        dataSource = new ArrayList<>();
        dataSource.add("Downloads");
        dataSource.add("Website");
        dataSource.add("Contact Us");
        dataSource.add("Logout");

        // Initialize RecyclerView components
        linearLayoutManager = new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false);
        buttonAdapter = new ButtonAdapter(dataSource, this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(buttonAdapter);

        // Set click listeners for card views
        allContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAllContent();
                // Log an event when all content is clicked
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
    }

    // Navigate to the Downloads screen
    public void goToDownloads() {
        Intent intent = new Intent(Dashboard.this, VideoListActivity.class);
        startActivity(intent);
    }

    // Handle item click events
    @Override
    public void onItemClick(String itemText) {
        switch (itemText) {
            case "Logout":
                logout();
                break;
            case "Website":
                openWebsite();
                break;
            case "Downloads":
                goToDownloads();
                break;
            case "Contact Us":
                openContact();
                break;
            default:
                // Handle other cases if needed
                break;
        }
    }

    // Handle the logout action
    private void logout() {
        // Clear the stored credentials
        clearCredentials();

        // Navigate back to the login screen
        Intent intent = new Intent(Dashboard.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Prevents the user from going back to the dashboard using the back button
    }

    // Open the website in a browser
    private void openWebsite() {
        String url = "https://bhabhisana.org.za/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    // Open the contact page in a browser
    private void openContact() {
        String url = "https://bhabhisana.org.za/contact-us/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    // Clear stored credentials
    private void clearCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove the stored credentials
        editor.remove("auth_token");
        editor.remove("username");

        // Commit the changes
        editor.apply();
    }

    // Navigate to the All Content screen
    public void goToAllContent() {
        Intent intent = new Intent(Dashboard.this, ShowVideo.class);
        startActivity(intent);
    }

    // Navigate to the Assigned Content screen
    public void goToAssignedContent() {
        Intent intent = new Intent(Dashboard.this, ShowAssignedContent.class);
        startActivity(intent);
    }
}
