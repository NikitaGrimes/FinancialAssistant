package com.example.financialassistant.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.financialassistant.AddAccountActivity;
import com.example.financialassistant.R;
import com.example.financialassistant.adapters.AccountsAdapter;
import com.example.financialassistant.dao.AccountsDao;
import com.example.financialassistant.dao.ExpDao;
import com.example.financialassistant.dao.TypeOfExpDao;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.RecyclerItemClickListener;
import com.example.financialassistant.models.TypeOfExpenses;

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

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Привязка RecyclerView
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        context = view.getContext();
        DataAccounts.recyclerView = (RecyclerView) view.findViewById(R.id.list_accounts);
        AccountsAdapter adapter = new AccountsAdapter();
        DataAccounts.recyclerView.setAdapter(adapter);
        DataAccounts.adapter = adapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DataAccounts.recyclerView.setLayoutManager(layoutManager);
        DataAccounts.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), DataAccounts.recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                        intent.putExtra("Action", "Remake");
                        intent.putExtra("Num", position);
                        startActivityForResult(intent, 0);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        int num = position;
                        new AlertDialog.Builder(context)
                                .setMessage("Удалить счет? Будут удалены все записи с данным счетом!")
                                .setPositiveButton("Удалить", (dialogInterface, i) -> {
                                    Accounts account = DataAccounts.accounts.get(num);
                                    DataAccounts.accounts.remove(account);
                                    DataAccounts.adapter.notifyItemRemoved(num);
                                    ExpDao expDao = DataBaseApp.getInstance(context).expDao();
                                    expDao.deleteByAccId(account.id);
                                    AccountsDao accountsDao = DataBaseApp.getInstance(context).accountsDao();
                                    accountsDao.deleteById(account.id);
                                    for (int j = 0; j < DataExpenses.expenses.size(); j++) {
                                        if (DataExpenses.expenses.get(j).getName_acc().equals(account.getName_acc())) {
                                            DataExpenses.expenses.remove(j);
                                            j--;
                                        }
                                    }
                                    if (DataExpenses.adapter != null) {
                                        DataExpenses.adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Отмена", (dialogInterface, i) ->
                                {
                                    dialogInterface.cancel();
                                })
                                .setCancelable(false)
                                .create().show();
                    }
                })
        );
        return view;
    }
}