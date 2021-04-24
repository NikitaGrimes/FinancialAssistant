package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.financialassistant.models.Debts;
import com.example.financialassistant.modelsDB.DebtsDB;

import java.util.List;

@Dao
public interface DebtsDao {

    @Query("select debtsdb.id, debtsdb.name, debtsdb.value, currents.Cur_Abbreviation, debtsdb.deadline, " +
            "debtsdb.isDebtor from debtsdb inner join currents on debtsdb.currents_id = currents.Cur_ID")
    List<Debts> getAll();

    @Query("select debtsdb.id, debtsdb.name, debtsdb.value, currents.Cur_Abbreviation, debtsdb.deadline, " +
            "debtsdb.isDebtor from debtsdb inner join currents on debtsdb.currents_id = currents.Cur_ID " +
            "where debtsdb.id = :id")
    Debts getAccById(long id);

    @Query("SELECT * FROM debtsdb where debtsdb.id = :id")
    DebtsDB getDebtDBById(long id);

    @Query("SELECT id FROM debtsdb where name = :name")
    long getIdByName(String name);

    @Query("DELETE FROM accountsdb")
    void deleteAll();

    @Insert
    long insert(DebtsDB debtsDB);

    @Update
    void update(DebtsDB debtsDB);

    @Delete
    void delete(DebtsDB debtsDB);

}
