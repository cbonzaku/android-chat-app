package com.example.myapplication;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<User> {
    private final Activity context;
    private final List<User> name;
    private final int imgID;

    public CustomAdapter(Activity context,List<User> name,int imgID){

        super(context, R.layout.layout, name);
        this.context=context;
        this.name=name;
        this.imgID=imgID;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.layout, null,true);

        TextView t1 = (TextView) rowView.findViewById(R.id.name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView3);


        t1.setText(name.get(position).username);
        if (name.get(position).notify==R.drawable.red_cercle)
        {imageView.setImageResource(name.get(position).notify);}



        return rowView;

    };
}
