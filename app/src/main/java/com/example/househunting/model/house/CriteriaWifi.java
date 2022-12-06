package com.example.househunting.model.house;

import java.util.ArrayList;
import java.util.List;

public class CriteriaWifi implements Criteria
{
    @Override
    public List<Data> meetCriteria(List<Data> houses)
    {
        List<Data> wifiHouse = new ArrayList<Data>();

        for (Data data : houses) {
            if(!data.getInternet().isEmpty())
            {
                wifiHouse.add(data);
            }
            else
            {
                return null;
            }
        }
        return wifiHouse;
    }
}
