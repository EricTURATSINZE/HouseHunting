package com.example.househunting.model.house;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewAllHouseResponse
{
    @SerializedName("status")
    private int status;

    @SerializedName("results")
    private int results;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<Data> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
