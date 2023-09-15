package com.honours.ecd_2023;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("get_all_content/")
    Call<List<Video>> getAllContent(@Header("Authorization")String token);

    @POST("android_login/") // Replace with your API endpoint
    Call<AuthTokenResponse> login(@Body LoginRequest loginRequest);
    //Call<AuthTokenResponse> login(@Body LoginRequest loginRequest);

    @GET("get_assigned_content/")
    Call<List<Video>> getAssignedContent(@Header("Authorization")String token);


}
