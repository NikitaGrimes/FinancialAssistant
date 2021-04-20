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

    @Insert
    void insert(Currents currents);

    @Update
    void update(Currents currents);

    @Delete
    void delete(Currents currents);

}
