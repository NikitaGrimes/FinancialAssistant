package com.example.financialassistant.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.financialassistant.R;
import com.example.financialassistant.adapters.AccountsAdapter;
import com.example.financialassistant.data.DataAccounts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Accounts_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Accounts_Fragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Accounts_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Accounts_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Accounts_Fragment newInstance(String param1, String param2) {
        Accounts_Fragment fragment = new Accounts_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        DataAccounts.recyclerView = (RecyclerView) view.findViewById(R.id.list_accounts);
        AccountsAdapter adapter = new AccountsAdapter();
        DataAccounts.recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DataAccounts.recyclerView.setLayoutManager(layoutManager);
        return view;
    }
}