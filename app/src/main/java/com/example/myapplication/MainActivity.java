package com.example.myapplication;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Button button;
    private EditText username;
    private  EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);
        button=(Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.password);

      /*  WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }

        windowInsetsController.setAppearanceLightNavigationBars(true);*/





        Toast.makeText(getApplicationContext(),"Hello ma9roud",Toast.LENGTH_SHORT).show();
        connection();

    }

    private void connection(){
        Call<results> call = ChatClient.getInstance().getMyApi().connection();
        call.enqueue(new Callback<results>() {

            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                results greeting = response.body();
                Log.e(TAG,"greeting"
                        +response.body());
               // Toast.makeText(getApplicationContext(),greeting.getGreeting(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<results> call, Throwable t) {
                Log.e("REST","greeting"
                        +t.getMessage());
                Toast.makeText(getApplicationContext(),"404:not found",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(View view){
         String user;
        user = username.getText().toString();
        Log.e("RESTkked","huejdkelde"
                +user);
        LoginBody loginBody = new LoginBody(user,password.getText().toString()) ;
        Call<loginResult> loginBodyCall =  ChatClient.getInstance().getMyApi().login(loginBody);
        loginBodyCall.enqueue(new Callback<loginResult>() {
            @Override
            public void onResponse(Call<loginResult> call, Response<loginResult> response) {
                loginResult lr=response.body();
                //Toast.makeText(getApplicationContext(),lr.getToken(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, UsersDisplay.class);
                intent.putExtra("username1",user);
                SinglotenSocket.username =  user;
                Log.d("MainUser:",""+SinglotenSocket.getUsername());
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<loginResult> call, Throwable t) {

            }
        });

    }
}