package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.TypeOfExpenses;

import java.util.GregorianCalendar;

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

    private final int OPERATION_INCOME = 1;
    private final int OPERATION_OUTCOME = 2;
    private final int OPERATION_EXCHANGE = 3;

    Expenses oldExp;

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
        String[] dataExp = new String[DataTypesExpenses.typesOfExpenses.size()];
        int i = 0;
        for (Accounts account : DataAccounts.accounts) {
            dataAcc[i] = account.getName();
            i++;
        }

        i = 0;
        for (TypeOfExpenses expense : DataTypesExpenses.typesOfExpenses) {
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

        Bundle arguments = getIntent().getExtras();
        try {
            int pos = Integer.parseInt(arguments.get("Num").toString());
            oldExp = DataExpenses.expenses.get(pos);
        }
        catch (Exception e) {
            oldExp = null;
        }
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
        mode = OPERATION_INCOME;
    }

    public void onClickOutCome(View view)
    {
        inComeCL.setVisibility(View.GONE);
        outComeCL.setVisibility(View.VISIBLE);
        exChangeCL.setVisibility(View.GONE);
        mode = OPERATION_OUTCOME;
    }

    public void onClickExChange(View view)
    {
        inComeCL.setVisibility(View.GONE);
        outComeCL.setVisibility(View.GONE);
        exChangeCL.setVisibility(View.VISIBLE);
        mode = OPERATION_EXCHANGE;
    }

    @SuppressLint("DefaultLocale")
    public void onClickSaveOperation(View view)
    {
        if (mode == OPERATION_INCOME) {
            int pos = inComeSpinnerAcc.getSelectedItemPosition();
            Accounts account = DataAccounts.accounts.get(pos);
            int value = account.getValue();
            EditText editText = (EditText) findViewById(R.id.incomeEnterValue);
            String str = editText.getText().toString();
            if (!str.equals("")) {
                if (str.charAt(0) == '.') {
                    str = "0" + str;
                }
                int addValue = (int) (Double.parseDouble(str) * 100);
                value += addValue;
                account.setValue(value);
                DataAccounts.accounts.set(pos, account);

                Expenses newExp = new Expenses("Доход", addValue, account.getCurrency(), account.getName());
                DataExpenses.expenses.add(0, newExp);
                if (DataExpenses.expenses.size() >= 20) {
                    DataExpenses.expenses.remove(20);
                }

                Intent answerIntent = new Intent();
                answerIntent.putExtra("Action", "UpdateExpensesAndAccounts");
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        }
        else if (mode == OPERATION_OUTCOME) {
            int posAcc = costSpinnerAcc.getSelectedItemPosition();
            int posExp = costSpinnerExpense.getSelectedItemPosition();
            EditText editText = (EditText) findViewById(R.id.costsEnterValue);
            String str = editText.getText().toString();
            if (!str.equals("")) {
                if (str.charAt(0) == '.') {
                    str = "0" + str;
                }
                Accounts account = DataAccounts.accounts.get(posAcc);
                TypeOfExpenses expense = DataTypesExpenses.typesOfExpenses.get(posExp);
                int value = (int) (Double.parseDouble(str) * 100);
                int valueAcc = account.getValue();
                int valueExp = expense.getValue();
                int realValueExp = value;
                if (!account.getCurrency().equals(expense.getCurrency())){
                    realValueExp = Convert_Currency(value, account.getCurrency(), expense.getCurrency());
                }

                if (valueAcc >= value) {
                    valueAcc -= value;
                    valueExp += realValueExp;
                    account.setValue(valueAcc);
                    DataAccounts.accounts.set(posAcc, account);
                    expense.setValue(valueExp);
                    DataTypesExpenses.typesOfExpenses.set(posExp, expense);

                    Expenses newExp = new Expenses(expense.getName(), -1 * value, account.getCurrency(), account.getName());
                    newExp.setRealValue(realValueExp);
                    DataExpenses.expenses.add(0, newExp);
                    if(DataExpenses.expenses.size() >= 20) {
                        DataExpenses.expenses.remove(20);
                    }

                    Intent answerIntent = new Intent();
                    answerIntent.putExtra("Action", "UpdateExpensesAndAccounts");
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }
            }
        }
        else if (mode == OPERATION_EXCHANGE) {
            int posFrom = fromSpinnerAcc.getSelectedItemPosition();
            int posTo = toSpinnerAcc.getSelectedItemPosition();
            String fromValue = fromValueCur.getText().toString();
            String toValue = toValueCur.getText().toString();
            if (posFrom != posTo && !fromValue.equals("") && !toValue.equals("")) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                Expenses fromExp = new Expenses();
                Expenses toExp = new Expenses();
                fromExp.setDate(gregorianCalendar);
                toExp.setDate(gregorianCalendar);
                fromExp.setName("Перевод");
                toExp.setName("Перевод");

                Accounts account = DataAccounts.accounts.get(posFrom);
                int fromAccValue = account.getValue();

                if (fromValue.charAt(0) == '.') {
                    fromValue = "0" + fromValue;
                }
                int fromValueInt = (int)(Double.parseDouble(fromValue) * 100);
                if (fromAccValue >= fromValueInt) {
                    fromAccValue -= fromValueInt;
                    account.setValue(fromAccValue);
                    DataAccounts.accounts.set(posFrom, account);
                    fromExp.setValue(-1 * fromValueInt);
                    fromExp.setCurrency(account.getCurrency());
                    String nameFromAcc = account.getName();

                    if (toValue.charAt(0) == '.') {
                        toValue = "0" + toValue;
                    }
                    int toValueInt = (int)(Double.parseDouble(toValue) * 100);
                    account = DataAccounts.accounts.get(posTo);
                    int toAccValue = account.getValue();
                    toAccValue += toValueInt;
                    account.setValue(toAccValue);
                    DataAccounts.accounts.set(posTo, account);
                    toExp.setValue(toValueInt);
                    toExp.setCurrency(account.getCurrency());
                    String nameToAcc = account.getName();

                    fromExp.setFromAcc(nameFromAcc);
                    toExp.setFromAcc(nameToAcc);

                    DataExpenses.expenses.add(0, fromExp);
                    if (DataExpenses.expenses.size() >= 20) {
                        DataExpenses.expenses.remove(20);
                    }
                    DataExpenses.expenses.add(0, toExp);
                    if (DataExpenses.expenses.size() >= 20) {
                        DataExpenses.expenses.remove(20);
                    }

                    Intent answerIntent = new Intent();
                    answerIntent.putExtra("Action", "UpdateExpensesAndAccounts");
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }
            }
        }
    }

    public int Convert_Currency(int fromCur, String fromCurName, String toCurName) {
        double tempDouble = fromCur / 100.;
        if (fromCurName.equals(toCurName)) {
            return (int) (tempDouble * 100);
        }
        double firstValue = 1, secondValue = 1;
        for (int i = 0, k = 0; k != 2; i++){ //Поиск курсов валют к одной валюте
            String temp = DataCurrents.currentList.get(i).getCur_Abbreviation();
            if (fromCurName.equals(temp)){
                firstValue = DataCurrents.currentList.get(i).getCur_OfficialRate();
                k++;
            }
            else if (toCurName.equals(temp)){
                secondValue = DataCurrents.currentList.get(i).getCur_OfficialRate();
                k++;
            }
        }
        double rate = secondValue / firstValue; //Расчет курса первой валюты ко второй
        double resRate = tempDouble * rate;
        return (int) (resRate * 100);
    }
}