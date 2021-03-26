package com.example.financialassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewExpensesTypeAdapter extends RecyclerView.Adapter<NewExpensesTypeAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<String> names;

    public NewExpensesTypeAdapter(Context context, List<String> names) {
        this.names = names;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public NewExpensesTypeAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_expenses_types, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewExpensesTypeAdapter.ViewHolder holder, int position) {
        String name = names.get(position);
        holder.nameView.setText(name);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name_list_expenses);
        }
    }
}