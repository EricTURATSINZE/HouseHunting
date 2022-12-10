package com.example.househunting.model.HouseRegister;

import java.util.ArrayList;

public class HouseLocation {
    public String type;
    private String address;
    private double[] coordinates;

    public HouseLocation() {
        type = null;
        address = null;
        coordinates = null;
    }

    public HouseLocation(String houseAddress, double[] houseCoordinates) {
        address = houseAddress;
        coordinates = houseCoordinates;
    }

    public HouseLocation(String inType, String houseAddress, double[] houseCoordinates) {
        type = inType;
        address = houseAddress;
        coordinates = houseCoordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
