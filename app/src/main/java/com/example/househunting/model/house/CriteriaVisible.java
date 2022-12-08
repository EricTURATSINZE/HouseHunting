package com.example.househunting.model.house;

import java.util.ArrayList;
import java.util.List;

public class CriteriaVisible implements Criteria{
    /** Author: David */
    @Override
    public List<Data> meetCriteria(List<Data> houses) {
        List<Data> visibleHouse = new ArrayList<Data>();

        for (Data data : houses) {
            if(data.isVisible())
            {
                visibleHouse.add(data);
            }
            else
            {
                return null;
            }
        }
        return visibleHouse;
    }
}
