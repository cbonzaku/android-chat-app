package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class loginResult {
    @SerializedName("token")
    String token;

    public loginResult(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
