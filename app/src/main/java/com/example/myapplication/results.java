package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class results {
    @SerializedName("greeting")
    public String greeting;

    public results(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }
}
