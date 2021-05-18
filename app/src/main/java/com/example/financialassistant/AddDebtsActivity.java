package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialassistant.dao.CurrentsDao;
import com.example.financialassistant.dao.DebtsDao;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataDebts;
import com.example.financialassistant.models.Debts;
import com.example.financialassistant.modelsDB.DebtsDB;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddDebtsActivity extends AppCompatActivity {

    String action;

    EditText nameText;
    EditText valueText;

    Button chooseCurButton;
    Button chooseDateButton;
    Button SaveDebts;

    RadioGroup radioGroup;

    TextView deadlineTextView;

    Calendar chosenDate;

    Debts oldDebt;

    int oldNum;

    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        chosenDate.set(Calendar.YEAR, year);
        chosenDate.set(Calendar.MONTH, monthOfYear);
        chosenDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTime();
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        action = arguments.get("Action").toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_debts);
        nameText = (EditText) findViewById(R.id.DebtsNameInput);
        valueText = (EditText) findViewById(R.id.debts_start_value);
        chooseCurButton = (Button) findViewById(R.id.current_debt);
        chooseDateButton = (Button) findViewById(R.id.set_deadline_button);
        SaveDebts = (Button) findViewById(R.id.saveDebtsButton);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupIsDebtors);

        deadlineTextView = (TextView) findViewById(R.id.view_of_deadline_debts);

        chosenDate = Calendar.getInstance();
        chosenDate.add(Calendar.MONTH, 1);
        setInitialDateTime();

        if (action.equals("Create")) {
            chooseCurButton.setText(DataCurrents.toCurrency);
        }
        else if (action.equals("Remake")) {
            int pos = Integer.parseInt(arguments.get("Num").toString());
            oldDebt = DataDebts.debts.get(pos);
            oldNum = pos;
            nameText.setText(oldDebt.getName());
            double tempValue = oldDebt.getValue() / 100.;
            @SuppressLint("DefaultLocale") String value_String = String.format("%.2f", tempValue);
            valueText.setText(value_String);
            chooseCurButton.setText(oldDebt.getCur_Abbreviation());
            chosenDate = oldDebt.getDeadLine();
            setInitialDateTime();
            RadioButton radioButton;
            if (oldDebt.isDebtor()) {
                radioButton = (RadioButton) findViewById(R.id.i_am_debtor_case);
            }
            else {
                radioButton = (RadioButton) findViewById(R.id.someone_are_debtor_case);
            }
            radioButton.setChecked(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String curName = data.getStringExtra("Currency");
            chooseCurButton.setText(curName);
        }
    }

    public void onClickChooseCurrency(View view) {
        Intent intent = new Intent(AddDebtsActivity.this, CurrenciesActivity.class);
        intent.putExtra("Who", 4);
        startActivityForResult(intent, 0);
    }

    @SuppressLint("SetTextI18n")
    private void setInitialDateTime() {
        deadlineTextView.setText( getResources().getString(R.string.deadline) +
                DateUtils.formatDateTime(this, chosenDate.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void onClickChooseDeadline(View view) {
        new DatePickerDialog(AddDebtsActivity.this, d,
                chosenDate.get(Calendar.YEAR),
                chosenDate.get(Calendar.MONTH),
                chosenDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void onClickSaveDebt(View view) {
        if (chosenDate.after(Calendar.getInstance())) {
            int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                Toast.makeText(this, getResources().getString(R.string.chooseWhoIsDebtor),
                        Toast.LENGTH_LONG).show();
                return;
            }
            RadioButton myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
            boolean isDebtor = false;
            if (myRadioButton.getText().toString().equals(getResources().getString(R.string.i_am_debtor))) {
                isDebtor = true;
            }
            String name = nameText.getText().toString();
            if (action.equals("Create")) {
                for (Debts debt : DataDebts.debts) {
                    if (debt.getName().toLowerCase().equals(name.toLowerCase())) {
                        Toast.makeText(this, getResources().getString(R.string.nameOfDebtIsUnique),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            else {
                for (Debts debt : DataDebts.debts) {
                    if (debt.getName().toLowerCase().equals(name.toLowerCase()) && debt.id != oldDebt.id) {
                        Toast.makeText(this, getResources().getString(R.string.nameOfDebtIsUnique),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            int startValue = 0;
            String str = valueText.getText().toString();
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < str.length(); i++){
                if (str.charAt(i) == ',') {
                    temp.append(".");
                }
                else {
                    temp.append(str.charAt(i));
                }
            }
            try {
                startValue = (int) (Double.parseDouble(temp.toString()) * 100 + 0.1);
            }
            catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
            GregorianCalendar newDeadline = (GregorianCalendar) chosenDate;
            String currency = chooseCurButton.getText().toString();
            DebtsDao debtsDao = DataBaseApp.getInstance(view.getContext()).debtsDao();
            CurrentsDao currentsDao = DataBaseApp.getInstance(view.getContext()).currentsDao();
            long cur_id = currentsDao.getIdByAbr(currency);
            if (action.equals("Create")) {
                long new_id = debtsDao.insert(new DebtsDB(name, startValue, cur_id, newDeadline, isDebtor));
                DataDebts.debts.add(new Debts((int) new_id, name, startValue, currency, newDeadline, isDebtor));
            }
            else if (action.equals("Remake")) {
                DebtsDB debtsDB = new DebtsDB(oldDebt.id, name, startValue, cur_id, newDeadline, isDebtor);
                oldDebt.setName(name);
                oldDebt.setValue(startValue);
                oldDebt.setCur_Abbreviation(currency);
                oldDebt.setDeadLine(newDeadline);
                oldDebt.setDebtor(isDebtor);
                DataDebts.debts.set(oldNum, oldDebt);
                debtsDao.update(debtsDB);
            }
            Intent answerIntent = new Intent();
            answerIntent.putExtra("Action", "UpdateDebts");
            setResult(RESULT_OK, answerIntent);
            finish();
        }
        else {
            Toast.makeText(view.getContext(), getResources().getString(R.string.incorrectDeadline),
                    Toast.LENGTH_LONG).show();
        }
    }
}