package com.example.househunting.model.HouseRegister;

public class LandLord {
    private String phone;
    private String names;

    public String getPhone() {
        return phone;
    }

    public LandLord(String phone, String names) {
        this.phone = phone;
        this.names = names;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
