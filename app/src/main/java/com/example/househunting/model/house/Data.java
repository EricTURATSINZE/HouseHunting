package com.example.househunting.model.house;

import java.util.ArrayList;

public class Data {
    private Location location;
    private OwnerInfo ownerInfo;
    private double ratingsAverage;
    private boolean available;
    private int ratingsQuantity;
    private ArrayList<String> images;
    private boolean visible;
    private String _id;
    private int bedRooms;
    public int priceMonthly;
    private String description;
    private String imageCover;
    private int __v;
    private String id;
    private ArrayList<String> internet;

    public Location getLocation() {
        return location;
    }

    public OwnerInfo getOwnerInfo() {
        return ownerInfo;
    }

    public double getRatingsAverage() {
        return ratingsAverage;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getRatingsQuantity() {
        return ratingsQuantity;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public boolean isVisible() {
        return visible;
    }

    public String get_id() {
        return _id;
    }

    public int getBedRooms() {
        return bedRooms;
    }

    public int getPriceMonthly() {
        return priceMonthly;
    }

    public String getDescription() {
        return description;
    }

    public String getImageCover() {
        return imageCover;
    }

    public int get__v() {
        return __v;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getInternet() {
        return internet;
    }
}
