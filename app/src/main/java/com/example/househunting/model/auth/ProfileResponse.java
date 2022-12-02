package com.example.househunting.model.auth;

import com.example.househunting.model.RootResponse;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse extends RootResponse {
    @SerializedName("data")
    private Profile data;
    public Profile getData() {
        return data;
    }
}
