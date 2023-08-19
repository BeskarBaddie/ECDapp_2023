package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.os.SystemClock;
import com.google.firebase.analytics.FirebaseAnalytics;
public class ViewPDF extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    RecyclerView pdfRecyclerView;
    private DatabaseReference pdfReference;
    Query query;
    ProgressBar progressBar;
    String user ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = retrieveUserName();
        setContentView(R.layout.activity_view_pdf);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        displayPDFs();
    }

    private void displayPDFs() {

        pdfReference = FirebaseDatabase.getInstance().getReference().child("article");
        pdfRecyclerView = findViewById(R.id.recyclerview_showPDF);
        pdfRecyclerView.setHasFixedSize(true);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        progressBar = findViewById(R.id.progressBarTwo);
        progressBar.setVisibility(View.VISIBLE);

        query = pdfReference.orderByChild("fileName");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Toast.makeText(ViewPDF.this, "toast 1", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                    showPDF();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ViewPDF.this, ":", Toast.LENGTH_SHORT).show();

                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void showPDF() {

        try {


            FirebaseRecyclerOptions<pdfFile> options = new FirebaseRecyclerOptions.Builder<pdfFile>()
                    .setQuery(query, pdfFile.class).build();


            FirebaseRecyclerAdapter<pdfFile, PDFAdapter> adapter = new FirebaseRecyclerAdapter<pdfFile, PDFAdapter>(options) {
                @Override
                protected void onBindViewHolder(@NonNull PDFAdapter holder, int position, @NonNull pdfFile model) {

                    Toast.makeText(ViewPDF.this, "hello", Toast.LENGTH_SHORT).show();


                    progressBar.setVisibility(View.GONE);
                    holder.pdfTitle.setText(model.getFileName());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setType("application/pdf");
                            intent.setData(Uri.parse(model.getUrl()));
                            startActivity(intent);
                            Bundle params = new Bundle();
                            params.putString("username", user);
                            params.putString(FirebaseAnalytics.Param.ITEM_NAME, "PDF Opened");
                            params.putString("pdf_title", model.getFileName());
                            mFirebaseAnalytics.logEvent("pdf_opened", params);

                        }
                    });


                }

                @NonNull
                @Override


                public PDFAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    //might be item not pdf_file

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_file, parent, false);
                    PDFAdapter holder = new PDFAdapter(view);


                    return holder;
                }
            };

            pdfRecyclerView.setAdapter(adapter);
            adapter.startListening();
        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
    private String retrieveUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }
}