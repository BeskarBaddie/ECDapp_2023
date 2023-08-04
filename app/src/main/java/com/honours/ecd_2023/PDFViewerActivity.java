package com.honours.ecd_2023;
import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class PDFViewerActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        pdfView = findViewById(R.id.pdfView);


        //loadPdfFromAssets("sample.pdf");
        String pdfUrl = getIntent().getExtras().getString("ur");



        if (pdfUrl != null) {
            Toast.makeText(this, "PDF Loading", Toast.LENGTH_SHORT).show();
            //loadPdf(pdfUrl);
            try {
                downloadAndDisplayPdf(pdfUrl);

            }catch (Exception ex){
                Log.e(TAG, "onCreate: ",ex );
            }

        } else {
            // Handle case when PDF URL is not available
            Toast.makeText(this, "No url passed", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAndDisplayPdf(String pdfUrl) {
        new AsyncTask<String, Void, File>() {
            @Override
            protected File doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // Get the input stream from the URL connection
                    InputStream inputStream = connection.getInputStream();

                    // Create a temporary file to store the downloaded PDF
                    File pdfFile = new File(getCacheDir(), "temp.pdf");

                    // Write the content of the input stream to the file
                    OutputStream outputStream = new FileOutputStream(pdfFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Close the streams
                    outputStream.close();
                    inputStream.close();

                    return pdfFile;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(File pdfFile) {
                if (pdfFile != null) {
                    // Load the downloaded PDF using PDFView
                    pdfView.fromFile(pdfFile)
                            .defaultPage(0)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    // PDF loading complete, do additional processing if needed
                                    Toast.makeText(PDFViewerActivity.this, "PDF has loaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .scrollHandle(new DefaultScrollHandle(PDFViewerActivity.this))
                            .load();
                } else {
                    Toast.makeText(PDFViewerActivity.this, "Error downloading PDF", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(pdfUrl);
    }




}
