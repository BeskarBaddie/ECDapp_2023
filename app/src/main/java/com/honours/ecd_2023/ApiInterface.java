package com.honours.ecd_2023;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("content/")
    Call<List<Video>> getAllContent();
}
