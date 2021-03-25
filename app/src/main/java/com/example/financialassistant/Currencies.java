package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.financialassistant.adapters.CurrenciesAdapter;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.models.Currents;
import com.example.financialassistant.models.RecyclerItemClickListener;

import java.util.ArrayList;

public class Currencies extends AppCompatActivity {

    ArrayList<Currents> currents;
    int who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        who = Integer.parseInt(arguments.get("Who").toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_currencies);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_currencies);
        CurrenciesAdapter adapter = new CurrenciesAdapter(this, DataCurrents.currentList);
        recyclerView.setAdapter(adapter);
        currents = DataCurrents.currentList;
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
                currents = new ArrayList<>();
                for (int i = 0; i < DataCurrents.currentList.size(); i++){
                    if (DataCurrents.currentList.get(i).getCur_Abbreviation().toLowerCase().contains(str.toLowerCase()) ||
                    DataCurrents.currentList.get(i).getCur_Name().toLowerCase().contains(str.toLowerCase())){
                        currents.add(DataCurrents.currentList.get(i));
                    }
                }
                CurrenciesAdapter adapter = new CurrenciesAdapter(getApplicationContext(), currents);
                recyclerView.setAdapter(adapter);
            }
        });
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String curName = currents.get(position).getCur_Abbreviation();
                        if (who == 3) {
                            Intent answerIntent = new Intent();
                            answerIntent.putExtra("Currency", curName);
                            setResult(RESULT_OK, answerIntent);
                            finish();
                        }
                        if (who == 1) {
                            DataCurrents.fromCurrency = curName;
                            finish();
                        }
                        else if (who == 2) {
                            DataCurrents.toCurrency = curName;
                            finish();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );
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