package com.honours.ecd_2023;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("get_all_content/")
    Call<List<Video>> getAllContent();

    @POST("android_login/") // Replace with your API endpoint
    Call<AuthTokenResponse> login(@Body LoginRequest loginRequest);


}
