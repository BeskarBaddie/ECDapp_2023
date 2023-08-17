package com.honours.ecd_2023;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;


public class Dashboard extends AppCompatActivity {



    CardView videoCard;
    private FirebaseAnalytics mFirebaseAnalytics;
    CardView assignedContent;

    CardView allContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        assignedContent = findViewById(R.id.card_assigned_content);
        allContent = findViewById(R.id.card_all_content);


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




    }



    public void goToAllContent() {

        Intent intent = new Intent(Dashboard.this,ShowVideo.class);
        startActivity(intent);


    }

    public void goToAssignedContent() {

        Intent intent = new Intent(Dashboard.this,ShowAssignedContent.class);
        startActivity(intent);


    }
}