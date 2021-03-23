package com.example.financialassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;
import com.example.financialassistant.data.DataAccounts;

public class AccountsAdapter extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_accounts, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return DataAccounts.types.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView nameView;
        private final TextView valueView;
        private final TextView currencyView;

        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.type_accounts);
            valueView = (TextView) view.findViewById(R.id.name_accounts);
            currencyView = (TextView) view.findViewById(R.id.currency_accounts);
            view.setOnClickListener(this);
        }

        public void bindView(int position){
            nameView.setText(DataAccounts.types.get(position));
            valueView.setText(DataAccounts.names.get(position));
            currencyView.setText(DataAccounts.currency.get(position));
        }

        public void onClick(View view){

        }
    }
}
