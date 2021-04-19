package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.financialassistant.adapters.NewExpensesTypeAdapter;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.RecyclerItemClickListener;
import com.example.financialassistant.models.TypeOfExpenses;

import java.util.ArrayList;

public class AddNewTypeExpensesActivity extends AppCompatActivity {

    ArrayList<String> types;
    NewExpensesTypeAdapter adapter;
    final Context context = this;
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
        types = new ArrayList<>();
        for (TypeOfExpenses expense : DataTypesExpenses.typesOfExpenses) {
            types.add(expense.getName());
        }
        adapter = new NewExpensesTypeAdapter(this, types);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, DataAccounts.recyclerView ,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        num = position;
                        LayoutInflater li = LayoutInflater.from(context);
                        @SuppressLint("InflateParams") View promptsView =
                                li.inflate(R.layout.rename_type_expenses, null);
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                        mDialogBuilder.setView(promptsView);
                        final EditText userInput = (EditText) promptsView.
                                findViewById(R.id.new_type_exp_edit_text);
                        mDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                (dialog, id) -> {
                                    String new_name = userInput.getText().toString();
                                    for (int i = 0; i < DataTypesExpenses.typesOfExpenses.size(); i++) {
                                        if (DataTypesExpenses.typesOfExpenses.get(i).getName().toLowerCase().equals(new_name.toLowerCase()) && num != i) {
                                            Toast.makeText(context, "Название должно быть индивидуальным",
                                                    Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                    TypeOfExpenses oldExpense = DataTypesExpenses.typesOfExpenses.get(num);
                                    TypeOfExpenses newExpense = new TypeOfExpenses(new_name, oldExpense.getValue(),
                                            oldExpense.getCurrency());
                                    for (Expenses expenses : DataExpenses.expenses){
                                        if (expenses.getName().equals(oldExpense.getName())) {
                                            expenses.setName(new_name);
                                        }
                                    }
                                    DataTypesExpenses.typesOfExpenses.set(num, newExpense);
                                    types.set(num, new_name);
                                    adapter.notifyDataSetChanged();
                                })
                                .setNegativeButton("Отмена",
                                        (dialog, id) -> dialog.cancel());
                        AlertDialog alertDialog = mDialogBuilder.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent answerIntent = new Intent();
            answerIntent.putExtra("Action", "UpdateExpenses");
            setResult(RESULT_OK, answerIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSaveTypeExpense(View view) {
        if (action.equals("Create")) {
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
            Intent answerIntent = new Intent();
            answerIntent.putExtra("Action", "UpdateExpenses");
            setResult(RESULT_OK, answerIntent);
            finish();
        }
    }
}