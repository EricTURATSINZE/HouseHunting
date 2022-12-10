package com.example.househunting.network;

import com.example.househunting.model.HouseRegister.HouseLocation;
import com.example.househunting.model.house.CreateHouseBody;
import com.example.househunting.model.house.CreateHouseResponse;
import com.example.househunting.model.house.OwnerInfo;
import com.example.househunting.model.house.ViewAllHouseResponse;
import com.example.househunting.model.house.ViewHouseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HouseApiService {
    @GET("houses")
    Call<ViewAllHouseResponse> getAllHouse(@Header("Authorization") String token);

    @GET("houses/{id}")
    Call<ViewHouseResponse> getHouse(@Path("id") String id,
                                     @Header("Authorization") String token
                                     );

    @POST("houses")
    Call<CreateHouseResponse> uploadHouse(@Body CreateHouseBody createHouseBody,
                                          @Header("Authorization") String token);

    @GET("houses/preferred-houses")
    Call<ViewAllHouseResponse> getPreferredHouses(@Header("Authorization") String token
    );
}

