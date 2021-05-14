package com.example.financialassistant;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.financialassistant.dao.ScheduledPayDao;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataScheduledPay;
import com.example.financialassistant.data.DataViews;
import com.example.financialassistant.database.DataBase;
import com.example.financialassistant.models.ScheduledPay;

import java.net.IDN;
import java.util.List;

public class NotificationCancel extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int Id = intent.getIntExtra("Id", -1);
        DataBase instance = Room.databaseBuilder(context, DataBase.class, DataBaseApp.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        ScheduledPayDao scheduledPayDao = instance.scheduledPayDao();
        ScheduledPay scheduledPay = scheduledPayDao.getById(Id);
        if (scheduledPay != null) {
            scheduledPayDao.deleteById(scheduledPay.id);
            if (DataScheduledPay.scheduledPays.size() == 0) {
                List<ScheduledPay> payList = scheduledPayDao.getAll();
                DataScheduledPay.scheduledPays.addAll(payList);
            }
            for (int i = 0; i < DataScheduledPay.scheduledPays.size(); i++) {
                if (DataScheduledPay.scheduledPays.get(i).id == scheduledPay.id) {
                    DataScheduledPay.scheduledPays.remove(i);
                    i--;
                }
            }

            DataScheduledPay.adapter.notifyDataSetChanged();

            if (DataViews.emptyScheduledPay != null) {
                if (DataScheduledPay.scheduledPays.size() == 0) {
                    DataViews.emptyScheduledPay.setVisibility(View.VISIBLE);
                    if (DataScheduledPay.recyclerView != null) {
                        DataScheduledPay.recyclerView.setVisibility(View.GONE);
                    }
                }
            }
        }
        cancelNotification(context, Id);
    }

    public static void cancelNotification(Context context, int notifyId) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(notifyId);
    }
}
