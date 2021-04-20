package com.example.financialassistant.models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Expenses {

    private String name;
    private int value;
    private int realValue;
    private String currency;
    private String fromAcc;
    private GregorianCalendar date;

    public Expenses(){
        date = new GregorianCalendar();
    }

    public Expenses(String _name, int _value, String _currency, String _fromAcc){
        this.name = _name;
        this.realValue = this.value = _value;
        this.currency = _currency;
        this.date = new GregorianCalendar();
        this.fromAcc = _fromAcc;
    }

    public Expenses(String _name, int _value, String _currency, GregorianCalendar _date, String _fromAcc){
        this.name = _name;
        this.realValue = this.value = _value;
        this.currency = _currency;
        this.date = _date;
        this.fromAcc = _fromAcc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public String getFromAcc() {
        return fromAcc;
    }

    public void setFromAcc(String fromAcc) {
        this.fromAcc = fromAcc;
    }

    public int getRealValue() {
        return realValue;
    }

    public void setRealValue(int realValue) {
        this.realValue = realValue;
    }
}
