package com.example.financialassistant.models;

public class Accounts {

    public long id;
    private String name_acc;
    private String type;
    private int value;
    private String Cur_Abbreviation;

    public Accounts () {

    }

    public Accounts (String _name, String _type, int _value, String _currency) {
        this.name_acc = _name;
        this.type = _type;
        this.value = _value;
        this.Cur_Abbreviation = _currency;
    }

    public Accounts (int _id, String _name, String _type, int _value, String _currency) {
        this.id = _id;
        this.name_acc = _name;
        this.type = _type;
        this.value = _value;
        this.Cur_Abbreviation = _currency;
    }

    public String getName_acc() {
        return name_acc;
    }

    public void setName_acc(String name_acc) {
        this.name_acc = name_acc;
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

    public String getCur_Abbreviation() {
        return Cur_Abbreviation;
    }

    public void setCur_Abbreviation(String cur_Abbreviation) {
        this.Cur_Abbreviation = cur_Abbreviation;
    }
}
