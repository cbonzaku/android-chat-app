package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface serviceWEb {
    //10.0.2.2
    String BASE_URL="http://10.0.2.2:3000";
    @GET("/")
    Call<results> connection();

    @POST("/login")
    Call<loginResult> login(@Body LoginBody loginBody );
}
