package com.honours.ecd_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> performLogin());

        if (isUserLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this,Dashboard.class);
            startActivity(intent);
        }

    }

    private void performLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        LoginRequest loginRequest = new LoginRequest(username, password);





        Call<AuthTokenResponse> auth = ApiService.getInterface().login(loginRequest);
        //Call<AuthTokenResponse> auth = ApiService.getInterface().login(loginRequest);

        try{

        auth.enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (response.isSuccessful()) {
                    AuthTokenResponse authTokenResponse = response.body();
                    String authToken = authTokenResponse.getAuthToken();
                    // Store the authToken securely
                    storeCredentials(username, authToken);
                    // Navigate to the main screen
                    Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                    startActivity(intent);

                    Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_LONG).show();
                } else {
                    // Handle login error
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                // Handle network failure
            }
        });
        }catch(Exception ex){
            System.out.println("THE ERROR IS " + ex);
        }

}



    private void storeCredentials(String username, String authToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Encrypt and store the token securely
        editor.putString("auth_token", authToken);
        editor.putString("username", username);

        // Commit the changes
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.contains("auth_token") && sharedPreferences.contains("username");
    }
}

   // private String retrieveAuthToken() {
     //   SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
       // return sharedPreferences.getString("auth_token", null);
    //}


