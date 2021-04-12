package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financialassistant.adapters.NewExpensesTypeAdapter;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.TypeOfExpenses;

import java.util.ArrayList;

public class AddNewTypeExpensesActivity extends AppCompatActivity {

    String action;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        action = arguments.get("Action").toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_new_type_expenses);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lise_expenses_type);
        ArrayList<String> types = new ArrayList<>();
        for (TypeOfExpenses expense : DataTypesExpenses.typesOfExpenses) {
            types.add(expense.getName());
        }
        NewExpensesTypeAdapter adapter = new NewExpensesTypeAdapter(this, types);
        recyclerView.setAdapter(adapter);
        if (action.equals("Remake")) {
            num = arguments.getInt("Num");
            EditText editText = (EditText) findViewById(R.id.newNameForExpense);
            editText.setText(DataTypesExpenses.typesOfExpenses.get(num).getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSaveTypeExpense(View view) {
        if (action.equals("Create") || action.equals("Remake")) {
            EditText editText = (EditText) findViewById(R.id.newNameForExpense);
            String name = editText.getText().toString();
            TypeOfExpenses newExpense = null;
            if (action.equals("Create")) {
                for (TypeOfExpenses expense : DataTypesExpenses.typesOfExpenses) {
                    if (expense.getName().toLowerCase().equals(name.toLowerCase())) {
                        Toast.makeText(this, "Название должно быть индивидуальным",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                newExpense = new TypeOfExpenses(name, 0, "BYN");
                DataTypesExpenses.typesOfExpenses.add(newExpense);
            }
            else if (action.equals("Remake")) {
                for (int i = 0; i < DataTypesExpenses.typesOfExpenses.size(); i++) {
                    if (DataTypesExpenses.typesOfExpenses.get(i).getName().toLowerCase().equals(name.toLowerCase()) && num != i) {
                        Toast.makeText(this, "Название должно быть индивидуальным",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                newExpense = new TypeOfExpenses(name, DataTypesExpenses.typesOfExpenses.get(num).getValue(),
                        DataTypesExpenses.typesOfExpenses.get(num).getCurrency());
                DataTypesExpenses.typesOfExpenses.set(num, newExpense);
            }
            Intent answerIntent = new Intent();
            answerIntent.putExtra("Action", "UpdateExpenses");
            setResult(RESULT_OK, answerIntent);
            finish();
        }
    }
}