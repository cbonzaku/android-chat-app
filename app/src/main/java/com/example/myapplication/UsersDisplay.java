package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class UsersDisplay extends AppCompatActivity {

    private Button addButton;
    private ListView displayer;
    private TextView usernameDisplay;
    private Socket mSocket;
    int imgID = R.drawable.red_cercle;
    CustomAdapter adapter;


    {
        try {
            SinglotenSocket.socket = IO.socket("http://10.0.2.2:3000");
            Log.d("id","socketID"+SinglotenSocket.socket.id());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean refrech = false;
    List<User> users = new ArrayList<>();


    //RECIEVING A MESSAGE
    private Emitter.Listener recieveMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            UsersDisplay.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String message = jsonObject.optString("message");
                    String senderID = jsonObject.optString("id");
                    String sender = jsonObject.optString("from");
                    String reciverID = jsonObject.optString("to");
                    Mmessage m1 = new Mmessage(message, senderID, sender, reciverID);
                    Log.d("hello again", "" + m1.s);
                    singlotonMessages.getInstance().addElement(m1);


                    for (int i = 0; i < users.size(); i++) {

                        if (users.get(i).username.equals(sender)) {
                            users.get(i).getNotification();

                        }
                    }

                    adapter = new CustomAdapter(UsersDisplay.this, users,
                            imgID);
                    displayer.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            });
        }
    };


    //GETTING CONNECTED USERS
    private Emitter.Listener onGetUsers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            UsersDisplay.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    try {
                        if (data != null) {
                            users.clear();

                            String intentdata = getIntent().getStringExtra("username1");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                User u1 = new User(jsonObject1.optString("userID"),
                                        jsonObject1.optString("username"));
                                if (!(intentdata.equals(u1.username))) {
                                    users.add(u1);
                                }

                            }
                            displayer.setAdapter(null);
                            adapter = new CustomAdapter(UsersDisplay.this, users,
                                    imgID);
                            displayer.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersdisplay);
        SinglotenSocket.socket.on("users", onGetUsers);
        SinglotenSocket.socket.on("sendMessage", recieveMessage);
        SinglotenSocket.socket.connect();


        addButton = (Button) findViewById(R.id.button);
        displayer = (ListView) findViewById(R.id.displayer1);


        //SinglotenSocket.setSocket(mSocket);
        String user = getIntent().getStringExtra("username1");

        //getSupportActionBar().setTitle(user);



        SinglotenSocket.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                SinglotenSocket.socket.emit("user", user, SinglotenSocket.socket.id());
            }

        });

        displayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users.get(position).deleteNotification();
                adapter = new CustomAdapter(UsersDisplay.this, users,
                        imgID);
                displayer.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                SinglotenSocket.socket.off("sendMessage", recieveMessage);
                Intent socket_data = new Intent(UsersDisplay.this, talking.class);
                socket_data.putExtra("targetUsername", users.get(position).username);
                Log.d("hello again S2", "" + users.get(position).username);
                socket_data.putExtra("targetID", users.get(position).id);
                startActivity(socket_data);
            }
        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }


    public void addconv(View view) {
        stopService(new Intent(this, CustomServise.class));
        Intent intent = new Intent(UsersDisplay.this, newConversation.class);
        startActivity(intent);

    }

}