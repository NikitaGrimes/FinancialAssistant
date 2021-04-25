package com.example.financialassistant.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.TypeOfExpDB;

import java.util.List;

@Dao
public interface TypeOfExpDao {

    @Query("SELECT typeofexpdb.id, typeofexpdb.name, typeofexpdb.value, currents.Cur_Abbreviation " +
            "FROM typeofexpdb inner join currents on typeofexpdb.currents_id = currents.Cur_ID where id != -1 and id != -2")
    List<TypeOfExpenses> getAllExpType();

    @Query("SELECT * FROM typeofexpdb where id = :id")
    TypeOfExpDB getTypeExpDBById(long id);

    @Query("SELECT id FROM typeofexpdb where name = :name")
    long getIdByName(String name);

    @Query("delete from typeofexpdb where id = :id")
    void deleteById(long id);

    @Insert
    long insert(TypeOfExpDB typeOfExpDb);

    @Update
    void update(TypeOfExpDB typeOfExpDb);

    @Delete
    void delete(TypeOfExpDB typeOfExpDb);
}
