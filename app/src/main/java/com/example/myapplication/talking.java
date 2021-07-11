package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class talking extends AppCompatActivity {
    private EditText editText;
    private ImageButton imageButton;
    private LinearLayout linearLayout;
    private ScrollView  scrollView;



    private Emitter.Listener recieveMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            talking.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject= (JSONObject) args[0];
                    String message = jsonObject.optString("message");
                    String ID = jsonObject.optString("id");
                    String sender = jsonObject.optString("from");
                    String reciverID = jsonObject.optString("to");
                    Log.d("from",""+sender);
                    Mmessage m2=new Mmessage(message,ID,sender,reciverID);
                    singlotonMessages.getInstance().getArray().add(m2);
                    displayRcivedMessage(m2.mes);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_talking);


        editText = (EditText) findViewById(R.id.messageToSend);
        imageButton= (ImageButton) findViewById(R.id.imageButton);
        linearLayout=(LinearLayout) findViewById(R.id.layout1);
        scrollView = (ScrollView) findViewById(R.id.s);


        SinglotenSocket.socket.on("sendMessage",recieveMessage);
        String targetUsername = getIntent().getStringExtra("targetUsername");
        String targetID = getIntent().getStringExtra("targetID");


        getSupportActionBar().setTitle(targetUsername);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#A8AB85"));

        // Set BackgroundDrawable
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        Log.d("targetUsername ","sender:"+targetUsername);

        for (int i=0;i<singlotonMessages.getInstance().getArray().size();i++){
            Log.d("messages","sender:"+singlotonMessages.getInstance().getArray().get(i).s
                    +"message:"+singlotonMessages.getInstance().getArray().get(i).mes);
            if(singlotonMessages.getInstance().getArray().get(i).s.equals(targetUsername)){
                displayRcivedMessage(singlotonMessages.getInstance().getArray().get(i).mes);
            }else
            if(singlotonMessages.getInstance().getArray().get(i).s.equals(SinglotenSocket.getUsername()) & singlotonMessages.getInstance().getArray().get(i).to.equals(targetID)) {
                displaySendedMessage(singlotonMessages.getInstance().getArray().get(i).mes);
            }}
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });


    }



    public void sendmessage(View view){
        if(!editText.getText().toString().equals("")) {String message = editText.getText().toString();
        editText.setText(null);
        String targetUsername = getIntent().getStringExtra("targetUsername");
        String targetID = getIntent().getStringExtra("targetID");
        Mmessage m=new Mmessage(message,SinglotenSocket.getSocket().id(),SinglotenSocket.getUsername(),targetID);
        singlotonMessages.getInstance().getArray().add(m);
        SinglotenSocket.socket.emit("sendMessage",message,targetID,targetUsername);
        displaySendedMessage(message);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });}

    }

    public void displaySendedMessage(String message){
        TextView txt1 = (TextView) View.inflate(this,R.layout.textveiw,null);
        txt1.setText(message);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.RIGHT;
        params.setMargins(20,20,0,20);

        GradientDrawable shape =new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.BLUE);
        shape.setStroke(3, Color.YELLOW);

        shape.setCornerRadius(40);
        txt1.setLayoutParams(params);
        txt1.setBackground(shape);
        txt1.setGravity(Gravity.CENTER_VERTICAL);
        txt1.setTextSize(20);

        linearLayout.addView(txt1);
    }

    public void displayRcivedMessage(String message){
        TextView txt = (TextView) View.inflate(talking.this, R.layout.recived1, null);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity=Gravity.LEFT;
        p.setMargins(0,20,20,20);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(Color.LTGRAY);
        gd.setCornerRadius(40);

        txt.setLayoutParams(p);
        txt.setBackgroundDrawable(gd);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(message);
        linearLayout.addView(txt);


    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        SinglotenSocket.socket.off("sendMessage",recieveMessage);
    }

}