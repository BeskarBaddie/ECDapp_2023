package com.honours.ecd_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    }

    private void performLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<AuthTokenResponse> auth = ApiService.getInterface().login(loginRequest);

        auth.enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (response.isSuccessful()) {
                    AuthTokenResponse authTokenResponse = response.body();
                    String authToken = authTokenResponse.getAuthToken();
                    // Store the authToken securely
                    // Navigate to the main screen
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    // Handle login error
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                // Handle network failure
            }
        });

}
}


