package com.example.financialassistant.models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Debts {

    private String name;
    private int value;
    private String currency;
    private GregorianCalendar deadLine;
    private boolean isDebtor;

    public Debts() {

    }

    public Debts(String _name, int _value, String _currency, boolean _isDebtor) {
        this.name = _name;
        this.value = _value;
        this.currency = _currency;
        this.isDebtor = _isDebtor;
        deadLine = new GregorianCalendar();
        deadLine.add(Calendar.MONTH, 1);
    }

    public Debts(String _name, int _value, String _currency, GregorianCalendar _date, boolean _isDebtor) {
        this.name = _name;
        this.value = _value;
        this.currency = _currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
