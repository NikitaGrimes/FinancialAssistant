package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.modelsDB.AccountsDB;
import com.example.financialassistant.modelsDB.ExpDB;

import java.util.GregorianCalendar;
import java.util.List;

@Dao
public interface ExpDao {

    @Query("SELECT expdb.id, expdb.value, expdb.realValue, expdb.date_operation, accountsdb.name_acc," +
            "typeofexpdb.name, currents.Cur_Abbreviation from expdb inner join accountsdb on expdb.acc_id " +
            "= accountsdb.id inner join typeofexpdb on expdb.type_of_exp_id = typeofexpdb.id inner join " +
            "currents on expdb.currents_id = currents.Cur_ID")
    List<Expenses> getAll();

    @Query("SELECT expdb.id, expdb.value, expdb.realValue, expdb.date_operation, accountsdb.name_acc," +
            "typeofexpdb.name, currents.Cur_Abbreviation from expdb inner join accountsdb on expdb.acc_id " +
            "= accountsdb.id inner join typeofexpdb on expdb.type_of_exp_id = typeofexpdb.id inner join " +
            "currents on expdb.currents_id = currents.Cur_ID ORDER BY date_operation DESC limit 20")
    List<Expenses> getLast20();

    @Query("SELECT expdb.id, expdb.value, expdb.realValue, expdb.date_operation, accountsdb.name_acc," +
            "typeofexpdb.name, currents.Cur_Abbreviation from expdb inner join accountsdb on expdb.acc_id " +
            "= accountsdb.id inner join typeofexpdb on expdb.type_of_exp_id = typeofexpdb.id inner join " +
            "currents on expdb.currents_id = currents.Cur_ID where expdb.date_operation > :time")
    List<Expenses> getByTime(GregorianCalendar time);

    @Query("SELECT expdb.id, expdb.value, expdb.realValue, expdb.date_operation, accountsdb.name_acc," +
            "typeofexpdb.name, currents.Cur_Abbreviation from expdb inner join accountsdb on expdb.acc_id " +
            "= accountsdb.id inner join typeofexpdb on expdb.type_of_exp_id = typeofexpdb.id inner join " +
            "currents on expdb.currents_id = currents.Cur_ID where expdb.id = :id")
    Expenses getById(long id);

    @Query("DELETE FROM expdb")
    void deleteAll();

    @Query("DELETE FROM expdb where id = :id")
    void deleteById(long id);

    @Query("DELETE FROM expdb where type_of_exp_id = :id")
    void deleteByTypeId(long id);

    @Query("DELETE FROM expdb where acc_id = :id")
    void deleteByAccId(long id);

    @Insert
    long insert(ExpDB expDB);

    @Update
    void update(ExpDB expDB);

    @Delete
    void delete(ExpDB expDB);
}
