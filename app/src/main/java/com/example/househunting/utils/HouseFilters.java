package com.example.househunting.utils;

public enum HouseFilters
{
    WIFI("Wifi"),
    RATED("Rated"),
    NEAREST("Nearest"),
    CHEAPER("Cheaper"),
    UNKNOWN("Unknown!");

    //instance
    private String _text;

    //enum constructor
    HouseFilters(String filter)
    {
        _text = filter;
    }

    //@Override toString function
    public String toString()
    {
        return _text;
    }

    //static function to return enum and handle user choice
    public static HouseFilters fromUserChoice(String choiceValue)
    {
        for(HouseFilters choice : HouseFilters.values())
        {
            if (choiceValue.equalsIgnoreCase(choice._text))
            {
                return choice;

            }
        }
        if(choiceValue.equalsIgnoreCase("wifi"))
        {
            return WIFI;
        }

        if(choiceValue.equalsIgnoreCase("rated"))
        {
            return RATED;
        }
        if(choiceValue.equalsIgnoreCase("nearest"))
        {
            return NEAREST;
        }
        if(choiceValue.equalsIgnoreCase("cheaper"))
        {
            return CHEAPER;
        }
        return UNKNOWN;
    }
    //check invalid choice
    public static boolean isValid(String choiceValue)
    {
        if (fromUserChoice(choiceValue) != UNKNOWN)
            return true;
        return false;
    }
}
