package com.honours.ecd_2023;

import com.honours.ecd_2023.Video;

import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import okhttp3.OkHttpClient;
import okhttp3.*;


public class ApiService {

    private static Retrofit getRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://yayb.onrender.com/") // Replace with your Django API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static ApiInterface getInterface(){
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        return apiInterface;
    }

}

