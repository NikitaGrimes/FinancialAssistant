package com.example.financialassistant.models;

public class Expenses {
    private String name;
    private String value;
    private String currency;

    public Expenses () {

    }

    public Expenses (String _name, String _value, String _cur) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
