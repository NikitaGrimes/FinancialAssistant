package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.RouteDiscoveryPreference;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.financialassistant.adapters.CurrenciesAdapter;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Expenses;

import java.util.ArrayList;
import java.util.List;

public class AddOperationActivity extends AppCompatActivity {

    Spinner inComeSpinnerAcc;
    Spinner costSpinnerAcc;
    Spinner costSpinnerExpense;
    Spinner fromSpinnerAcc;
    Spinner toSpinnerAcc;

    ConstraintLayout inComeCL;
    ConstraintLayout outComeCL;
    ConstraintLayout exChangeCL;

    EditText fromValueCur;
    EditText rateEditText;
    EditText toValueCur;

    TextView fromCur;
    TextView toCur;

    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_operation);
        mode = 0;
        inComeSpinnerAcc = (Spinner) findViewById(R.id.incomeAccountSpinner);
        costSpinnerAcc = (Spinner) findViewById(R.id.costsAccountSpinner);
        costSpinnerExpense = (Spinner) findViewById(R.id.costsExpensesSpinner);
        fromSpinnerAcc = (Spinner) findViewById(R.id.fromAccountSpinner);
        toSpinnerAcc = (Spinner) findViewById(R.id.toAccountSpinner);

        fromValueCur = (EditText) findViewById(R.id.exChangeValueFromAcc);
        rateEditText = (EditText) findViewById(R.id.exChangeValueRates);
        toValueCur = (EditText) findViewById(R.id.exChangeValueToAcc);

        fromCur = (TextView) findViewById(R.id.currencyOfFirstAccTextView);
        toCur = (TextView) findViewById(R.id.currencyOfSecondAccTextView);

        inComeCL = (ConstraintLayout) findViewById(R.id.layoutIncome);
        outComeCL = (ConstraintLayout) findViewById(R.id.layoutCosts);
        exChangeCL = (ConstraintLayout) findViewById(R.id.layoutAccExChange);

        inComeCL.setVisibility(View.GONE);
        outComeCL.setVisibility(View.GONE);
        exChangeCL.setVisibility(View.GONE);

        String[] dataAcc = new String[DataAccounts.accounts.size()];
        String[] dataExp = new String[DataExpenses.expenses.size()];
        int i = 0;
        for (Accounts account : DataAccounts.accounts) {
            dataAcc[i] = account.getName();
            i++;
        }

        i = 0;
        for (Expenses expense : DataExpenses.expenses) {
            dataExp[i] = expense.getName();
            i++;
        }

        ArrayAdapter<String> adapterAcc = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataAcc);
        adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inComeSpinnerAcc.setAdapter(adapterAcc);
        costSpinnerAcc.setAdapter(adapterAcc);
        fromSpinnerAcc.setAdapter(adapterAcc);
        toSpinnerAcc.setAdapter(adapterAcc);

        ArrayAdapter<String> adapterExp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataExp);
        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        costSpinnerExpense.setAdapter(adapterExp);
        fromCur.setText(DataAccounts.accounts.get(0).getCurrency());
        toCur.setText(DataAccounts.accounts.get(0).getCurrency());
        fromSpinnerAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {
                fromCur.setText(DataAccounts.accounts.get(selectedItemPosition).getCurrency());
                rateEditText.setText(trueRate(fromCur.getText().toString(), toCur.getText().toString()));
                String str = fromValueCur.getText().toString();
                if(str.length() > 0) {
                    if (str.charAt(0) == '.')
                    {
                        str = "0" + str;
                    }
                    double fromValue = Double.parseDouble(str);
                    double rate = Double.parseDouble(rateEditText.getText().toString());
                    double toValue = fromValue * rate;
                    @SuppressLint("DefaultLocale") String result = String.format("%.2f", toValue);
                    toValueCur.setText(result);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toSpinnerAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {
                toCur.setText(DataAccounts.accounts.get(selectedItemPosition).getCurrency());
                rateEditText.setText(trueRate(fromCur.getText().toString(), toCur.getText().toString()));
                String str = fromValueCur.getText().toString();
                if(str.length() > 0) {
                    if (str.charAt(0) == '.')
                    {
                        str = "0" + str;
                    }
                    double fromValue = Double.parseDouble(str);
                    double rate = Double.parseDouble(rateEditText.getText().toString());
                    double toValue = fromValue * rate;
                    @SuppressLint("DefaultLocale") String result = String.format("%.2f", toValue);
                    toValueCur.setText(result);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rateEditText.setText(trueRate(fromCur.getText().toString(), toCur.getText().toString()));

        fromValueCur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                String str = s.toString();
                if(str.length() > 0) {
                    if (str.charAt(0) == '.')
                    {
                        str = "0" + str;
                    }
                    double fromValue = Double.parseDouble(str);
                    double rate = Double.parseDouble(rateEditText.getText().toString());
                    double toValue = fromValue * rate;
                    @SuppressLint("DefaultLocale") String result = String.format("%.2f", toValue);
                    toValueCur.setText(result);
                }
            }
        });

        toValueCur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    public String trueRate(String fromCur, String toCur) {
        if (fromCur.equals(toCur)) {
            return "1";
        }
        double firstValue = 1, secondValue = 1;
        for (int i = 0, k = 0; k != 2; i++){ //Поиск курсов валют к одной валюте
            String temp = DataCurrents.currentList.get(i).getCur_Abbreviation();
            if (fromCur.equals(temp)){
                firstValue = DataCurrents.currentList.get(i).getCur_OfficialRate();
                k++;
            }
            else if (toCur.equals(temp)){
                secondValue = DataCurrents.currentList.get(i).getCur_OfficialRate();
                k++;
            }
        }
        double rate = secondValue / firstValue; //Расчет курса первой валюты ко второй
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", rate);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickInCome(View view)
    {
        inComeCL.setVisibility(View.VISIBLE);
        outComeCL.setVisibility(View.GONE);
        exChangeCL.setVisibility(View.GONE);
        mode = 1;
    }

    public void onClickOutCome(View view)
    {
        inComeCL.setVisibility(View.GONE);
        outComeCL.setVisibility(View.VISIBLE);
        exChangeCL.setVisibility(View.GONE);
        mode = 2;
    }

    public void onClickExChange(View view)
    {
        inComeCL.setVisibility(View.GONE);
        outComeCL.setVisibility(View.GONE);
        exChangeCL.setVisibility(View.VISIBLE);
        mode = 3;
    }

    @SuppressLint("DefaultLocale")
    public void onClickSaveOperation(View view)
    {
        if (mode == 1) {
            int pos = inComeSpinnerAcc.getSelectedItemPosition();
            Accounts account = DataAccounts.accounts.get(pos);
            double value = Double.parseDouble(account.getValue());
            EditText editText = (EditText) findViewById(R.id.incomeEnterValue);
            String str = editText.getText().toString();

            if (!str.equals("")) {
                if (str.charAt(0) == '.') {
                    str = "0" + str;
                }
                value += Double.parseDouble(str);
                str = String.format("%.2f", value);
                account.setValue(str);
                DataAccounts.accounts.set(pos, account);

                Intent answerIntent = new Intent();
                answerIntent.putExtra("Action", "UpdateAccounts");
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        }
        else if (mode == 2) {
            int posAcc = costSpinnerAcc.getSelectedItemPosition();
            int posExp = costSpinnerExpense.getSelectedItemPosition();
            EditText editText = (EditText) findViewById(R.id.costsEnterValue);
            String str = editText.getText().toString();
            if (!str.equals("")) {
                if (str.charAt(0) == '.') {
                    str = "0" + str;
                }
                Accounts account = DataAccounts.accounts.get(posAcc);
                Expenses expense = DataExpenses.expenses.get(posExp);
                double value = Double.parseDouble(str);
                double valueAcc = Double.parseDouble(account.getValue());
                double valueExp = Double.parseDouble(expense.getValue());

                if (valueAcc >= value) {
                    valueAcc -= value;
                    valueExp += value;
                    str = String.format("%.2f", valueAcc);
                    account.setValue(str);
                    DataAccounts.accounts.set(posAcc, account);
                    str = String.format("%.2f", valueExp);
                    expense.setValue(str);
                    DataExpenses.expenses.set(posExp, expense);

                    Intent answerIntent = new Intent();
                    answerIntent.putExtra("Action", "UpdateExpensesAndAccounts");
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }
            }
        }
        else if (mode == 3) {
            int posFrom = fromSpinnerAcc.getSelectedItemPosition();
            int posTo = toSpinnerAcc.getSelectedItemPosition();
            String fromValue = fromValueCur.getText().toString();
            String toValue = toValueCur.getText().toString();
            if (posFrom != posTo && !fromValue.equals("") && !toValue.equals("")) {
                Accounts account = DataAccounts.accounts.get(posFrom);
                double fromAccValue = Double.parseDouble(account.getValue());
                if (fromValue.charAt(0) == '.') {
                    fromValue = "0" + fromValue;
                }
                double fromValueDouble = Double.parseDouble(fromValue);
                if (fromAccValue >= fromValueDouble) {
                    fromAccValue -= fromValueDouble;
                    fromValue = String.format("%.2f", fromAccValue);
                    account.setValue(fromValue);
                    if (toValue.charAt(0) == '.') {
                        toValue = "0" + toValue;
                    }
                    double toValueDouble = Double.parseDouble(toValue);
                    DataAccounts.accounts.set(posFrom, account);
                    account = DataAccounts.accounts.get(posTo);
                    double toAccValue = Double.parseDouble(account.getValue());
                    toAccValue += toValueDouble;
                    toValue = String.format("%.2f", toAccValue);
                    account.setValue(toValue);
                    DataAccounts.accounts.set(posTo, account);

                    Intent answerIntent = new Intent();
                    answerIntent.putExtra("Action", "UpdateAccounts");
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }
            }
        }
    }
}