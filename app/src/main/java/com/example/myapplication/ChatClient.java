package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//API client
public class ChatClient {
    private static ChatClient instance = null;
    private serviceWEb myApi;

    private ChatClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(serviceWEb.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(serviceWEb.class);
    }
    public static synchronized ChatClient getInstance() {
        if (instance == null) {
            instance = new ChatClient();
        }
        return instance;
    }

    public serviceWEb getMyApi() {
        return myApi;
    }
}
