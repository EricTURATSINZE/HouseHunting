package com.example.househunting.model.house;

import android.location.Location;

import com.example.househunting.utils.EarthDistance;

import java.util.ArrayList;
import java.util.List;

public class CriteriaNearest implements Criteria{
    /** Author: David */
    private Location location;
    public CriteriaNearest(Location loc)
    {
        location = loc;
    }
    @Override
    public List<Data> meetCriteria(List<Data> houses) {
        List<Data> nearestHouse = new ArrayList<Data>();

        for (Data data : houses) {
            Location loc = new Location("dummyprovider");

            loc.setLatitude(data.getLocation().getCoordinates().get(0));
            loc.setLongitude(data.getLocation().getCoordinates().get(1));

            int distance = EarthDistance.distance(location, loc);
            data.setDistance(distance);
            nearestHouse.add(data);
        }
        return nearestHouse;
    }
}
