package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.financialassistant.adapters.NewExpensesTypeAdapter;
import com.example.financialassistant.data.DataTypeExpenses;

public class AddNewTypeExpensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_new_type_expenses);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lise_expenses_type);
        NewExpensesTypeAdapter adapter = new NewExpensesTypeAdapter(this, DataTypeExpenses.names);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}