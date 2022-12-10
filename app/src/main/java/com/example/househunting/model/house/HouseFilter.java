package com.example.househunting.model.house;

import android.os.Build;
import android.widget.Switch;

import androidx.annotation.RequiresApi;

import com.example.househunting.utils.HouseFilters;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public final class HouseFilter
{
    /** Author: David **/
    HouseFilters filter;
    ArrayList<Data> houses;
    public HouseFilter(ArrayList<Data> _houses, HouseFilters _filter)
    {
        filter =_filter;
        houses =_houses;
    }
    public ArrayList<Data> getHouses()
    {
        switch (filter)
        {
            case WIFI:
            {
                houses.removeIf(data -> (data.getInternet() == null));
                return houses;

            }
            case RATED:
            {
                houses.sort(((o1, o2) -> o1.getRatingsAverage().compareTo(o2.getRatingsAverage())));
                return houses;
            }
            case CHEAPER:
            {
                houses.sort(((o1, o2) -> o1.getPriceMonthly().compareTo(o2.getPriceMonthly())));
                return houses;
            }
            default:
                return houses;
        }
    }

}
