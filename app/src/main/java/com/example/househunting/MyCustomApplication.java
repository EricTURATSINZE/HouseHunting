package com.example.househunting;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for that stores data application wide
 */

public class MyCustomApplication extends Application {

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Initializing cloudinary configuration
         */
        initConfig();
    }

    private void initConfig()
    {
        Map config = new HashMap();
        config.put("cloud_name", "kuranga");
        config.put("api_key","622489496465415");
        config.put("api_secret","dAogkM6LaPF9S_m9oHKnHquzITA");
        config.put("secure", true);
        MediaManager.init(this, config);
    }
}
