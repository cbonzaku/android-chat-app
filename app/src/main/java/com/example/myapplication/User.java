package com.example.myapplication;

import com.example.myapplication.listener.UserNotificationListener;

import java.util.ArrayList;

public class User {
    String id;
    String username;
    int notify=R.drawable.transparant;
    ArrayList<UserNotificationListener> unl= new ArrayList<>();

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }



    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public void getNotification(){
        this.notify=R.drawable.red_cercle;
        for (UserNotificationListener u:unl){
            u.messageRecived();
        }

    }
    public void deleteNotification(){
        this.notify=R.drawable.transparant;
    }

    public void addListener(UserNotificationListener u){
        unl.add(u);
    }
}

