package com.example.househunting.constants;

public enum Internet {
    MTN("MTN"),
    CANAL("Canal-Box"),
    LIQUID("Liquid"),
    NONE("None");

    private String value;

    Internet(String incomingVal) {
        value = incomingVal;
    }

    public String getValue() {
        return value;
    }
}
