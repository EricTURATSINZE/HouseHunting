package com.example.househunting.model;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public User user;

    public SignupResponse(int status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
