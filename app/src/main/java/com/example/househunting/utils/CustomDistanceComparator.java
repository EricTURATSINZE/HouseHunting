package com.example.househunting.utils;

import com.example.househunting.model.house.Data;

import java.util.Comparator;

public class CustomDistanceComparator implements Comparator<Data> {
    /** Author: David */
    @Override
    public int compare(Data o1, Data o2) {
        return o1.getDistance().compareTo(o2.getDistance());
    }
}
