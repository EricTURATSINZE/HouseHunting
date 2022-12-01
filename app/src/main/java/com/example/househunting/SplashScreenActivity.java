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
        checkAuth(isLoggedIn);
    }

    public void checkAuth(boolean isLoggedIn) {
        if(isLoggedIn) {
            navigate(new Intent(SplashScreenActivity.this, MainActivity.class));
        } else {
            String token = storage.getToken();

            if(token.equals("")) {
                navigate(new Intent(SplashScreenActivity.this, SignUpActivity.class));
            } else {
                navigate(new Intent(SplashScreenActivity.this, VerifyEmailActivity.class));
            }
        }
    }

    public void navigate(Intent intent) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 1000L);
    }
}
