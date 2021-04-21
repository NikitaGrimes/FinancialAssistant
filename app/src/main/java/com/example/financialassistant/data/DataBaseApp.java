package com.example.financialassistant.data;

import android.content.Context;

import androidx.room.Room;

import com.example.financialassistant.database.DataBase;

public class DataBaseApp {

    private static DataBase instance;

    private static final String DATABASE_NAME = "FinancialDB";

    public static DataBase getInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context, DataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
