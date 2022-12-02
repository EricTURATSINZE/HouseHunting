package com.example.househunting.model.auth;

import com.example.househunting.model.RootResponse;
import com.google.gson.annotations.SerializedName;

public class OTPResponse extends RootResponse {

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
