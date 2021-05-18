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
import com.example.financialassistant.data.DataScheduledPay;
import com.example.financialassistant.models.ScheduledPay;

import java.text.SimpleDateFormat;

public class ScheduledPayAdapter extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_expenses_with_date, parent, false);
        return new ScheduledPayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ScheduledPayAdapter.ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataScheduledPay.scheduledPays.size();
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
            ScheduledPay scheduledPay = DataScheduledPay.scheduledPays.get(position);
            nameView.setText(scheduledPay.getName());
            double tempD = scheduledPay.getValue() / 100.;
            @SuppressLint("DefaultLocale") String res = String.format("%.2f", tempD);
            if (tempD < 0) {
                valueView.setTextColor(ContextCompat.getColor(context, R.color.red_real));
                valueView.setText(res + " " + scheduledPay.getCur_Abbreviation() + " <--- " + scheduledPay.getName_acc());
            }
            else {
                valueView.setTextColor(ContextCompat.getColor(context, R.color.green));
                valueView.setText(res + " " + scheduledPay.getCur_Abbreviation() + " ---> " + scheduledPay.getName_acc());
            }
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd.MM.yyyy HH:mm");
            dateView.setText(simpleDateFormat.format(scheduledPay.getDate_operation().getTime()));
        }
    }
}
