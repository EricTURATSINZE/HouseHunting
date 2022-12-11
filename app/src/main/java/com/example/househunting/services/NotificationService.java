package com.example.househunting.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.househunting.MainActivity;

import java.util.UUID;
/**
 * Author: FABRICE IRANKUNDA
 */
public class NotificationService {
    private String title;
    private String body;
    private int icon;
    private Context context;

    public NotificationService(String inTitle, String inBody, int inIcon, Context ctx) {
        title = inTitle;
        body = inBody;
        icon = inIcon;
        context = ctx;
    }

    public Notification createNotification(NotificationManager manager, int importance){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            final String CHANNEL_ID = UUID.randomUUID().toString();
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,
                    CHANNEL_ID, importance);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent);
            return builder.build();
        }
        return null;
    }

    public void sendNotification( Notification notification)
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(1, notification);
        }
    }
}
