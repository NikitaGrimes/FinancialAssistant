package com.example.financialassistant.modelsDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.financialassistant.models.Currents;

import java.util.GregorianCalendar;

@Entity(foreignKeys = {@ForeignKey(entity = AccountsDB.class, parentColumns = "id", childColumns = "acc_id"),
        @ForeignKey(entity = Currents.class, parentColumns = "Cur_ID", childColumns = "currents_id"),
        @ForeignKey(entity = TypeOfExpDB.class, parentColumns = "id", childColumns = "type_of_exp_id")})
public class ExpDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public long type_of_exp_id;
    public int value;
    public int realValue;
    public long currents_id;
    public long acc_id;
    public GregorianCalendar date_operation;

    public ExpDB() {

    }

    public ExpDB(int _id, long _type_of_exp_id, int _value, int _realValue, long _cur_id, long _acc_id, GregorianCalendar _date) {
        this.id = _id;
        this.type_of_exp_id = _type_of_exp_id;
        this.value = _value;
        this.realValue = _realValue;
        this.currents_id = _cur_id;
        this.acc_id = _acc_id;
        this.date_operation = _date;
    }

    public ExpDB(long _type_of_exp_id, int _value, int _realValue, long _cur_id, long _acc_id, GregorianCalendar _date) {
        this.type_of_exp_id = _type_of_exp_id;
        this.value = _value;
        this.realValue = _realValue;
        this.currents_id = _cur_id;
        this.acc_id = _acc_id;
        this.date_operation = _date;
    }

}
