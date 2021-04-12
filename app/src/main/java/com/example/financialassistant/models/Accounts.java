package com.example.financialassistant.models;

public class Accounts {
    private String name;
    private String type;
    private int value;
    private String currency;

    public Accounts () {

    }

    public Accounts (String _name, String _type, int _value, String _currency) {
        this.name = _name;
        this.type = _type;
        this.value = _value;
        this.currency = _currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
