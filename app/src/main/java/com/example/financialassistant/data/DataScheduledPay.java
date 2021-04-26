package com.example.financialassistant.data;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialassistant.adapters.ScheduledPayAdapter;
import com.example.financialassistant.models.ScheduledPay;

import java.util.ArrayList;

public class DataScheduledPay {
    public static ArrayList<ScheduledPay> scheduledPays = new ArrayList<>();
    public static RecyclerView recyclerView;
    public static ScheduledPayAdapter adapter;
}
