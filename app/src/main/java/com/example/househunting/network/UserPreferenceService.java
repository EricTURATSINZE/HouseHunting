package com.example.househunting.network;
import com.example.househunting.model.preference.Preference;
import com.example.househunting.model.preference.PriceRange;
import com.example.househunting.model.preference.UserPreferenceResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserPreferenceService {
    @POST("users/preferences")
    Call<UserPreferenceResponse> createPreference(@Body Preference preference,
                                            @Header("Authorization") String token
                                            );
    @GET("users/preferences")
    Call<UserPreferenceResponse> getPreferences(@Header("Authorization") String token);
}
