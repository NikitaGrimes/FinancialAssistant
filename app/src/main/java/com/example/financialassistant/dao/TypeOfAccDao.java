package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financialassistant.modelsDB.TypeOfAccDB;

import java.util.List;

@Dao
public interface TypeOfAccDao {

    @Query("SELECT * FROM typeofaccdb")
    List<TypeOfAccDB> getAll();

    @Insert
    void insert(TypeOfAccDB typeOfAccDb);

    @Update
    void update(TypeOfAccDB typeOfAccDb);

    @Delete
    void delete(TypeOfAccDB typeOfAccDb);
}
