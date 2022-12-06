package com.example.househunting.model.house;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.househunting.adapter.HouseAdapter;
import com.example.househunting.network.ApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Database {
    private static ArrayList<Data> houseList;
    private static Context context;

    public static void fetchAllData() {
        Storage storage = new Storage(context);
        String token = storage.getToken();
        RetrofitClient.getClient("").create(ApiService.class)
                .getAllHouse(token)
                .enqueue(new Callback<ViewAllHouseResponse>() {
                    @Override
                    public void onResponse(Call<ViewAllHouseResponse> call, Response<ViewAllHouseResponse> response) {
                        if (response.code() == 200) {
                            houseList = response.body().getData();
                        }
                    }

                    @Override
                    public void onFailure(Call<ViewAllHouseResponse> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

/** End of david edit */
    }

    public static ArrayList<Data> returnAllHouse()
    {
        fetchAllData();
        return houseList;
    }

}
