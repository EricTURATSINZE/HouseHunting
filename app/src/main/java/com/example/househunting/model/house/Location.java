package com.example.househunting.model.house;

import java.util.ArrayList;

public class Location {
    private String type;
    private String address;
    private ArrayList<Double> coordinates;

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }
}
