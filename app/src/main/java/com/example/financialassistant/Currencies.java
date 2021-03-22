package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.financialassistant.adapters.CurrenciesAdapter;
import com.example.financialassistant.adapters.NewExpensesTypeAdapter;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataTypeExpenses;

public class Currencies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_currencies);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_currencies);
        CurrenciesAdapter adapter = new CurrenciesAdapter(this, DataCurrents.currentList);
        recyclerView.setAdapter(adapter);
        EditText nameCurrency = (EditText) findViewById(R.id.editCurrencyNameText);
        nameCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                String str = s.toString();
                
            }
        });
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