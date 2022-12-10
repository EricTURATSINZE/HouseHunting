package com.example.househunting.model.house;

import com.example.househunting.model.HouseRegister.HouseLocation;
import com.example.househunting.model.RootResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class CreateHouseResponse extends RootResponse {
    @SerializedName("data")
    private CreatedHouse data;
    public CreatedHouse getData() {
        return data;
    }
}

