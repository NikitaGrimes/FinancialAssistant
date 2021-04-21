package com.example.financialassistant.modelsDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TypeOfAccDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String type;

    public TypeOfAccDB() {

    }

    public TypeOfAccDB(String _type) {
        this.type = _type;
    }
}
