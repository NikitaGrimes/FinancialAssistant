package com.example.financialassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_account);
    }

    public void onClickAddAccount(View view)
    {
        Intent answerIntent = new Intent();
        answerIntent.putExtra("qwe", "Сраный пёсик");
        setResult(RESULT_OK, answerIntent);
        finish();
    }
}