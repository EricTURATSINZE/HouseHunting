package com.example.househunting.utils;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import com.example.househunting.model.HouseRegister.House;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for that stores data application wide
 */

public class MyCustomApplication extends Application {

    private House house;

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Initializing cloudinary configuration
         */
        initConfig();
    }

    void initConfig()
    {
        Map config = new HashMap();
        config.put("cloud_name", "kuranga");
        config.put("api_key","622489496465415");
        config.put("api_secret","dAogkM6LaPF9S_m9oHKnHquzITA");
        config.put("secure", true);
        MediaManager.init(this, config);
    }
}
