package com.example.myapplication;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
public class Chat {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://127.0.0.1:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}


