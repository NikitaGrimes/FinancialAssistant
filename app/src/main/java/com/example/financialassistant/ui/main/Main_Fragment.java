package com.example.financialassistant.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financialassistant.AddNewTypeExpensesActivity;
import com.example.financialassistant.R;
import com.example.financialassistant.adapters.DebtsAdapter;
import com.example.financialassistant.adapters.ExpensesAdapter;
import com.example.financialassistant.adapters.TypesOfExpensesAdapter;
import com.example.financialassistant.data.DataDebts;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Main_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Main_Fragment newInstance(String param1, String param2) {
        Main_Fragment fragment = new Main_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        DataTypesExpenses.recyclerView = (RecyclerView) view.findViewById(R.id.list_types_of_expenses);
        TypesOfExpensesAdapter adapter = new TypesOfExpensesAdapter();
        DataTypesExpenses.recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DataTypesExpenses.recyclerView.setLayoutManager(layoutManager);
        DataTypesExpenses.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), DataTypesExpenses.recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), AddNewTypeExpensesActivity.class);
                                intent.putExtra("Action", "Remake");
                                intent.putExtra("Num", position);
                                startActivityForResult(intent, 0);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );
        DataExpenses.recyclerView = (RecyclerView) view.findViewById(R.id.list_last_expenses);
        ExpensesAdapter expensesAdapter = new ExpensesAdapter();
        DataExpenses.recyclerView.setAdapter(expensesAdapter);
        DataDebts.recyclerView = (RecyclerView) view.findViewById(R.id.list_debts);
        DebtsAdapter debtsAdapter = new DebtsAdapter();
        DataDebts.recyclerView.setAdapter(debtsAdapter);
        return view;
    }
}