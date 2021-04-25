package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.ScheduledPay;
import com.example.financialassistant.modelsDB.ExpDB;
import com.example.financialassistant.modelsDB.ScheduledPayDB;

import java.util.List;

@Dao
public interface ScheduledPayDao {

    @Query("SELECT scheduledpaydb.id, scheduledpaydb.value, scheduledpaydb.realValue, scheduledpaydb.date_operation, accountsdb.name_acc," +
            "typeofexpdb.name, currents.Cur_Abbreviation from scheduledpaydb inner join accountsdb on scheduledpaydb.acc_id " +
            "= accountsdb.id inner join typeofexpdb on scheduledpaydb.type_of_exp_id = typeofexpdb.id inner join " +
            "currents on scheduledpaydb.currents_id = currents.Cur_ID")
    List<ScheduledPay> getAll();

    @Query("SELECT scheduledpaydb.id, scheduledpaydb.value, scheduledpaydb.realValue, scheduledpaydb.date_operation, accountsdb.name_acc," +
            "typeofexpdb.name, currents.Cur_Abbreviation from scheduledpaydb inner join accountsdb on scheduledpaydb.acc_id " +
            "= accountsdb.id inner join typeofexpdb on scheduledpaydb.type_of_exp_id = typeofexpdb.id inner join " +
            "currents on scheduledpaydb.currents_id = currents.Cur_ID where scheduledpaydb.id = :id")
    ScheduledPay getById(long id);

    @Query("DELETE FROM scheduledpaydb")
    void deleteAll();

    @Query("DELETE FROM scheduledpaydb where id = :id")
    void deleteById(long id);

    @Query("DELETE FROM scheduledpaydb where type_of_exp_id = :id")
    void deleteByTypeId(long id);

    @Query("DELETE FROM scheduledpaydb where acc_id = :id")
    void deleteByAccId(long id);

    @Insert
    long insert(ScheduledPayDB scheduledPayDB);

    @Update
    void update(ScheduledPayDB scheduledPayDB);

    @Delete
    void delete(ScheduledPayDB scheduledPayDB);
}
