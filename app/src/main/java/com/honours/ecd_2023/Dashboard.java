package com.honours.ecd_2023;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;


public class Dashboard extends AppCompatActivity {

    Button buttonVideo;
    Button buttonArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        buttonVideo = findViewById(R.id.buttonVideos);
        buttonArticles = findViewById(R.id.buttonArticles);




        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToVideoContent();

            }
        });

        buttonArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToArticles();

            }
        });



    }



    public void goToVideoContent() {

        Intent intent = new Intent(Dashboard.this,VideoContent.class);
        startActivity(intent);


    }

    public void goToArticles() {

        Intent intent = new Intent(Dashboard.this,Articles.class);
        startActivity(intent);


    }
}