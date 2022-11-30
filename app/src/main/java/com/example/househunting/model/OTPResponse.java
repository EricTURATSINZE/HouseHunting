package com.example.househunting.model;

import com.google.gson.annotations.SerializedName;

public class OTPResponse {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    public OTPResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
