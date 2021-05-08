package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.financialassistant.dao.AccountsDao;
import com.example.financialassistant.dao.CurrentsDao;
import com.example.financialassistant.dao.TypeOfAccDao;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.modelsDB.AccountsDB;

public class AddAccountActivity extends AppCompatActivity {

    String action;

    String name;
    int value;
    String value_String;
    String currency;
    String type;
    int num;
    long oldId;

    Button chooseCurrency;
    Button saveAccount;
    EditText nameText;
    EditText valueText;
    RadioGroup radioGroup;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        action = arguments.get("Action").toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_account);
        chooseCurrency = (Button) findViewById(R.id.addAccountEnterCurrency);
        saveAccount = (Button) findViewById(R.id.addAccountAddButton);
        nameText = (EditText) findViewById(R.id.addAccountNameAcc);
        valueText = (EditText) findViewById(R.id.addAccountValueAcc);
        radioGroup = (RadioGroup) findViewById(R.id.addAccountRadioGroup);
        if (action.equals("Create")) {
            chooseCurrency.setText(DataCurrents.toCurrency);
        }
        else if (action.equals("Remake")) {
            num = arguments.getInt("Num");
            Accounts account = DataAccounts.accounts.get(num);
            oldId = account.id;
            name = account.getName_acc();
            value = account.getValue();
            currency = account.getCur_Abbreviation();
            type = account.getType();
            nameText.setText(name);
            value_String = String.format("%.2f", value/100.);
            valueText.setText(value_String);
            chooseCurrency.setText(currency);
            if (type.equals(getResources()
                    .getString(R.string.addAccountRadioTextCash))) {
                RadioButton radioButton = (RadioButton) findViewById(R.id.cash);
                radioButton.setChecked(true);
            }
            else if (type.equals(getResources()
                    .getString(R.string.addAccountRadioTextCard))) {
                RadioButton radioButton = (RadioButton) findViewById(R.id.card);
                radioButton.setChecked(true);
            }
            else if (type.equals(getResources()
                    .getString(R.string.addAccountRadioTextElectronic))) {
                RadioButton radioButton = (RadioButton) findViewById(R.id.electronic);
                radioButton.setChecked(true);
            }
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

    public void onClickChooseCurrency(View view)
    {
        Intent intent = new Intent(AddAccountActivity.this, CurrenciesActivity.class);
        intent.putExtra("Who", 3);
        startActivityForResult(intent, 0);
    }

    public boolean isChanged() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
        return !(name.toLowerCase().equals(nameText.getText().toString().toLowerCase()) &&
                value_String.equals(valueText.getText().toString()) &&
                type.equals(myRadioButton.getText().toString()) &&
                currency.equals(chooseCurrency.getText().toString()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String curName = data.getStringExtra("Currency");
            chooseCurrency.setText(curName);
        }
    }

    @SuppressLint("DefaultLocale")
    public void onClickAddAccount(View view)
    {
        if (action.equals("Create") || (action.equals("Remake") && isChanged())) {
            int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                Toast.makeText(this, "Пожалуйста, заполните поле 'Тип счета'",
                        Toast.LENGTH_LONG).show();
                return;
            }
            RadioButton myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
            String typeAccount = myRadioButton.getText().toString();
            String valueAccount = valueText.getText().toString();
            String nameAccount = nameText.getText().toString();
            Accounts account;
            for (int i = 0; i < DataAccounts.accounts.size(); i++) {
                if (action.equals("Remake") && i == num)
                    continue;
                account = DataAccounts.accounts.get(i);
                if (nameAccount.toLowerCase().equals(account.getName_acc().toLowerCase())) {
                    Toast.makeText(this, "Название счета должно быть индивидуальным",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            double tempDouble = Double.parseDouble(valueAccount);
            int valueAccInt = (int) (tempDouble * 100 + 0.1);
            account = new Accounts(nameAccount, typeAccount, valueAccInt,
                    chooseCurrency.getText().toString());
            AccountsDao accountsDao = DataBaseApp.getInstance(this).accountsDao();
            TypeOfAccDao typeOfAccDao = DataBaseApp.getInstance(this).typeOfAccDao();
            CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
            if (action.equals("Create")) {
                long type_id = typeOfAccDao.getIdByName(account.getType());
                long cur_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());
                account.id = accountsDao.insert(new AccountsDB(account.getName_acc(), account.getValue(), cur_id, type_id));
                DataAccounts.accounts.add(account);
            }
            else if (action.equals("Remake")) {
                long type_id = typeOfAccDao.getIdByName(account.getType());
                long cur_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());
                AccountsDB oldAcc = accountsDao.getAccDBById(oldId);
                oldAcc.value = account.getValue();
                oldAcc.name_acc = account.getName_acc();
                oldAcc.type_of_acc_id = type_id;
                oldAcc.currents_id = cur_id;
                accountsDao.update(oldAcc);
                DataAccounts.accounts.set(num, account);
            }
            Intent answerIntent = new Intent();
            answerIntent.putExtra("Action", "UpdateAccounts");
            setResult(RESULT_OK, answerIntent);
            finish();
        }
    }
}