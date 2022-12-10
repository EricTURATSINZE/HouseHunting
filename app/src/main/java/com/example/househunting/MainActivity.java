package com.example.househunting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.househunting.fragments.HomeFragment;
import com.example.househunting.fragments.NotificationsFragment;
import com.example.househunting.fragments.PreferencesFragment;
import com.example.househunting.fragments.ProfileFragment;
import com.example.househunting.services.PullPreferredHousesService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout upload_btn;
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        selectedFragment = new Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        upload_btn = findViewById(R.id.btn_upload);

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterHouseFirstStep.class);
                startActivity(intent);
            }
        });

        // Notification Service: Note It will be taken from here!!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!new PullPreferredHousesService().notificationServiceRunning(this)){
                Intent notificationService = new Intent(this, PullPreferredHousesService.class);
                startForegroundService(notificationService);
            }
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId())
                    {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_preferences:
                            selectedFragment = new PreferencesFragment();
                            break;

                        case R.id.nav_notifications:
                            selectedFragment = new NotificationsFragment();
                            break;

                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };


}