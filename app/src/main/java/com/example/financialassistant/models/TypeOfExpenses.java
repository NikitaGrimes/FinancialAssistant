package com.example.financialassistant.models;

public class TypeOfExpenses {

    public int id;
    private String name;
    private int value;
    private String Cur_Abbreviation;

    public TypeOfExpenses() {

    }

    public TypeOfExpenses(String _name, int _value, String _cur) {
        this.name = _name;
        this.value = _value;
        this.Cur_Abbreviation = _cur;
    }

    public TypeOfExpenses(int _id, String _name, int _value, String _cur) {
        this.id = _id;
        this.name = _name;
        this.value = _value;
        this.Cur_Abbreviation = _cur;
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

}
