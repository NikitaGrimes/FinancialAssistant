package com.example.financialassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.ScheduledPay;
import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.AccountsDB;
import com.example.financialassistant.modelsDB.ExpDB;
import com.example.financialassistant.modelsDB.ScheduledPayDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;
import static androidx.core.app.NotificationCompat.PRIORITY_LOW;

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

    SwitchCompat isScheduledIn;
    SwitchCompat isScheduledOut;

    Button inComeButtonDT;
    Button outComeButtonDT;

    TextView fromCur;
    TextView toCur;
    TextView DateTimeIn;
    TextView DateTimeOut;

    Calendar dateAndTime = Calendar.getInstance();

    private final int OPERATION_INCOME = 1;
    private final int OPERATION_OUTCOME = 2;
    private final int OPERATION_EXCHANGE = 3;

    Expenses oldExp;

    Button saveButton;

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
        DateTimeIn = (TextView) findViewById(R.id.date_time_InCome_TV);
        DateTimeOut = (TextView) findViewById(R.id.date_time_OutCome_TV);

        inComeCL = (ConstraintLayout) findViewById(R.id.layoutIncome);
        outComeCL = (ConstraintLayout) findViewById(R.id.layoutCosts);
        exChangeCL = (ConstraintLayout) findViewById(R.id.layoutAccExChange);

        isScheduledIn = (SwitchCompat) findViewById(R.id.isScheduledInCome);
        isScheduledOut = (SwitchCompat) findViewById(R.id.isScheduledOutCome);

        outComeButtonDT = (Button) findViewById(R.id.set_date_time_outcome_bt);
        inComeButtonDT = (Button) findViewById(R.id.set_date_time_income_bt);

        inComeButtonDT.setEnabled(false);
        outComeButtonDT.setEnabled(false);

        inComeCL.setVisibility(View.GONE);
        outComeCL.setVisibility(View.GONE);
        exChangeCL.setVisibility(View.GONE);

        dateAndTime.add(Calendar.HOUR, 1);



        DateTimeIn.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
        DateTimeOut.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));

        String[] dataAcc = new String[DataAccounts.accounts.size()];
        String[] dataExp = new String[DataTypesExpenses.typesOfExpenses.size()];
        int i = 0;
        for (Accounts account : DataAccounts.accounts) {
            dataAcc[i] = account.getName_acc();
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
        fromCur.setText(DataAccounts.accounts.get(0).getCur_Abbreviation());
        toCur.setText(DataAccounts.accounts.get(0).getCur_Abbreviation());
        fromSpinnerAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {
                fromCur.setText(DataAccounts.accounts.get(selectedItemPosition).getCur_Abbreviation());
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
                toCur.setText(DataAccounts.accounts.get(selectedItemPosition).getCur_Abbreviation());
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
        saveButton = (Button) findViewById(R.id.save_operation_bt);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        saveButton.setOnClickListener(v -> {
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
                    if (isScheduledIn.isChecked())
                    {
                        ScheduledPayDao scheduledPayDao = DataBaseApp.getInstance(this).scheduledPayDao();
                        TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(this).typeOfExpDao();
                        CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
                        long acc_id = account.id;
                        long exp_type_id = typeOfExpDao.getIdByName("Доход");
                        long cur_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());
                        GregorianCalendar gregorianCalendar = (GregorianCalendar) dateAndTime;
                        ScheduledPay scheduledPay = new ScheduledPay("Доход", addValue,
                                account.getCur_Abbreviation(), account.getName_acc(), gregorianCalendar);
                        scheduledPay.id = (int) scheduledPayDao.insert(new ScheduledPayDB(exp_type_id,
                                addValue, addValue, cur_id, acc_id, gregorianCalendar));
                        DataScheduledPay.scheduledPays.add(scheduledPay);

                        Intent answerIntent = new Intent();
                        answerIntent.putExtra("Action", "UpdateExpenses");
                        setResult(RESULT_OK, answerIntent);
                    }
                    else {
                        value += addValue;
                        account.setValue(value);

                        AccountsDao accountsDao = DataBaseApp.getInstance(this).accountsDao();
                        TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(this).typeOfExpDao();
                        CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();

                        AccountsDB accountsDB = accountsDao.getAccDBById(account.id);
                        accountsDB.value = value;
                        accountsDao.update(accountsDB);
                        DataAccounts.accounts.set(pos, account);

                        ExpDao expDao = DataBaseApp.getInstance(this).expDao();

                        long acc_id = account.id;
                        long exp_type_id = typeOfExpDao.getIdByName("Доход");
                        long cur_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());

                        Expenses newExp = new Expenses("Доход", addValue, account.getCur_Abbreviation(), account.getName_acc());
                        newExp.id = (int) expDao.insert(new ExpDB(exp_type_id, addValue, addValue, cur_id, acc_id, newExp.getDate_operation()));
                        DataExpenses.expenses.add(0, newExp);
                        if (DataExpenses.expenses.size() >= 20) {
                            DataExpenses.expenses.remove(20);
                        }

                        Intent answerIntent = new Intent();
                        answerIntent.putExtra("Action", "UpdateExpensesAndAccounts");
                        setResult(RESULT_OK, answerIntent);
                    }
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
                    if (!account.getCur_Abbreviation().equals(expense.getCur_Abbreviation())){
                        realValueExp = Convert_Currency(value, account.getCur_Abbreviation(), expense.getCur_Abbreviation());
                    }

                    if (isScheduledOut.isChecked()) {
                        CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
                        ScheduledPayDao scheduledPayDao = DataBaseApp.getInstance(this).scheduledPayDao();
                        long acc_id = account.id;
                        long exp_type_id = expense.id;
                        long cur_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());
                        GregorianCalendar gregorianCalendar = (GregorianCalendar) dateAndTime;
                        ScheduledPay scheduledPay = new ScheduledPay(expense.getName(), -1 * value,
                                account.getCur_Abbreviation(), account.getName_acc(), gregorianCalendar);
                        scheduledPay.id = (int) scheduledPayDao.insert(new ScheduledPayDB(exp_type_id,
                                -1 * value, -1 * realValueExp, cur_id, acc_id, gregorianCalendar));
                        scheduledPay.setRealValue(-1 * realValueExp);
                        DataScheduledPay.scheduledPays.add(scheduledPay);

                        Intent answerIntent = new Intent();
                        answerIntent.putExtra("Action", "UpdateExpenses");
                        setResult(RESULT_OK, answerIntent);
                        finish();
                    }
                    else {
                        if (valueAcc >= value) {
                            valueAcc -= value;
                            valueExp += realValueExp;
                            account.setValue(valueAcc);

                            AccountsDao accountsDao = DataBaseApp.getInstance(this).accountsDao();
                            TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(this).typeOfExpDao();
                            CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();

                            AccountsDB accountsDB = accountsDao.getAccDBById(account.id);
                            accountsDB.value = valueAcc;
                            accountsDao.update(accountsDB);

                            DataAccounts.accounts.set(posAcc, account);

                            expense.setValue(valueExp);
                            DataTypesExpenses.typesOfExpenses.set(posExp, expense);
                            TypeOfExpDB typeOfExpDB = typeOfExpDao.getTypeExpDBById(expense.id);
                            typeOfExpDB.value = expense.getValue();
                            typeOfExpDao.update(typeOfExpDB);

                            ExpDao expDao = DataBaseApp.getInstance(this).expDao();
                            long acc_id = account.id;
                            long exp_type_id = expense.id;
                            long cur_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());

                            Expenses newExp = new Expenses(expense.getName(), -1 * value, account.getCur_Abbreviation(), account.getName_acc());
                            newExp.id = (int) expDao.insert(new ExpDB(exp_type_id, -1 * value, -1 * realValueExp, cur_id, acc_id, newExp.getDate_operation()));
                            newExp.setRealValue(-1 * realValueExp);
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
                    fromExp.setDate_operation(gregorianCalendar);
                    toExp.setDate_operation(gregorianCalendar);
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

                        AccountsDao accountsDao = DataBaseApp.getInstance(this).accountsDao();
                        TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(this).typeOfExpDao();
                        CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
                        long cur_from_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());
                        long acc_from_id = account.id;

                        AccountsDB accountsDB = accountsDao.getAccDBById(account.id);
                        accountsDB.value = account.getValue();
                        accountsDao.update(accountsDB);

                        DataAccounts.accounts.set(posFrom, account);
                        fromExp.setValue(-1 * fromValueInt);
                        fromExp.setCur_Abbreviation(account.getCur_Abbreviation());
                        String nameFromAcc = account.getName_acc();

                        if (toValue.charAt(0) == '.') {
                            toValue = "0" + toValue;
                        }
                        int toValueInt = (int)(Double.parseDouble(toValue) * 100);
                        account = DataAccounts.accounts.get(posTo);
                        int toAccValue = account.getValue();
                        toAccValue += toValueInt;
                        account.setValue(toAccValue);

                        accountsDB = accountsDao.getAccDBById(account.id);
                        accountsDB.value = account.getValue();
                        accountsDao.update(accountsDB);
                        long cur_to_id = currentsDao.getIdByAbr(account.getCur_Abbreviation());
                        long acc_to_id = account.id;

                        DataAccounts.accounts.set(posTo, account);
                        toExp.setValue(toValueInt);
                        toExp.setCur_Abbreviation(account.getCur_Abbreviation());
                        String nameToAcc = account.getName_acc();

                        fromExp.setName_acc(nameFromAcc);
                        toExp.setName_acc(nameToAcc);

                        ExpDao expDao = DataBaseApp.getInstance(this).expDao();
                        long exp_type_id = typeOfExpDao.getIdByName("Перевод");
                        ExpDB expDB = new ExpDB(exp_type_id, toExp.getValue(), toExp.getRealValue(), cur_to_id, acc_to_id, toExp.getDate_operation());
                        toExp.id = (int) expDao.insert(expDB);
                        expDB = new ExpDB(exp_type_id, fromExp.getValue(), fromExp.getRealValue(), cur_from_id, acc_from_id, fromExp.getDate_operation());
                        fromExp.id = (int) expDao.insert(expDB);

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
        });
    }

    private void restartNotify() {
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT );
// На случай, если мы ранее запускали активити, а потом поменяли время,
// откажемся от уведомления
        am.cancel(pendingIntent);
// Устанавливаем разовое напоминание
        am.set(AlarmManager.RTC_WAKEUP, stamp.getTime(), pendingIntent);
    }

    public void onDateTimeClick(View view) {
        new TimePickerDialog(AddOperationActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
        new DatePickerDialog(AddOperationActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка даты и времени
    private void setInitialDateTime() {
        if (mode == OPERATION_INCOME) {
            DateTimeIn.setText(DateUtils.formatDateTime(this,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                            | DateUtils.FORMAT_SHOW_TIME));
        }
        else if (mode == OPERATION_OUTCOME) {
            DateTimeOut.setText(DateUtils.formatDateTime(this,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                            | DateUtils.FORMAT_SHOW_TIME));
        }
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);
        setInitialDateTime();
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTime();
    };

    public void onSwitchInComeDT(View view) {
        inComeButtonDT.setEnabled(isScheduledIn.isChecked());
    }

    public void onSwitchOutComeDT(View view) {
        outComeButtonDT.setEnabled(isScheduledOut.isChecked());
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