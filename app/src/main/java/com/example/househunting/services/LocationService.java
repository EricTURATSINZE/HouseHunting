package com.example.househunting.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationService {
    static Location location;

    LocationService(Location loc) {
        location = new Location(loc);
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location loc) {
        location = loc;
    }

    public static Location getCurrentLocation(Context mContext, Activity currentActivity, int REQUEST_LOCATION)
    {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location loc=null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestLocationPermission(currentActivity, REQUEST_LOCATION);
        }
        else
        {
            loc = lm.getLastKnownLocation("gps");
//            location = new Location(loc);
        }
        return loc;
    }



    public static void requestLocationPermission(Activity currentActivity, int LOCATION_PERMISSION_CODE) {
        ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }
}
