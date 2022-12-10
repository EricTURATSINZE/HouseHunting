package com.example.househunting.model.HouseRegister;

import com.example.househunting.model.house.OwnerInfo;

import java.util.ArrayList;

public class House {
    private int bedrooms;
    private int bathrooms;
    private int priceMonthly;
    private String description;
    private ArrayList<String> images;
    private String imageCover;
    private ArrayList<String> internet;
    private HouseLocation location;
    private OwnerInfo ownerInfo;

    public House() {
        bedrooms = 0;
        bathrooms = 0;
        priceMonthly = 0;
        images = new ArrayList<String>();
        internet = new ArrayList<String>();
        imageCover = null;
        location = new HouseLocation();
        ownerInfo = new OwnerInfo();
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getPriceMonthly() {
        return priceMonthly;
    }

    public void setPriceMonthly(int priceMonthly) {
        this.priceMonthly = priceMonthly;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getInternet() {
        return internet;
    }

    public void setInternet(ArrayList<String> internet) {
        this.internet = internet;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public HouseLocation getLocation() {
        return location;
    }

    public void setLocation(HouseLocation location) {
        this.location = location;
    }

    public OwnerInfo getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(OwnerInfo ownerInfo) {
        this.ownerInfo = ownerInfo;
    }
}
