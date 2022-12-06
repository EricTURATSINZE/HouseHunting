package com.example.househunting;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
//                startActivity(new Intent(SplashScreen.this, SignUp.class));

            }
        }, 2000L);


    }

}
