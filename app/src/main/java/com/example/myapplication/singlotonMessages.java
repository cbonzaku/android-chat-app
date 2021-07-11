package com.example.myapplication;

import com.example.myapplication.listener.ArraylistenerC;

import java.util.ArrayList;

public class singlotonMessages  {
    private static singlotonMessages myinstance;
    private  ArrayList<Mmessage> myarray=null;
    private ArrayList<ArraylistenerC> alcs =new ArrayList<>();

    public  static singlotonMessages getInstance(){
        if(myinstance == null){
            myinstance = new singlotonMessages();
        }
        return myinstance;
    }
    private singlotonMessages(){
        myarray=new ArrayList<Mmessage>();
    }

    public ArrayList<Mmessage> getArray() {
        return this.myarray;
    }

    public void addElement(Mmessage m){
        myarray.add(m);
        for (ArraylistenerC alc:alcs){alc.addingElement();}
    }
    public void addListener(ArraylistenerC toAdd) {
        alcs.add(toAdd);
    }

}
