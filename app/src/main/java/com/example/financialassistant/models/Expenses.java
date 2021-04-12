package com.example.financialassistant.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Expenses {

    private String name;
    private int value;
    private String currency;
    private Calendar date;

    public Expenses(){
        date = new GregorianCalendar();
    }

    public Expenses(String _name, int _value, String _currency){
        this.name = _name;
        this.value = _value;
        this.currency = _currency;
        this.date = new GregorianCalendar();
    }

    public Expenses(String _name, int _value, String _currency, Calendar _date){
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

}
