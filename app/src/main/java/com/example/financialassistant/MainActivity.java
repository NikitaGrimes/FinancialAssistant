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
import com.example.financialassistant.dao.ExpDao;
import com.example.financialassistant.dao.TypeOfAccDao;
import com.example.financialassistant.dao.TypeOfExpDao;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.data.DataDebts;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Currents;
import com.example.financialassistant.models.Debts;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.TypeOfAccDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;
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

    private static String JSON_URL = "https://www.floatrates.com/daily/usd.json";

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
        if (typeOfAccDao.getAll().isEmpty()) {
            typeOfAccDao.insert(new TypeOfAccDB("Наличные"));
            typeOfAccDao.insert(new TypeOfAccDB("Карта"));
            typeOfAccDao.insert(new TypeOfAccDB("Электронные"));
        }
        if (typeOfExpDao.getTypeExpDBById(-1) == null) {
            TypeOfExpDB typeOfExpDB = new TypeOfExpDB(-1, "Доход", 0, 933);
            typeOfExpDao.insert(typeOfExpDB);
        }
        if (typeOfExpDao.getTypeExpDBById(-2) == null) {
            TypeOfExpDB typeOfExpDB = new TypeOfExpDB(-2,"Перевод", 0, 933);
            typeOfExpDao.insert(typeOfExpDB);
        }
        //Парсинг последних валют
        if (currentsDao.getById(1) != null && !isOnline(this)) {
            Toast.makeText(this, "Отсутствует подключение к Интернету", Toast.LENGTH_LONG).show();
            List<Currents> currentsList = currentsDao.getAll();
            DataCurrents.currentList.addAll(currentsList);
            DataCurrents.fromCurrency = "USD";
            DataCurrents.toCurrency = "BYN";
        }
        else if (isOnline(this)) {
            GetData getData = new GetData();
            getData.execute();
            DataCurrents.fromCurrency = "USD";
            DataCurrents.toCurrency = "BYN";
        }
        else {
            Toast.makeText(this, "Отсутствует подключение к Интернету", Toast.LENGTH_LONG).show();
            AssetManager assetManager = getAssets();
            InputStream inputStream = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                inputStream = assetManager.open("selectedСurrencies.json");
                InputStreamReader isr = new InputStreamReader(inputStream);
                int data = isr.read();
                while (data != -1) {
                    stringBuilder.append((char) data);
                    data = isr.read();
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String res = stringBuilder.toString();
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject firstObject = jsonObject.getJSONObject("from");
                DataCurrents.fromCurrency = firstObject.getString("currency");
                JSONObject secondObject = jsonObject.getJSONObject("to");
                DataCurrents.toCurrency = secondObject.getString("currency");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Парсинг курса валют
            AssetManager am = getAssets();
            InputStream is = null;
            StringBuilder s = new StringBuilder();
            try {
                is = am.open("currents.json");
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
                uSCurrent.setCur_ID("1");
                uSCurrent.setCur_Abbreviation("USD");
                uSCurrent.setCur_Name("U.S. Dollar");
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
        //Ввод начальных значений
        /*Currents tempCur = currentsDao.getByAbr("BYN");
        int id_temp_id = tempCur.getCur_ID();
        accountsDao.insert(new AccountsDB("Family", 3500, id_temp_id, 2));
        tempCur = currentsDao.getByAbr("EUR");
        id_temp_id = tempCur.getCur_ID();
        accountsDao.insert(new AccountsDB("Nata", 2000, id_temp_id, 3));
        tempCur = currentsDao.getByAbr("USD");
        id_temp_id = tempCur.getCur_ID();
        accountsDao.insert(new AccountsDB("MySelf", 3000, id_temp_id, 1));*/

        /*Accounts account = new Accounts("Family", "Карта", 3500, "BYN");
        DataAccounts.accounts.add(account);
        account = new Accounts("Nata", "Электронные", 2000, "EUR");
        DataAccounts.accounts.add(account);
        account = new Accounts("MySelf", "Наличные", 3000, "USD");
        DataAccounts.accounts.add(account);*/

        List<Accounts> accountsList = accountsDao.getAll();
        DataAccounts.accounts.addAll(accountsList);

        /*Currents tempCur = currentsDao.getByAbr("BYN");
        int id_temp_id = tempCur.getCur_ID();
        typeOfExpDao.insert(new TypeOfExpDB("Всякое", 1000, id_temp_id));
        typeOfExpDao.insert(new TypeOfExpDB("Подарки", 1500, id_temp_id));
        typeOfExpDao.insert(new TypeOfExpDB("ФастФуд", 800, id_temp_id));*/

        /*TypeOfExpenses expense = new TypeOfExpenses("Всякое", 1000, "BYN");
        DataTypesExpenses.typesOfExpenses.add(expense);
        expense = new TypeOfExpenses("Подарки", 1500, "BYN");
        DataTypesExpenses.typesOfExpenses.add(expense);
        expense = new TypeOfExpenses("ФастФуд", 800, "BYN");
        DataTypesExpenses.typesOfExpenses.add(expense);*/

        List<TypeOfExpenses> typeOfExpenses = typeOfExpDao.getAllExpType();
        DataTypesExpenses.typesOfExpenses.addAll(typeOfExpenses);

        /*Currents tempCur = currentsDao.getByAbr("BYN");
        int id_temp_id = tempCur.getCur_ID();
        expDao.insert(new ExpDB(1, -200, -200, id_temp_id, 1, new GregorianCalendar()));
        expDao.insert(new ExpDB(2, -1000, -1000, id_temp_id, 1, new GregorianCalendar()));
        expDao.insert(new ExpDB(3, -200, -200, id_temp_id, 1, new GregorianCalendar()));
        expDao.insert(new ExpDB(1, -200, -200, id_temp_id, 1, new GregorianCalendar()));*/

        /*Expenses expenses = new Expenses("Всякое", -200, "BYN", "Family");
        DataExpenses.expenses.add(expenses);
        expenses = new Expenses("Подарки", -1000, "BYN", "Family");
        DataExpenses.expenses.add(expenses);
        expenses = new Expenses("ФастФуд", -200, "BYN", "Family");
        DataExpenses.expenses.add(expenses);
        expenses = new Expenses("Всякое", -200, "BYN", "Family");
        DataExpenses.expenses.add(expenses);*/

        List<Expenses> expenses = expDao.getLast20();
        DataExpenses.expenses.addAll(expenses);

        Debts debts = new Debts("Паше", 4000, "BYN", true);
        DataDebts.debts.add(debts);
        debts = new Debts("Лере", 1000, "BYN", true);
        DataDebts.debts.add(debts);
        debts = new Debts("Нате", 500, "BYN", false);
        DataDebts.debts.add(debts);

        //Создание адаптера секций
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, String.valueOf(DataAccounts.names.size()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                DataAccounts.names.add("qwe");
                DataAccounts.values.add("qwe");
                AccountsAdapter adapter = new AccountsAdapter();
                DataAccounts.recyclerView.setAdapter(adapter);
                Intent intent = new Intent(MainActivity.this, AddOperationActivity.class);
                startActivityForResult(intent, 0);
            }
        });*/
    }

    //Создание новой операции расходов или доходов
    public void onClickNewOperation(View view) {
        /*Snackbar.make(view, String.valueOf(DataAccounts.names.size()), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        DataAccounts.names.add("qwe");
        DataAccounts.values.add("qwe");
        AccountsAdapter adapter = new AccountsAdapter();
        DataAccounts.recyclerView.setAdapter(adapter);
        boolean isCon = isOnline(this);
        if(isCon)
            Snackbar.make(view, String.valueOf(DataAccounts.names.size()), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
        Intent intent = new Intent(MainActivity.this, AddOperationActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_more_vert, menu);
        return true;
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
                    //AccountsAdapter adapter = new AccountsAdapter();
                    //DataAccounts.recyclerView.setAdapter(adapter);
                    DataAccounts.adapter.notifyDataSetChanged();
                    break;
                case "UpdateExpenses":
                    //TypesOfExpensesAdapter adapter = new TypesOfExpensesAdapter();
                    //DataTypesExpenses.recyclerView.setAdapter(adapter);
                    DataTypesExpenses.adapter.notifyDataSetChanged();
                    DataExpenses.adapter.notifyDataSetChanged();
                    break;
                case "UpdateExpensesAndAccounts":
                    //AccountsAdapter adapter0 = new AccountsAdapter();
                    //DataAccounts.recyclerView.setAdapter(adapter0);
                    DataAccounts.adapter.notifyDataSetChanged();
                    //TypesOfExpensesAdapter adapter1 = new TypesOfExpensesAdapter();
                    //DataTypesExpenses.recyclerView.setAdapter(adapter1);
                    DataTypesExpenses.adapter.notifyDataSetChanged();
                    //ExpensesAdapter adapter2 = new ExpensesAdapter();
                    //DataExpenses.recyclerView.setAdapter(adapter2);
                    DataExpenses.adapter.notifyDataSetChanged();
                    break;
            }
        }
        /*DataAccounts.names.add("qwe");
        DataAccounts.types.add("qwe");
        DataAccounts.currency.add("qwe");
        AccountsAdapter adapter = new AccountsAdapter();
        DataAccounts.recyclerView.setAdapter(adapter);
        TextView textView = (TextView) findViewById(R.id.test);
        if (resultCode == RESULT_OK) {
            String thiefname = data.getStringExtra("qwe");
            textView.setText(thiefname);
        }*/
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
        return super.onOptionsItemSelected(item);
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

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
                uSCurrent.setCur_ID("1");
                uSCurrent.setCur_Abbreviation("USD");
                uSCurrent.setCur_Name("U.S. Dollar");
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
                Toast.makeText(getApplicationContext(), "Курс валют обновлен",
                        Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}