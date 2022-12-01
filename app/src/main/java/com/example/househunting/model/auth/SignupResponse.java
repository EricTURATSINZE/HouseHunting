package com.example.househunting.model.auth;

import com.example.househunting.model.RootResponse;
import com.google.gson.annotations.SerializedName;

public class SignupResponse extends RootResponse {
    @SerializedName("data")
    public User user;
    public SignupResponse(int status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
