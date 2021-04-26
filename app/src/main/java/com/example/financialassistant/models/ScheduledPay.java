package com.example.financialassistant.models;

import java.util.GregorianCalendar;

public class ScheduledPay {

    public int id;
    private String name;
    private int value;
    private int realValue;
    private String Cur_Abbreviation;
    private String name_acc;
    private GregorianCalendar date_operation;

    public ScheduledPay() {

    }

    public ScheduledPay(String _name, int _value, String _currency, String _fromAcc, GregorianCalendar _gregorianCalendar) {
        this.name = _name;
        this.realValue = this.value = _value;
        this.Cur_Abbreviation = _currency;
        this.date_operation = _gregorianCalendar;
        this.name_acc = _fromAcc;
    }

    public ScheduledPay(int _id, String _name, int _value, String _currency, String _fromAcc, GregorianCalendar _gregorianCalendar) {
        this.id = _id;
        this.name = _name;
        this.realValue = this.value = _value;
        this.Cur_Abbreviation = _currency;
        this.date_operation = _gregorianCalendar;
        this.name_acc = _fromAcc;
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

    public int getRealValue() {
        return realValue;
    }

    public void setRealValue(int realValue) {
        this.realValue = realValue;
    }

    public String getCur_Abbreviation() {
        return Cur_Abbreviation;
    }

    public void setCur_Abbreviation(String cur_Abbreviation) {
        Cur_Abbreviation = cur_Abbreviation;
    }

    public String getName_acc() {
        return name_acc;
    }

    public void setName_acc(String name_acc) {
        this.name_acc = name_acc;
    }

    public GregorianCalendar getDate_operation() {
        return date_operation;
    }

    public void setDate_operation(GregorianCalendar date_operation) {
        this.date_operation = date_operation;
    }
}
