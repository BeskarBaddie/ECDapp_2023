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
    CardView articlesCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        articlesCard = findViewById(R.id.cardArticle);
        videoCard = findViewById(R.id.cardContent);

        videoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToVideoContent();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "video_card");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "video_button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        articlesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToArticles();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "articles_card");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "articles_button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });




    }



    public void goToVideoContent() {

        Intent intent = new Intent(Dashboard.this,ShowVideo.class);
        startActivity(intent);


    }

    public void goToArticles() {

        Intent intent = new Intent(Dashboard.this,Articles.class);
        startActivity(intent);


    }
}