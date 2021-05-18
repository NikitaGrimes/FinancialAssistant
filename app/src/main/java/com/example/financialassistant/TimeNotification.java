package com.example.financialassistant;

import android.annotation.SuppressLint;
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
import androidx.room.Room;

import com.example.financialassistant.dao.ScheduledPayDao;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.database.DataBase;
import com.example.financialassistant.models.ScheduledPay;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class TimeNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DataBase instance = Room.databaseBuilder(context, DataBase.class, DataBaseApp.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        ScheduledPayDao scheduledPayDao = instance.scheduledPayDao();
        List<ScheduledPay> payList = scheduledPayDao.getAll();
        int Id = intent.getIntExtra("Id", -1);
        ScheduledPay scheduledPayNow = scheduledPayDao.getById(Id);
        if (scheduledPayNow != null) {
            Intent confirmIntent = new Intent(context, NotificationConfirm.class);
            Intent cancelIntent = new Intent(context, NotificationCancel.class);

            confirmIntent.putExtra("Id", scheduledPayNow.id);
            cancelIntent.putExtra("Id", scheduledPayNow.id);

            PendingIntent confirmPendingIntent = PendingIntent.getBroadcast(context, 0, confirmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            @SuppressLint("DefaultLocale") String contentText = scheduledPayNow.getName_acc() + " -> " + scheduledPayNow.getName() +
                    ", " + String.format("%.2f", scheduledPayNow.getValue() / 100.) + " " + scheduledPayNow.getCur_Abbreviation();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FAChannel")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(context.getResources().getString(R.string.youHaveScheduledPay))
                    .setContentText(contentText)
                    .setPriority(PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_launcher_foreground, context.getResources().getString(R.string.confirm),
                            confirmPendingIntent)
                    .addAction(R.drawable.ic_launcher_foreground, context.getResources().getString(R.string.remove),
                            cancelPendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(Id, builder.build());
        }

        long time = System.currentTimeMillis();
        boolean isFind = false;
        int id = -1;
        long shortestTime = Long.MAX_VALUE;
        for (ScheduledPay scheduledPay : payList) {
            if (scheduledPay.getDate_operation().getTimeInMillis() < shortestTime &&
                    scheduledPay.getDate_operation().getTimeInMillis() > time + 1000) {
                id = scheduledPay.id;
                shortestTime = scheduledPay.getDate_operation().getTimeInMillis();
                isFind = true;
            }
        }
        if (isFind) {
            intent.putExtra("Id" , id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, shortestTime,
                    pendingIntent);
        }
    }

}
