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
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataCurrents;

public class AddAccountActivity extends AppCompatActivity {

    String action;
    Button chooseCurrency;
    Button saveAccount;

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
        if (action.equals("Create")) {
            chooseCurrency.setText(DataCurrents.toCurrency);
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
        Intent intent = new Intent(AddAccountActivity.this, Currencies.class);
        intent.putExtra("Who", 3);
        startActivityForResult(intent, 0);
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
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.addAccountRadioGroup);
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            Toast.makeText(this, "Пожалуйста, заполните поле 'Тип счета'",
                    Toast.LENGTH_LONG).show();
            return;
        }
        RadioButton myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
        String typeAccount = myRadioButton.getText().toString();
        EditText nameText = (EditText) findViewById(R.id.addAccountNameAcc);
        EditText valueText = (EditText) findViewById(R.id.addAccountValueAcc);
        String valueAccount = valueText.getText().toString();
        String nameAccount = nameText.getText().toString();
        for (int i = 0; i < DataAccounts.names.size(); i++) {
            if (nameAccount.toLowerCase().equals(DataAccounts.names.get(i).toLowerCase())) {
                Toast.makeText(this, "Название счета должно быть индивидуальным",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        double tempDouble = Double.parseDouble(valueAccount);
        valueAccount = String.format("%.2f", tempDouble);
        DataAccounts.names.add(nameAccount);
        DataAccounts.types.add(typeAccount);
        DataAccounts.currency.add(valueAccount + " " +  chooseCurrency.getText().toString());
        Intent answerIntent = new Intent();
        answerIntent.putExtra("Action", "UpdateAccounts");
        setResult(RESULT_OK, answerIntent);
        finish();
    }
}