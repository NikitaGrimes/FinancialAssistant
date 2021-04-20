package com.example.financialassistant.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.financialassistant.dao.CurrentsDao;
import com.example.financialassistant.models.Currents;
import com.example.financialassistant.utils.ConverterDateLong;

@Database(entities = {Currents.class}, version = 1)
@TypeConverters({ConverterDateLong.class})
public abstract class DataBase extends RoomDatabase {
    public abstract CurrentsDao currentsDao();

}
