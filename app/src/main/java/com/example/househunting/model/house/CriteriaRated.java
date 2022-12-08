package com.example.househunting.model.house;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CriteriaRated implements Criteria
{
    /** Author: David */
    @Override
    public List<Data> meetCriteria(List<Data> houses) {
        List<Data> ratedHouse = new ArrayList<Data>();

        for (Data data : houses) {
            if(data.getRatingsAverage() > 2)
            {
                ratedHouse.add(data);
            }
            else
            {
                return null;
            }
        }
        return ratedHouse;
    }
}
