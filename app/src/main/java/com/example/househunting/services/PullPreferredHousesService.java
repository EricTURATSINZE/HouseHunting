package com.example.househunting.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import java.lang.reflect.Type;
import androidx.annotation.Nullable;

import com.example.househunting.R;
import com.example.househunting.model.house.Data;
import com.example.househunting.model.house.ViewAllHouseResponse;
import com.example.househunting.network.HouseApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Author: FABRICE IRANKUNDA
 */
public class PullPreferredHousesService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Storage storage = new Storage(getApplication());
                        String token = storage.getToken();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<String>>(){}.getType();
                        while (true) {
                            RetrofitClient.getClient("").create(HouseApiService.class)
                                    .getPreferredHouses(token)
                                    .enqueue(new Callback<ViewAllHouseResponse>() {
                                        @Override
                                        public void onResponse(Call<ViewAllHouseResponse> call, Response<ViewAllHouseResponse> response) {
                                           ArrayList<Data> houses = response.body().getData();
                                           ArrayList<String> seenHouses = gson.fromJson(storage.getSeenHouses(), type);
                                           ArrayList<String> ids = new ArrayList<>();
                                            if(houses.size() > 0){
                                                if(seenHouses != null){
                                                    for(Data data: houses){
                                                        if(!seenHouses.contains(data.get_id()))
                                                            ids.add(data.get_id());
                                                    }
                                                    if(ids.size() > 0){
                                                        prepareAndNotifyUser();
                                                        seenHouses.addAll(ids);
                                                        storage.setSeenHouses(gson.toJson(seenHouses));
                                                    }
                                                }else {
                                                    ArrayList<String> houseIds = new ArrayList<>();
                                                    for(Data data: houses){
                                                        houseIds.add(data.get_id());
                                                    }
                                                    storage.setSeenHouses(gson.toJson(houseIds));
                                                    prepareAndNotifyUser();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<ViewAllHouseResponse> call, Throwable t) {

                                        }
                                    });
                            try {
                                Thread.sleep(120000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                final String CHANNEL_ID = UUID.randomUUID().toString();
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_ID,
                        NotificationManager.IMPORTANCE_LOW
                );

                getSystemService(NotificationManager.class).createNotificationChannel(channel);
                Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                        .setContentText(getString(R.string.foreground))
                        .setContentTitle(getString(R.string.loading))
                        .setSmallIcon(R.drawable.ic_notifications);

                startForeground(1001, notification.build());
            }

        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean notificationServiceRunning(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (PullPreferredHousesService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void prepareAndNotifyUser(){
        NotificationService service = new NotificationService(getString(R.string.notificationTitle),
                getString(R.string.notificationBody), R.drawable.ic_notifications, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            service.sendNotification(service.createNotification(getSystemService(NotificationManager.class), NotificationManager.IMPORTANCE_DEFAULT));
        }
    }
}
