package com.example.househunting.utils;

/**
 * Author: FABRICE IRANKUNDA
 */
import android.location.Location;

public class CalculateDistance {
    public static float getDistance(Double lat, Double lon){
        Double cmuLat = -1.934575115228372;
        Double cmuLon = 30.158916463189836;
        float[] results = new float[1];
        Location.distanceBetween(lat, lon, cmuLat, cmuLon, results);

        float distance = Math.round(results[0] / 1000);
        return distance;
    }
}
