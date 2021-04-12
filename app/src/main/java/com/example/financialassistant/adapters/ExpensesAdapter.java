package com.example.financialassistant.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.models.Expenses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExpensesAdapter extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_expenses_with_date, parent, false);
        return new ExpensesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ExpensesAdapter.ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataExpenses.expenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameView;
        private final TextView valueView;
        private final TextView dateView;

        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_expense);
            valueView = (TextView) view.findViewById(R.id.value_expense);
            dateView = (TextView) view.findViewById(R.id.date_expense);
        }

        @SuppressLint("SetTextI18n")
        public void bindView(int position){
            Expenses expense = DataExpenses.expenses.get(position);
            nameView.setText(expense.getName());
            double tempD = expense.getValue() / 100.;
            @SuppressLint("DefaultLocale") String res = String.format("%.2f", tempD);
            valueView.setText(res + " " + expense.getCurrency());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd.MM.yyyy hh:mm");
            dateView.setText(simpleDateFormat.format(expense.getDate()));
        }
    }
}
