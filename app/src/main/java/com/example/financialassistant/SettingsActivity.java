package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialassistant.dao.AccountsDao;
import com.example.financialassistant.dao.CurrentsDao;
import com.example.financialassistant.dao.ExpDao;
import com.example.financialassistant.dao.ScheduledPayDao;
import com.example.financialassistant.dao.TypeOfExpDao;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataScheduledPay;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Currents;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.AccountsDB;
import com.example.financialassistant.modelsDB.ExpDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;
import com.example.financialassistant.utils.JSONMainCurHelper;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    TextView AbrCur;
    TextView NameCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_settings);
        AbrCur = (TextView) findViewById(R.id.mainCurAbr);
        NameCur = (TextView) findViewById(R.id.mainCurName);
        CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
        Currents current = currentsDao.getCurByAbr(DataCurrents.mainCur);
        AbrCur.setText(current.getCur_Abbreviation());
        NameCur.setText(current.getCur_Name());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDeleteBDClick(View view) {
        new AlertDialog.Builder(view.getContext())
                .setMessage(getResources().getString(R.string.removeOperation))
                .setPositiveButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                .setNegativeButton(getResources().getString(R.string.remove), (dialogInterface, i) -> {
                    ExpDao expDao = DataBaseApp.getInstance(view.getContext()).expDao();
                    expDao.deleteAll();
                    DataExpenses.expenses.clear();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public void onChangeMainCurClick(View view) {
        new AlertDialog.Builder(view.getContext())
                .setMessage(getResources().getString(R.string.changeMainCurAlert))
                .setPositiveButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                .setNegativeButton(getResources().getString(R.string.change), (dialogInterface, i) -> {
                    Intent intent = new Intent(SettingsActivity.this, CurrenciesActivity.class);
                    intent.putExtra("Who", 5);
                    startActivityForResult(intent, 0);
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            DataCurrents.mainCur = data.getStringExtra("Currency");
            CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
            Currents current = currentsDao.getCurByAbr(DataCurrents.mainCur);
            AbrCur.setText(current.getCur_Abbreviation());
            NameCur.setText(current.getCur_Name());

            ExpDao expDao = DataBaseApp.getInstance(this).expDao();
            expDao.deleteAll();
            DataExpenses.expenses.clear();

            TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(this).typeOfExpDao();
            List<TypeOfExpDB> ofExpDBList = typeOfExpDao.getAll();
            for (TypeOfExpDB el : ofExpDBList) {
                el.value = 0;
                el.currents_id = current.getCur_ID();
                typeOfExpDao.update(el);
            }
            List<TypeOfExpenses> typeOfExpenses = typeOfExpDao.getAllExpType();
            DataTypesExpenses.typesOfExpenses.clear();
            DataTypesExpenses.typesOfExpenses.addAll(typeOfExpenses);
            JSONMainCurHelper.exportToJSON(this, current.getCur_Abbreviation());
        }
    }
}