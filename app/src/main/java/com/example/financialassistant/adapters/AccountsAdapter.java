package com.example.financialassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;
import com.example.financialassistant.data.Data;
import com.example.financialassistant.models.Accounts_View_Class;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        return Data.names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView nameView;
        private TextView valueView;

        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_accounts);
            valueView = (TextView) view.findViewById(R.id.value_accounts);
            view.setOnClickListener(this);
        }

        public void bindView(int position){
            nameView.setText(Data.names[position]);
            valueView.setText(Data.values[position]);
        }

        public void onClick(View view){
        }
    }
}
