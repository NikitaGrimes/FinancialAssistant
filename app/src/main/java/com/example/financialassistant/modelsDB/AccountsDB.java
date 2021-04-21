package com.example.financialassistant.modelsDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.financialassistant.models.Currents;

@Entity(foreignKeys = {@ForeignKey(entity = TypeOfAccDB.class, parentColumns = "id", childColumns = "type_of_acc_id"),
        @ForeignKey(entity = Currents.class, parentColumns = "Cur_ID", childColumns = "currents_id")})
public class AccountsDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name_acc;
    public int value;

    public long currents_id;

    public long type_of_acc_id;

    public AccountsDB() {

    }

    public AccountsDB(int _id, String _name, int _value, long _cur_id, long _type_id) {
        this.id = _id;
        this.name_acc = _name;
        this.value = _value;
        this.currents_id = _cur_id;
        this.type_of_acc_id = _type_id;
    }

    public AccountsDB(String _name, int _value, long _cur_id, long _type_id) {
        this.name_acc = _name;
        this.value = _value;
        this.currents_id = _cur_id;
        this.type_of_acc_id = _type_id;
    }
}
