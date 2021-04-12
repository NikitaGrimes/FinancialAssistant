package com.example.financialassistant.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TypeOfExpenses {

    private String name;
    private int value;
    private String currency;

    public TypeOfExpenses() {

    }

    public TypeOfExpenses(String _name, int _value, String _cur) {
        this.name = _name;
        this.value = _value;
        this.currency = _cur;
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

}
