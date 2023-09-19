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

    /**
     * Creates and configures a Retrofit instance for making API requests.
     *
     * @return A Retrofit object.
     */
    private static Retrofit getRetrofit() {
        // Create an HTTP request/response logger for debugging purposes.
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        // Create an OkHttpClient with the logger.
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        // Create a Retrofit instance with base URL, Gson converter, and OkHttpClient.
        //http://bbp-1.cs.uct.ac.za/
        //https://yayb.onrender.com/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://bbp-1.cs.uct.ac.za/") // Replace with your API's base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    /**
     * Get an instance of the API interface for making API requests.
     *
     * @return An instance of the API interface.
     */
    public static ApiInterface getInterface(){
        // Create an instance of the API interface using the Retrofit instance.
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        return apiInterface;
    }
}
