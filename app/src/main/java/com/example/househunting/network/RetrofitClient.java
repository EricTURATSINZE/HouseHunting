package com.example.househunting.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
<<<<<<< HEAD
//    private static final String BASE_URL = "https://house-hunting.onrender.com/api/v1/";
    private static final String BASE_URL = "http://172.29.105.140:5000/api/v1/";
=======
    private static final String BASE_URL = "https://house-hunting.onrender.com/api/v1/";
//private static final String BASE_URL = "http://192.168.1.70:5000/api/v1/";
>>>>>>> 945c3d5f69ca7e43eacf102be878eee6102c256c

    public static Retrofit getClient(String url) {
        if(url.equals("")) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
        else{
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
}

