package com.example.househunting.model.preference;

import com.example.househunting.model.RootResponse;
import com.example.househunting.model.auth.User;
import com.google.gson.annotations.SerializedName;

public class UserPreference extends RootResponse {
    @SerializedName("data")
    public User user;
    public UserPreference(int status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
