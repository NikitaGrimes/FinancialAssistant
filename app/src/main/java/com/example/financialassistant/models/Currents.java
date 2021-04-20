package com.example.financialassistant.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Currents {

    @PrimaryKey(autoGenerate = true)
    private int Cur_ID;


    private Date LastDate;
    private String Cur_Abbreviation;
    private String Cur_Name;
    private double Cur_OfficialRate;

    public Currents(){

    }

    public Currents(String cur_ID, Date date, String cur_Abbreviation, String cur_Name, double cur_OfficialRate){
        Cur_ID = Integer.parseInt(cur_ID);
        LastDate = date;
        Cur_Abbreviation = cur_Abbreviation;
        Cur_Name = cur_Name;
        Cur_OfficialRate = cur_OfficialRate;
    }

    public int getCur_ID(){
        return Cur_ID;
    }

    public void setCur_ID(int cur_ID) {
        Cur_ID = cur_ID;
    }

    public void setCur_ID(String cur_ID) {
        Cur_ID = Integer.parseInt(cur_ID);
    }

    public Date getLastDate(){
        return LastDate;
    }

    public void setLastDate(Date lastDate) {
        LastDate = lastDate;
    }

    public String getCur_Abbreviation(){
        return Cur_Abbreviation;
    }

    public void setCur_Abbreviation(String cur_Abbreviation) {
        Cur_Abbreviation = cur_Abbreviation;
    }

    public String getCur_Name() {
        return Cur_Name;
    }

    public void setCur_Name(String cur_Name) {
        Cur_Name = cur_Name;
    }

    public double getCur_OfficialRate() {
        return Cur_OfficialRate;
    }

    public void setCur_OfficialRate(double cur_OfficialRate) {
        Cur_OfficialRate = cur_OfficialRate;
    }
}
