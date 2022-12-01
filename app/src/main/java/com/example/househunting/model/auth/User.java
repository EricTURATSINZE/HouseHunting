package com.example.househunting.model.auth;

public class User {

    public String token;

    public User(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
