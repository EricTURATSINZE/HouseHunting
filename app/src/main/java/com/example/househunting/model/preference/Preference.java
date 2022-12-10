package com.example.househunting.model.preference;

import java.util.ArrayList;

public class Preference {
    private PriceRange priceRange;
    private ArrayList<String> internet;
    private String location;
    private int numOfBedRooms;

    private Preference(final PreferenceBuilder builder) {
        internet = builder.internet;
        location = builder.location;
        numOfBedRooms = builder.numOfBedRooms;
        priceRange = builder.priceRange;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public ArrayList<String> getInternet() {
        return internet;
    }

    public String getLocation() {
        return location;
    }

    public int getNumOfBedRooms() {
        return numOfBedRooms;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "priceRange=" + priceRange +
                ", internet=" + internet +
                ", location='" + location + '\'' +
                ", numOfBedRooms=" + numOfBedRooms +
                '}';
    }

    public static class PreferenceBuilder {
        private PriceRange priceRange;
        private ArrayList<String> internet;
        private String location;
        private int numOfBedRooms;

        public PreferenceBuilder(){
        }

        public PreferenceBuilder(PriceRange inPriceRange, ArrayList<String> inInternet, String inLocation, int inNumOfBedRooms){
            this.priceRange = inPriceRange;
            this.internet = inInternet;
            this.location = inLocation;
            numOfBedRooms = inNumOfBedRooms;
        }

        public PreferenceBuilder setPriceRange(PriceRange inPriceRange) {
            priceRange = inPriceRange;
            return this;
        }

        public PreferenceBuilder setInternet(ArrayList<String> inInternet) {
            internet = inInternet;
            return this;
        }

        public PreferenceBuilder setLocation(String inLocation) {
            location = inLocation;
            return this;
        }

        public PreferenceBuilder setNumOfBedRooms(int inNumOfBedRooms) {
            numOfBedRooms = inNumOfBedRooms;
            return this;
        }


        public Preference create() {
            return new Preference(this);
        }
    }
}
