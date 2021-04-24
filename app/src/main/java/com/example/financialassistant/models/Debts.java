package com.example.financialassistant.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Debts {

    public int id;
    private String name;
    private int value;
    private String Cur_Abbreviation;
    private GregorianCalendar deadLine;
    private boolean isDebtor;

    public Debts() {

    }

    public Debts(String _name, int _value, String _currency, boolean _isDebtor) {
        this.name = _name;
        this.value = _value;
        this.Cur_Abbreviation = _currency;
        this.isDebtor = _isDebtor;
        deadLine = new GregorianCalendar();
        deadLine.add(Calendar.MONTH, 1);
    }

    public Debts(String _name, int _value, String _currency, GregorianCalendar _date, boolean _isDebtor) {
        this.name = _name;
        this.value = _value;
        this.Cur_Abbreviation = _currency;
        this.isDebtor = _isDebtor;
        deadLine = _date;
    }

    public Debts(int _id, String _name, int _value, String _currency, GregorianCalendar _date, boolean _isDebtor) {
        this.id = _id;
        this.name = _name;
        this.value = _value;
        this.Cur_Abbreviation = _currency;
        this.isDebtor = _isDebtor;
        deadLine = _date;
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

    public String getCur_Abbreviation() {
        return Cur_Abbreviation;
    }

    public void setCur_Abbreviation(String cur_Abbreviation) {
        this.Cur_Abbreviation = cur_Abbreviation;
    }

    public GregorianCalendar getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(GregorianCalendar deadLine) {
        this.deadLine = deadLine;
    }

    public boolean isDebtor() {
        return isDebtor;
    }

    public void setDebtor(boolean debtor) {
        isDebtor = debtor;
    }
}
