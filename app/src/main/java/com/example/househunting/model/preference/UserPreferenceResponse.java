package com.example.househunting.model.preference;

import com.example.househunting.model.RootResponse;
import com.google.gson.annotations.SerializedName;

public class UserPreferenceResponse extends RootResponse {
    @SerializedName("data")
    public Preference preference;

    public Preference getPreference() {
        return preference;
    }

}
