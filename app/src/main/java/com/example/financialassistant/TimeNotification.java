package com.example.financialassistant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class TimeNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String contentText = intent.getExtras().getString("contentText");
        int Id = intent.getExtras().getInt("id");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FAChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("У Вас есть запланированный платеж")
                .setContentText(contentText)
                .setPriority(PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(Id, builder.build());
    }

}
