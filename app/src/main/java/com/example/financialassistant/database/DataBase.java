package com.example.financialassistant.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.financialassistant.dao.AccountsDao;
import com.example.financialassistant.dao.CurrentsDao;
import com.example.financialassistant.dao.ExpDao;
import com.example.financialassistant.dao.TypeOfAccDao;
import com.example.financialassistant.dao.TypeOfExpDao;
import com.example.financialassistant.models.Currents;
import com.example.financialassistant.modelsDB.AccountsDB;
import com.example.financialassistant.modelsDB.ExpDB;
import com.example.financialassistant.modelsDB.TypeOfAccDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;
import com.example.financialassistant.utils.ConverterDateLong;
import com.example.financialassistant.utils.ConverterGrCalLong;

@Database(entities = {Currents.class, TypeOfAccDB.class, AccountsDB.class, TypeOfExpDB.class, ExpDB.class}, version = 1)
@TypeConverters({ConverterDateLong.class, ConverterGrCalLong.class})
public abstract class DataBase extends RoomDatabase {
    public abstract CurrentsDao currentsDao();
    public abstract TypeOfAccDao typeOfAccDao();
    public abstract AccountsDao accountsDao();
    public abstract TypeOfExpDao typeOfExpDao();
    public abstract ExpDao expDao();
}
