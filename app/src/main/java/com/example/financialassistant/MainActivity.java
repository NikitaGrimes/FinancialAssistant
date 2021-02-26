package com.example.financialassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.models.Currents;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String JSON_URL_1 = "https://www.nbrb.by/api/exrates/rates?periodicity=1";
    private static String JSON_URL_0 = "https://www.nbrb.by/api/exrates/rates?periodicity=0";

    List<Currents> currentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DataAccounts.currency.add("curr");
        DataAccounts.names.add("name");
        DataAccounts.types.add("type");
        DataExpenses.names.add("name");
        DataExpenses.values.add("value");
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        currentsList = new ArrayList<>();
        if (isOnline(this)) {
            GetData getData = new GetData();
            getData.execute();
        }
        else {
            Toast.makeText(this, "Отсутствует подключение к Интернету", Toast.LENGTH_LONG).show();
        }

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

    public void onClickNewActivityAccount(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 0);
        }
        else if (item.getItemId() == R.id.new_type_expenses) {
            Intent intent = new Intent(MainActivity.this, AddNewTypeExpensesActivity.class);
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
                    Url = new URL(JSON_URL_1);
                    urlConnection = (HttpURLConnection) Url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    int data = isr.read();
                    while (data != -1) {
                        if ((char) data == ']')
                            current.append(',');
                        else
                            current.append((char) data);
                        data = isr.read();
                    }
                    urlConnection.disconnect();
                    Url = new URL(JSON_URL_0);
                    urlConnection = (HttpURLConnection) Url.openConnection();
                    is = urlConnection.getInputStream();
                    isr = new InputStreamReader(is);
                    data = isr.read();
                    while (data != -1) {
                        if ((char) data != '[')
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
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Currents current = new Currents();
                    current.setCur_ID(jsonObject.getInt("Cur_ID"));
                    current.setCur_Abbreviation(jsonObject.getString("Cur_Abbreviation"));
                    current.setCur_Scale(jsonObject.getInt("Cur_Scale"));
                    current.setCur_Name(jsonObject.getString("Cur_Name"));
                    current.setCur_OfficialRate(jsonObject.getDouble("Cur_OfficialRate"));
                    current.setLastDate(new Date());
                    currentsList.add(current);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}