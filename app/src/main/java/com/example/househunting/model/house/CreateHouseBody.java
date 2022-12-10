package com.example.househunting.model.house;

import com.example.househunting.model.HouseRegister.HouseLocation;

import java.util.ArrayList;
import java.util.Date;

public class CreateHouseBody {
    public int bedRooms;
    public int priceMonthly;
    public String description;
    public ArrayList<String> internet;
    public ArrayList<String> images;
    public String imageCover;
    public HouseLocation location;
    public OwnerInfo ownerInfo;

    public CreateHouseBody(int bedRooms, int priceMonthly, String description, ArrayList<String> internet, ArrayList<String> images, String imageCover, HouseLocation location, OwnerInfo ownerInfo) {
        this.bedRooms = bedRooms;
        this.priceMonthly = priceMonthly;
        this.description = description;
        this.internet = internet;
        this.images = images;
        this.imageCover = imageCover;
        this.location = location;
        this.ownerInfo = ownerInfo;
    }
}
