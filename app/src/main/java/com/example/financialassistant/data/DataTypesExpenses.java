package com.example.financialassistant.data;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.adapters.TypesOfExpensesAdapter;
import com.example.financialassistant.models.TypeOfExpenses;

import java.util.ArrayList;

public class DataTypesExpenses {
    public static ArrayList<TypeOfExpenses> typesOfExpenses = new ArrayList<>();
    public static RecyclerView recyclerView;
    public static TypesOfExpensesAdapter adapter;
}
