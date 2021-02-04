package com.example.financialassistant.models;

public class Accounts_View_Class {

    private String name;
    private String value;

    public Accounts_View_Class(String _name, String _value) {
        this.name = _name;
        this.value = _value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
