package com.example.househunting;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.househunting.utils.Storage;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        storage = new Storage(this);

        boolean isLoggedIn = storage.isLoggedIn();

        navigate(isLoggedIn);

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashScreenActivity.this, VerifyEmailActivity.class));
//            }
//        }, 2000L);
    }

    public void navigate(boolean isLoggedIn) {
        if(isLoggedIn) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        } else {
            String token = storage.getToken();

            if(token.equals("")) {
                startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
            } else {
                startActivity(new Intent(SplashScreenActivity.this, VerifyEmailActivity.class));
            }
        }
    }
}
