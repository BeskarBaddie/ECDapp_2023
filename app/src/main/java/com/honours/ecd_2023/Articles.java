package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Articles extends AppCompatActivity {

    EditText edit;
    Button upload;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        edit = findViewById(R.id.edittext);
        upload = findViewById(R.id.button_upload_pdf);

        storageReference = FirebaseStorage.getInstance().getReference("Articles");
        databaseReference = FirebaseDatabase.getInstance().getReference("article");
//should be disabled before you click a pdf
        upload.setEnabled(false);


        //upload the pdf
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();

            }
        });

    }

    private void selectPDF() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf file to upload"),101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null){

            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            if(uriString.startsWith("content://")){

                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null,null, null, null);
                    if (cursor != null && cursor.moveToFirst()){
                        displayName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                }finally{
                    cursor.close();
                }
            }else if (uriString.startsWith("file://")){
                displayName = myFile.getName();
            }

            upload.setEnabled(true);
            edit.setText(displayName);

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDF(data.getData());
                }
            });

        }else{
            Toast.makeText(this, "failed to upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPDF(Uri data) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("pdf uploading");
        pd.show();

        final StorageReference reference = storageReference.child("articleUploads"+ System.currentTimeMillis()
        + ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                pdfFile fileinModel = new pdfFile(edit.getText().toString(),uri.toString());
                databaseReference.child(databaseReference.push().getKey()).setValue(fileinModel);
                Toast.makeText(Articles.this, "File uploaded succesfully",Toast.LENGTH_SHORT).show();
                pd.dismiss();


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                float percent = (100* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                pd.setMessage("Uploaded "+ (int) percent + "%");

            }
        });

    }
}