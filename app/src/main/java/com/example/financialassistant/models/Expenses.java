package com.example.financialassistant.models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Expenses {

    private String name;
    private int value;
    private String currency;
    private Date date;

    public Expenses(){
        date = new Date();
    }

    public Expenses(String _name, int _value, String _currency){
        this.name = _name;
        this.value = _value;
        this.currency = _currency;
        this.date = new Date();
    }

    public Expenses(String _name, int _value, String _currency, Date _date){
        this.name = _name;
        this.value = _value;
        this.currency = _currency;
        this.date = _date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
