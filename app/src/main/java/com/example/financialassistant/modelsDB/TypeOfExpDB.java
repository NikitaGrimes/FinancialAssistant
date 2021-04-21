package com.example.financialassistant.modelsDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.financialassistant.models.Currents;

@Entity(foreignKeys = {@ForeignKey(entity = Currents.class, parentColumns = "Cur_ID", childColumns = "currents_id")})
public class TypeOfExpDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int value;

    public long currents_id;

    public TypeOfExpDB() {

    }

    public TypeOfExpDB(int _id, String _name, int _value, long _cur_id) {
        this.id = _id;
        this.name = _name;
        this.value = _value;
        this.currents_id = _cur_id;
    }

    public TypeOfExpDB(String _name, int _value, long _cur_id) {
        this.name = _name;
        this.value = _value;
        this.currents_id = _cur_id;
    }
}
