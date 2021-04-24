package com.example.financialassistant.modelsDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.financialassistant.models.Currents;

import java.util.GregorianCalendar;
@Entity(foreignKeys = {@ForeignKey(entity = Currents.class, parentColumns = "Cur_ID", childColumns = "currents_id")})
public class DebtsDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int value;

    public long currents_id;

    public GregorianCalendar deadLine;
    public boolean isDebtor;

    public DebtsDB(){

    }

    public DebtsDB(String _name, int _value, long cur_id, GregorianCalendar _date, boolean _isDebtor) {
        this.name = _name;
        this.value = _value;
        this.currents_id = cur_id;
        this.deadLine = _date;
        this.isDebtor = _isDebtor;
    }

    public DebtsDB(int _id, String _name, int _value, long cur_id, GregorianCalendar _date, boolean _isDebtor) {
        this.id = _id;
        this.name = _name;
        this.value = _value;
        this.currents_id = cur_id;
        this.deadLine = _date;
        this.isDebtor = _isDebtor;
    }
}
