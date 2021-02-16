package com.example.financialassistant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.financialassistant.adapters.AccountsAdapter;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataExpenses;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.financialassistant.adapters.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

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
        DataAccounts.recyclerView.setAdapter(adapter);*/
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
        DataAccounts.names.add("qwe");
        DataAccounts.types.add("qwe");
        DataAccounts.currency.add("qwe");
        AccountsAdapter adapter = new AccountsAdapter();
        DataAccounts.recyclerView.setAdapter(adapter);
        /*TextView textView = (TextView) findViewById(R.id.test);
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
}