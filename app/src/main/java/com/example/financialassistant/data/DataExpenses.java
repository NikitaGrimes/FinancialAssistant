package com.example.financialassistant.data;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.adapters.ExpensesAdapter;
import com.example.financialassistant.models.Expenses;

import java.util.ArrayList;

public class DataExpenses {
    public static ArrayList<Expenses> expenses = new ArrayList<>();
    public static RecyclerView recyclerView;
    public static ExpensesAdapter adapter;
}


