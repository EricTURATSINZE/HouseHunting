package com.example.househunting.network;

import com.example.househunting.model.auth.SignupResponse;
import com.example.househunting.model.preference.PriceRange;
import com.example.househunting.model.preference.UserPreference;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserPreferenceService {
    @POST("users/preferences")
    @FormUrlEncoded
    Call<UserPreference> preference(@Field("priceRange")PriceRange priceRange,
                                         @Field("internet") String internet,
                                         @Field("location") String location,
                                         @Field("numOfBedRooms") int num_of_Bed);
}
