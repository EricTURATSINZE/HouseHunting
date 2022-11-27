package com.example.househunting.network;

import com.example.househunting.model.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("users/signup")
    @FormUrlEncoded
    Call<SignupResponse>signup(@Field("names") String names,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("phone") String phone);
}

