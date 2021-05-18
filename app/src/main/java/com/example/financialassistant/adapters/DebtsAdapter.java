package com.example.financialassistant.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;
import com.example.financialassistant.data.DataDebts;
import com.example.financialassistant.models.Debts;

import java.text.SimpleDateFormat;

public class DebtsAdapter  extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_expenses_with_date, parent, false);
        return new DebtsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DebtsAdapter.ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataDebts.debts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameView;
        private final TextView valueView;
        private final TextView dateView;
        Context context;

        ViewHolder(View view){
            super(view);
            context = view.getContext();
            nameView = (TextView) view.findViewById(R.id.name_expense);
            valueView = (TextView) view.findViewById(R.id.value_expense);
            dateView = (TextView) view.findViewById(R.id.date_expense);
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        public void bindView(int position){
            Debts debt = DataDebts.debts.get(position);
            nameView.setText(debt.getName());
            if (debt.isDebtor()) {
                nameView.setTextColor(ContextCompat.getColor(context, R.color.red_real));
            }
            else {
                nameView.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
            double tempD = debt.getValue() / 100.;
            @SuppressLint("DefaultLocale") String res = String.format("%.2f", tempD);
            valueView.setText(res + " " + debt.getCur_Abbreviation());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd.MM.yyyy");
            dateView.setText(simpleDateFormat.format(debt.getDeadLine().getTime()));
        }
    }
}
