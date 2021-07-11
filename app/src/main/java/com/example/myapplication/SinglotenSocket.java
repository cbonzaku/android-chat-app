package com.example.myapplication;

import io.socket.client.Socket;

public class SinglotenSocket {
    public static Socket socket;
    public static String username;


    public static void setSocket(Socket passedSocket){
        SinglotenSocket.socket =passedSocket;

    }
    public static Socket getSocket(){
        return SinglotenSocket.socket;
    }
    public static String  getUsername(){
        return SinglotenSocket.username;
    }
}
