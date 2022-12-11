package com.example.househunting.model;

import com.google.gson.annotations.SerializedName;
/**
 * Author: FABRICE IRANKUNDA
 */

public class RootResponse {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
