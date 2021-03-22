package com.example.financialassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.R;
import com.example.financialassistant.data.DataCurrents;
import com.example.financialassistant.models.Currents;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Currents> currentsList;

    public CurrenciesAdapter(Context context, List<Currents> currents) {
        this.currentsList = currents;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public CurrenciesAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_currencies, parent, false);
        return new CurrenciesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrenciesAdapter.ViewHolder holder, int position) {
        Currents cur = currentsList.get(position);
        holder.shortNameView.setText(cur.getCur_Abbreviation());
        holder.longNameView.setText(cur.getCur_Name());
    }

    @Override
    public int getItemCount() {
        return currentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView shortNameView;
        final TextView longNameView;

        ViewHolder(View view){
            super(view);
            shortNameView = (TextView) view.findViewById(R.id.short_name);
            longNameView = (TextView) view.findViewById(R.id.long_name);
        }
    }
}
