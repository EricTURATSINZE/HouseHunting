package com.example.househunting.model.preference;

public class PriceRange {
    int max;
    int min;

    public PriceRange(int newMax,int newMin)
    {
        max = newMax;
        min = newMin;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    @Override
    public String toString() {
        return "PriceRange{" +
                "max=" + max +
                ", min=" + min +
                '}';
    }
}
