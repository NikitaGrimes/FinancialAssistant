package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financialassistant.models.Currents;

import java.util.List;

@Dao
public interface CurrentsDao {

    @Query("SELECT * FROM currents")
    List<Currents> getAll();

    @Query("SELECT * FROM currents WHERE Cur_ID = :id")
    Currents getById(long id);

    @Query("DELETE FROM currents")
    void deleteAll();

    @Query("SELECT * FROM currents WHERE Cur_Abbreviation = :abr")
    Currents getCurByAbr(String abr);

    @Query("SELECT Cur_ID FROM currents WHERE Cur_Abbreviation = :abr")
    long getIdByAbr(String abr);

    @Insert
    void insert(Currents currents);

    @Update
    void update(Currents currents);

    @Delete
    void delete(Currents currents);

}
