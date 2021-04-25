package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.modelsDB.AccountsDB;

import java.util.List;

@Dao
public interface AccountsDao {

    @Query("SELECT accountsdb.id, accountsdb.name_acc, typeofaccdb.type, accountsdb.value, currents.Cur_Abbreviation  " +
            "FROM accountsdb inner join currents on accountsdb.currents_id = currents.Cur_ID inner join typeofaccdb " +
            "on accountsdb.type_of_acc_id = typeofaccdb.id")
    List<Accounts> getAll();

    @Query("SELECT accountsdb.id, accountsdb.name_acc, typeofaccdb.type, accountsdb.value, currents.Cur_Abbreviation  " +
            "FROM accountsdb inner join currents on accountsdb.currents_id = currents.Cur_ID inner join typeofaccdb " +
            "on accountsdb.type_of_acc_id = typeofaccdb.id where accountsdb.id = :id")
    Accounts getAccById(long id);

    @Query("SELECT * FROM accountsdb where accountsdb.id = :id")
    AccountsDB getAccDBById(long id);

    @Query("SELECT id FROM accountsdb where name_acc = :name")
    long getIdByName(String name);

    @Query("DELETE FROM accountsdb where id = :id")
    void deleteById(long id);

    @Query("DELETE FROM accountsdb")
    void deleteAll();

    @Insert
    long insert(AccountsDB accountsDB);

    @Update
    void update(AccountsDB accountsDB);

    @Delete
    void delete(AccountsDB accountsDB);

}
