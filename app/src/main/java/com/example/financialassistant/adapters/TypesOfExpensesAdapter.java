package com.example.financialassistant.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.TypeOfExpenses;

public class TypesOfExpensesAdapter extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_expenses, parent,
                false);
        return new TypesOfExpensesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TypesOfExpensesAdapter.ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataTypesExpenses.typesOfExpenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameView;
        private final TextView valueView;

        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_expense);
            valueView = (TextView) view.findViewById(R.id.value_expense);
        }

        @SuppressLint("SetTextI18n")
        public void bindView(int position){
            TypeOfExpenses expense = DataTypesExpenses.typesOfExpenses.get(position);
            nameView.setText(expense.getName());
            double tempD = expense.getValue() / 100.;
            @SuppressLint("DefaultLocale") String res = String.format("%.2f", tempD);
            valueView.setText(res + " " + expense.getCur_Abbreviation());
        }
    }
}
