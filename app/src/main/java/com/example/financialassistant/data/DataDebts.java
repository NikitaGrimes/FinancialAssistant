package com.example.financialassistant.data;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.adapters.DebtsAdapter;
import com.example.financialassistant.models.Debts;

import java.util.ArrayList;

public class DataDebts {
    public static ArrayList<Debts> debts = new ArrayList<>();
    public static RecyclerView recyclerView;
    public static DebtsAdapter adapter;
}
