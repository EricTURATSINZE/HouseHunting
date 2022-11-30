package com.example.househunting.model.house;

import com.google.gson.annotations.SerializedName;

public class ViewHouseResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }
}
