package com.example.househunting.network;

import com.example.househunting.model.auth.OTPResponse;
import com.example.househunting.model.auth.ProfileResponse;
import com.example.househunting.model.auth.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("users/signup")
    @FormUrlEncoded
    Call<SignupResponse>signup(@Field("names") String names,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("phone") String phone);

    @POST("users/verifyEmail")
    @FormUrlEncoded
    Call<OTPResponse>verifyEmail(@Field("otp") String otp,
                               @Header("Authorization") String token);

    @GET("users/resendOTP")
    Call<OTPResponse>resendOtp(@Header("Authorization") String token);

    @GET("users/me")
    Call<ProfileResponse>getMe(@Header("Authorization") String token);

    @POST("users/login")
    @FormUrlEncoded
    Call<SignupResponse>login(@Field("email") String email,
                               @Field("password") String password);
}

