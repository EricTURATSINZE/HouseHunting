package com.example.househunting.network;

import com.example.househunting.model.SignupResponse;
import com.example.househunting.model.house.ViewHouseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @POST("users/signup")
    @FormUrlEncoded
    Call<SignupResponse>signup(@Field("names") String names,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("phone") String phone);

    @GET("houses/{id}")
    Call<ViewHouseResponse> getHouse(@Url String url,
                                     @Path("id") String id,
                                     @Header("Bearer ") String token
                                     );
}

