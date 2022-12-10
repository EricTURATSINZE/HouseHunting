package com.example.househunting.model.house;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CriteriaGlobal implements Criteria
{
    /** Author: David */
    String searchString;
    public CriteriaGlobal(String _searchString)
    {
        searchString = _searchString;
    }
    @Override
    public List<Data> meetCriteria(List<Data> houses)
    {
        List<Data> houseList = new ArrayList<Data>();

        for (Data data : houses) {
            if(Integer.toString(data.getBedRooms()).equals(searchString) || data.getLocation().getAddress().toLowerCase(Locale.ROOT).contains(searchString))
            {
                houseList.add(data);
            }
            else
            {
                return null;
            }
        }
        return houseList;
    }
}
