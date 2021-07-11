package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

public class CustomServise extends Service {

    //RECIEVING A MESSAGE

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("servise:","starting...");
        EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
