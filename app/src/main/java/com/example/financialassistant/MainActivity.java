package com.example.financialassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.financialassistant.dao.AccountsDao;
import com.example.financialassistant.dao.CurrentsDao;
import com.example.financialassistant.dao.DebtsDao;
import com.example.financialassistant.dao.ExpDao;
import com.example.financialassistant.dao.ScheduledPayDao;
import com.example.financialassistant.dao.TypeOfAccDao;
import com.example.financialassistant.dao.TypeOfExpDao;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataDebts;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataScheduledPay;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Currents;
import com.example.financialassistant.models.Debts;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.ScheduledPay;
import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.TypeOfAccDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;
import com.example.financialassistant.utils.JSONMainCurHelper;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.financialassistant.adapters.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL = "https://www.floatrates.com/daily/usd.json";
    private static final String CUR_JSON = "currents.json";
    private static final String BYN = "BYN";
    private static final String USD = "USD";
    private static final String US_DOLLAR = "U.S. Dollar";
    private static final String DOLLAR_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CurrentsDao currentsDao = DataBaseApp.getInstance(this).currentsDao();
        TypeOfAccDao typeOfAccDao = DataBaseApp.getInstance(this).typeOfAccDao();
        AccountsDao accountsDao = DataBaseApp.getInstance(this).accountsDao();
        ExpDao expDao = DataBaseApp.getInstance(this).expDao();
        TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(this).typeOfExpDao();

        DataCurrents.mainCur = JSONMainCurHelper.importFromJSON(this);
        if (DataCurrents.mainCur == null) {
            JSONMainCurHelper.exportToJSON(this, BYN);
        }
        DataCurrents.mainCur = JSONMainCurHelper.importFromJSON(this);
        DataCurrents.fromCurrency = USD;
        DataCurrents.toCurrency = DataCurrents.mainCur;
        //Парсинг последних валют
        List<Currents> currentsList = currentsDao.getAll();
        DataCurrents.currentList.addAll(currentsList);

        if (currentsDao.getById(1) != null && !isOnline(this)) {
            Toast.makeText(this, getResources().getString(R.string.Internet_out), Toast.LENGTH_LONG).show();
        }
        else if (currentsDao.getById(1) == null) {
            String res;
            //Парсинг курса валют
            AssetManager am = getAssets();
            InputStream is = null;
            StringBuilder s = new StringBuilder();
            try {
                is = am.open(CUR_JSON);
                InputStreamReader isr = new InputStreamReader(is);
                int data = isr.read();
                while (data != -1) {
                    s.append((char) data);
                    data = isr.read();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            res = s.toString();
            try {
                currentsDao.deleteAll();
                JSONObject jsonObjects = new JSONObject(res);
                Iterator<String> keys = jsonObjects.keys();
                DataCurrents.currentList.clear();
                Currents uSCurrent = new Currents();
                uSCurrent.setCur_ID(DOLLAR_ID);
                uSCurrent.setCur_Abbreviation(USD);
                uSCurrent.setCur_Name(US_DOLLAR);
                uSCurrent.setCur_OfficialRate(1);
                uSCurrent.setLastDate(new Date());
                DataCurrents.currentList.add(uSCurrent);
                currentsDao.insert(uSCurrent);
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject jsonObject = jsonObjects.getJSONObject(key);
                    Currents current = new Currents();
                    current.setCur_ID(jsonObject.getString("numericCode"));
                    current.setCur_Abbreviation(jsonObject.getString("alphaCode"));
                    current.setCur_Name(jsonObject.getString("name"));
                    current.setCur_OfficialRate(jsonObject.getDouble("rate"));
                    current.setLastDate(new Date());
                    DataCurrents.currentList.add(current);
                    currentsDao.insert(current);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (isOnline(this)) {
            GetData getData = new GetData();
            getData.execute();
        }
        //Ввод начальных значений

        if (typeOfAccDao.getAll().isEmpty()) {
            typeOfAccDao.insert(new TypeOfAccDB(getResources().getString(R.string.addAccountRadioTextCash)));
            typeOfAccDao.insert(new TypeOfAccDB(getResources().getString(R.string.addAccountRadioTextCard)));
            typeOfAccDao.insert(new TypeOfAccDB(getResources().getString(R.string.addAccountRadioTextElectronic)));
        }
        if (typeOfExpDao.getTypeExpDBById(-1) == null) {
            TypeOfExpDB typeOfExpDB = new TypeOfExpDB(-1,
                    getResources().getString(R.string.income), 0, 1);
            typeOfExpDao.insert(typeOfExpDB);
        }
        if (typeOfExpDao.getTypeExpDBById(-2) == null) {
            TypeOfExpDB typeOfExpDB = new TypeOfExpDB(-2,
                    getResources().getString(R.string.exChange), 0, 1);
            typeOfExpDao.insert(typeOfExpDB);
        }

        List<Accounts> accountsList = accountsDao.getAll();
        DataAccounts.accounts.clear();
        DataAccounts.accounts.addAll(accountsList);

        List<TypeOfExpenses> typeOfExpenses = typeOfExpDao.getAllExpType();
        DataTypesExpenses.typesOfExpenses.clear();
        DataTypesExpenses.typesOfExpenses.addAll(typeOfExpenses);

        List<Expenses> expenses = expDao.getLast20();
        DataExpenses.expenses.clear();
        DataExpenses.expenses.addAll(expenses);

        DebtsDao debtsDao = DataBaseApp.getInstance(this).debtsDao();
        List<Debts> debtsList = debtsDao.getAll();
        DataDebts.debts.clear();
        DataDebts.debts.addAll(debtsList);

        ScheduledPayDao scheduledPayDao = DataBaseApp.getInstance(this).scheduledPayDao();
        List<ScheduledPay> payList = scheduledPayDao.getAll();
        DataScheduledPay.scheduledPays.clear();
        DataScheduledPay.scheduledPays.addAll(payList);

        //Создание адаптера секций
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    //Создание новой операции расходов или доходов
    public void onClickNewOperation(View view) {
        if (DataAccounts.accounts.size() < 1) {
            Toast.makeText(view.getContext(),
                    getResources().getString(R.string.out_acc), Toast.LENGTH_LONG).show();
        }
        else if (DataTypesExpenses.typesOfExpenses.size() < 1) {
            Toast.makeText(view.getContext(),
                    getResources().getString(R.string.out_type_of_exp), Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, AddOperationActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataTypesExpenses.adapter != null)
            DataTypesExpenses.adapter.notifyDataSetChanged();
        if (DataExpenses.adapter != null)
            DataExpenses.adapter.notifyDataSetChanged();
        if (DataScheduledPay.adapter != null)
            DataScheduledPay.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_more_vert, menu);
        return true;
    }

    public void onClickAddNewDebts(View view) {
        Intent intent = new Intent(MainActivity.this, AddDebtsActivity.class);
        intent.putExtra("Action", "Create");
        startActivityForResult(intent, 0);
    }

    //Создание нового счета
    public void onClickNewActivityAccount(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
        intent.putExtra("Action", "Create");
        startActivityForResult(intent, 0);
    }

    //Выбор валоюты для конвертера "из валюты"
    public void onClickChooseCurrencyFirst(View view)
    {
        Intent intent = new Intent(MainActivity.this, CurrenciesActivity.class);
        intent.putExtra("Who", 1);
        startActivityForResult(intent, 0);
    }

    //Выбор валоюты для конвертера "в валюту"
    public void onClickChooseCurrencySecond(View view)
    {
        Intent intent = new Intent(MainActivity.this, CurrenciesActivity.class);
        intent.putExtra("Who", 2);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String action = data.getStringExtra("Action");
            switch (action) {
                case "UpdateAccounts":
                    DataAccounts.adapter.notifyDataSetChanged();
                    break;
                case "UpdateExpenses":
                    DataTypesExpenses.adapter.notifyDataSetChanged();
                    DataExpenses.adapter.notifyDataSetChanged();
                    DataScheduledPay.adapter.notifyDataSetChanged();
                    break;
                case "UpdateExpensesAndAccounts":
                    DataAccounts.adapter.notifyDataSetChanged();
                    DataTypesExpenses.adapter.notifyDataSetChanged();
                    DataExpenses.adapter.notifyDataSetChanged();
                    DataScheduledPay.adapter.notifyDataSetChanged();
                    break;
                case "UpdateDebts":
                    DataDebts.adapter.notifyDataSetChanged();
            }
        }
    }

    //Создание кнопок "Меню"
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) { //Нажатие на Настройки
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 0);
        }
        else if (item.getItemId() == R.id.new_type_expenses) { //Нажатие на Добваить новый тип расходов
            Intent intent = new Intent(MainActivity.this, AddNewTypeExpensesActivity.class);
            intent.putExtra("Action", "Create");
            startActivityForResult(intent, 0);
        }
        //else if (item.getItemId() == R.id.detailedStatistics) { //Нажатие на статистику
        //    Intent intent = new Intent(MainActivity.this, DetailedStatistics.class);
        //    startActivity(intent);
        //}
        return super.onOptionsItemSelected(item);
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @SuppressLint("StaticFieldLeak")
    public class GetData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder current = new StringBuilder();
            try {
                URL Url;
                HttpURLConnection urlConnection = null;
                try {
                    Url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) Url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    int data = isr.read();
                    while (data != -1) {
                        current.append((char) data);
                        data = isr.read();
                    }
                    return current.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return current.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                CurrentsDao currentsDao = DataBaseApp.getInstance(getApplicationContext()).currentsDao();
                JSONObject jsonObjects = new JSONObject(s);
                Iterator<String> keys = jsonObjects.keys();
                DataCurrents.currentList.clear();
                Currents uSCurrent = new Currents();
                uSCurrent.setCur_ID(DOLLAR_ID);
                uSCurrent.setCur_Abbreviation(USD);
                uSCurrent.setCur_Name(US_DOLLAR);
                uSCurrent.setCur_OfficialRate(1);
                uSCurrent.setLastDate(new Date());
                DataCurrents.currentList.add(uSCurrent);
                currentsDao.update(uSCurrent);
                while(keys.hasNext()) {
                    String key = keys.next();
                    JSONObject jsonObject = jsonObjects.getJSONObject(key);
                    Currents current = new Currents();
                    current.setCur_ID(jsonObject.getString("numericCode"));
                    current.setCur_Abbreviation(jsonObject.getString("alphaCode"));
                    current.setCur_Name(jsonObject.getString("name"));
                    current.setCur_OfficialRate(jsonObject.getDouble("rate"));
                    current.setLastDate(new Date());
                    DataCurrents.currentList.add(current);
                    currentsDao.update(current);
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.currencies_updated),
                        Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}