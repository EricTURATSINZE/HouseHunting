package com.example.househunting.model.house;

import com.example.househunting.model.RootResponse;
import com.google.gson.annotations.SerializedName;

public class ViewHouseResponse extends RootResponse {
    @SerializedName("data")
    private Data data;
    public Data getData() {
        return data;
    }
}
