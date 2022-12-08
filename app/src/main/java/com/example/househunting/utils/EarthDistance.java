package com.example.househunting.utils;

import android.location.Location;

public class EarthDistance
{
    /** Author: David */
    public static Integer distance(Location loc1, Location loc2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        double lon1 = 0, lon2 = 0, lat1 = 0, lat2 = 0;
        if(loc1!=null && loc2 != null)
        {
            lon1 = Math.toRadians(loc1.getLongitude());
            lon2 = Math.toRadians(loc2.getLongitude());
            lat1 = Math.toRadians(loc1.getLatitude());
            lat2 = Math.toRadians(loc2.getLatitude());
        }


        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return Math.toIntExact((Math.round(c * r)));
    }
}
