package com.example.househunting.model.auth;

import java.util.Date;

public class Profile {
    public String names;
    public String role;
    public String _id;
    public String email;
    public String phone;
    public String profile;
    public boolean isVerified;
    public boolean isActive;
    public Date createdAt;
    public Date updatedAt;
    public int __v;
    public String otp;
    public Date otpExpires;
    public String id;

    public String getNames() {
        return names;
    }

    public String getRole() {
        return role;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfile() {
        return profile;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public boolean isActive() {
        return isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public String getOtp() {
        return otp;
    }

    public Date getOtpExpires() {
        return otpExpires;
    }

    public String getId() {
        return id;
    }
}
