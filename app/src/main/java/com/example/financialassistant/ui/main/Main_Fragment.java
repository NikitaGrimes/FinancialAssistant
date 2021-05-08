package com.example.financialassistant.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialassistant.AddDebtsActivity;
import com.example.financialassistant.AddOperationActivity;
import com.example.financialassistant.R;
import com.example.financialassistant.adapters.DebtsAdapter;
import com.example.financialassistant.adapters.ExpensesAdapter;
import com.example.financialassistant.adapters.ScheduledPayAdapter;
import com.example.financialassistant.adapters.TypesOfExpensesAdapter;
import com.example.financialassistant.dao.AccountsDao;
import com.example.financialassistant.dao.DebtsDao;
import com.example.financialassistant.dao.ExpDao;
import com.example.financialassistant.dao.ScheduledPayDao;
import com.example.financialassistant.dao.TypeOfExpDao;
import com.example.financialassistant.data.DataAccounts;
import com.example.financialassistant.data.DataBaseApp;
import com.example.financialassistant.data.DataDebts;
import com.example.financialassistant.data.DataExpenses;
import com.example.financialassistant.data.DataScheduledPay;
import com.example.financialassistant.data.DataTypesExpenses;
import com.example.financialassistant.data.DataViews;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Debts;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.RecyclerItemClickListener;
import com.example.financialassistant.models.ScheduledPay;
import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.AccountsDB;
import com.example.financialassistant.modelsDB.DebtsDB;
import com.example.financialassistant.modelsDB.ExpDB;
import com.example.financialassistant.modelsDB.ScheduledPayDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

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
    View newView;

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
    public void onResume() {
        super.onResume();
        checkEmptyViews(newView);
    }

    private void createThreeButtonsAlertDialog(int num) {
        Context context = this.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(this.getContext()));
        builder.setTitle("Выберите действие");
        builder.setNegativeButton("Изменить",
                (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), AddDebtsActivity.class);
                    intent.putExtra("Action", "Remake");
                    intent.putExtra("Num", num);
                    startActivityForResult(intent, 0);
                });
        builder.setPositiveButton("Погасить (частично)",
                (dialog, which) -> {
                    LayoutInflater li = LayoutInflater.from(context);
                    @SuppressLint("InflateParams") View promptsView =
                            li.inflate(R.layout.pay_for_debt, null);
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                    mDialogBuilder.setView(promptsView);
                    final TextView debtName = (TextView) promptsView.
                            findViewById(R.id.name_of_debt);
                    final Spinner accSpinner = (Spinner) promptsView.
                            findViewById(R.id.choose_debt_for_pay_spinner);
                    final EditText valueInput = (EditText) promptsView.
                            findViewById(R.id.value_for_pay_input);

                    Debts debt = DataDebts.debts.get(num);

                    String[] dataAcc = new String[DataAccounts.accounts.size()];
                    int i = 0;
                    for (Accounts account : DataAccounts.accounts) {
                        dataAcc[i] = account.getName_acc();
                        i++;
                    }
                    ArrayAdapter<String> adapterAcc = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_item, dataAcc);
                    adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    accSpinner.setAdapter(adapterAcc);

                    debtName.setText(debt.getName());

                    mDialogBuilder.setCancelable(false).setPositiveButton("Погасить",
                            (dialogInterface ,id) -> {
                                int pos = accSpinner.getSelectedItemPosition();
                                Accounts accounts = DataAccounts.accounts.get(pos);
                                if (!accounts.getCur_Abbreviation().equals(debt.getCur_Abbreviation())) {
                                    Toast.makeText(context, "Неверный счет для оплаты", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    int pay_value = (int) (Double.parseDouble(valueInput.getText().toString()) * 100 + 0.1);
                                    if (debt.isDebtor()) {
                                        if (pay_value > accounts.getValue()) {
                                            Toast.makeText(context, "На счете мало средств", Toast.LENGTH_LONG).show();
                                        } else {
                                            AccountsDao accountsDao = DataBaseApp.getInstance(context).accountsDao();
                                            DebtsDao debtsDao = DataBaseApp.getInstance(context).debtsDao();
                                            AccountsDB accountsDB = accountsDao.getAccDBById(accounts.id);
                                            DebtsDB debtsDB = debtsDao.getDebtDBById(debt.id);
                                            if (debt.getValue() <= pay_value) {
                                                accounts.setValue(accounts.getValue() - debt.getValue());
                                                DataAccounts.accounts.set(pos, accounts);
                                                accountsDB.value = accounts.getValue();
                                                accountsDao.update(accountsDB);
                                                debtsDao.delete(debtsDB);
                                                DataDebts.debts.remove(debt);
                                            }
                                            else {
                                                accounts.setValue(accounts.getValue() - pay_value);
                                                DataAccounts.accounts.set(pos, accounts);
                                                accountsDB.value = accounts.getValue();
                                                accountsDao.update(accountsDB);
                                                debt.setValue(debt.getValue() - pay_value);
                                                debtsDB.value = debt.getValue();
                                                DataDebts.debts.set(num, debt);
                                                debtsDao.update(debtsDB);
                                            }
                                            DataDebts.adapter.notifyDataSetChanged();
                                            if (DataAccounts.adapter != null)
                                                DataAccounts.adapter.notifyDataSetChanged();
                                        }
                                    }
                                    else {
                                        AccountsDao accountsDao = DataBaseApp.getInstance(context).accountsDao();
                                        DebtsDao debtsDao = DataBaseApp.getInstance(context).debtsDao();
                                        AccountsDB accountsDB = accountsDao.getAccDBById(accounts.id);
                                        DebtsDB debtsDB = debtsDao.getDebtDBById(debt.id);
                                        if (debt.getValue() <= pay_value) {
                                            accounts.setValue(accounts.getValue() + debt.getValue());
                                            DataAccounts.accounts.set(pos, accounts);
                                            accountsDB.value = accounts.getValue();
                                            accountsDao.update(accountsDB);
                                            debtsDao.delete(debtsDB);
                                            DataDebts.debts.remove(debt);
                                        }
                                        else {
                                            accounts.setValue(accounts.getValue() + pay_value);
                                            DataAccounts.accounts.set(pos, accounts);
                                            accountsDB.value = accounts.getValue();
                                            accountsDao.update(accountsDB);
                                            debt.setValue(debt.getValue() - pay_value);
                                            debtsDB.value = debt.getValue();
                                            DataDebts.debts.set(num, debt);
                                            debtsDao.update(debtsDB);
                                        }
                                        DataDebts.adapter.notifyDataSetChanged();
                                        if (DataAccounts.adapter != null)
                                            DataAccounts.adapter.notifyDataSetChanged();
                                    }
                                    dialogInterface.cancel();
                                    dialog.cancel();
                                    checkEmptyViews(newView);
                                }
                            })
                            .setNegativeButton("Отмена",
                                    (dialogInterface, id) -> {
                                dialogInterface.cancel();
                                dialog.cancel();
                            });
                    AlertDialog alertDialog = mDialogBuilder.create();
                    alertDialog.show();
                });
        builder.setNeutralButton("Отмена",
                (dialog, which) -> {
                });

        builder.show();
    }

    private void checkEmptyViews(View view) {
        TextView noType = (TextView) view.findViewById(R.id.no_exp_type_TV);
        if (DataTypesExpenses.typesOfExpenses.isEmpty())
            noType.setVisibility(View.VISIBLE);
        else
            noType.setVisibility(View.GONE);
        TextView noExp = (TextView) view.findViewById(R.id.no_exp_TV);
        if (DataExpenses.expenses.isEmpty())
            noExp.setVisibility(View.VISIBLE);
        else
            noExp.setVisibility(View.GONE);
        TextView noScheduledPay = (TextView) view.findViewById(R.id.no_sch_pay_TV);
        if (DataScheduledPay.scheduledPays.isEmpty())
            noScheduledPay.setVisibility(View.VISIBLE);
        else
            noScheduledPay.setVisibility(View.GONE);
        TextView noDebts = (TextView) view.findViewById(R.id.no_debts_TV);
        if (DataDebts.debts.isEmpty())
            noDebts.setVisibility(View.VISIBLE);
        else
            noDebts.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        checkEmptyViews(view);
        newView = view;
        DataViews.emptyLastExp = (TextView) view.findViewById(R.id.no_exp_TV);
        DataViews.emptyScheduledPay = (TextView) view.findViewById(R.id.no_sch_pay_TV);
        DataTypesExpenses.recyclerView = (RecyclerView) view.findViewById(R.id.list_types_of_expenses);
        //DataTypesExpenses.filterOfExpenses = new ArrayList<>(DataTypesExpenses.typesOfExpenses);
        TypesOfExpensesAdapter adapter = new TypesOfExpensesAdapter();
        DataTypesExpenses.recyclerView.setAdapter(adapter);
        DataTypesExpenses.adapter = adapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DataTypesExpenses.recyclerView.setLayoutManager(layoutManager);

        DataExpenses.recyclerView = (RecyclerView) view.findViewById(R.id.list_last_expenses);
        ExpensesAdapter expensesAdapter = new ExpensesAdapter();
        DataExpenses.recyclerView.setAdapter(expensesAdapter);
        DataExpenses.adapter = expensesAdapter;

        DataScheduledPay.recyclerView = (RecyclerView) view.findViewById(R.id.list_scheduled_pay);
        ScheduledPayAdapter scheduledPayAdapter = new ScheduledPayAdapter();
        DataScheduledPay.recyclerView.setAdapter(scheduledPayAdapter);
        DataScheduledPay.adapter = scheduledPayAdapter;

        DataDebts.recyclerView = (RecyclerView) view.findViewById(R.id.list_debts);
        DebtsAdapter debtsAdapter = new DebtsAdapter();
        DataDebts.recyclerView.setAdapter(debtsAdapter);
        DataDebts.adapter = debtsAdapter;
        DataDebts.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), DataTypesExpenses.recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                createThreeButtonsAlertDialog(position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView,
                                  @NotNull RecyclerView.ViewHolder viewHolder,
                                  @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (!DataExpenses.expenses.isEmpty()) {
                    Expenses expense = DataExpenses.expenses.get(viewHolder.getAdapterPosition());
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Удалить запись?")
                            .setPositiveButton("Удалить", (dialogInterface, i) -> {
                            DataExpenses.expenses.remove(expense);
                            ExpDao expDao = DataBaseApp.getInstance(view.getContext()).expDao();
                            expDao.deleteById(expense.id);
                            DataExpenses.adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            if (expense.getValue() < 0) {
                                for (TypeOfExpenses typeOfExpense : DataTypesExpenses.typesOfExpenses) {
                                    if (typeOfExpense.getName().equals(expense.getName())) {
                                        typeOfExpense.setValue(typeOfExpense.getValue() + expense.getRealValue());
                                        TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(view.getContext()).typeOfExpDao();
                                        TypeOfExpDB typeOfExpDB = typeOfExpDao.getTypeExpDBById(typeOfExpense.id);
                                        typeOfExpDB.value += + expense.getRealValue();
                                        typeOfExpDao.update(typeOfExpDB);
                                        break;
                                    }
                                }
                                DataTypesExpenses.adapter.notifyDataSetChanged();
                            }
                            for (Accounts account : DataAccounts.accounts) {
                                if (account.getName_acc().equals(expense.getName_acc())) {
                                    account.setValue(account.getValue() - expense.getValue());
                                    AccountsDao accDao = DataBaseApp.getInstance(view.getContext()).accountsDao();
                                    AccountsDB accDB = accDao.getAccDBById(account.id);
                                    accDB.value = account.getValue();
                                    accDao.update(accDB);
                                    break;
                                }
                            }
                            DataAccounts.adapter.notifyDataSetChanged();
                            checkEmptyViews(newView);
                            })
                            .setNegativeButton("Отмена", (dialogInterface, i) -> Objects.requireNonNull(DataExpenses.adapter).notifyItemChanged(viewHolder.getAdapterPosition()))
                            .setCancelable(false)
                            .create().show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(DataExpenses.recyclerView);

        DataScheduledPay.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), DataTypesExpenses.recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (!DataScheduledPay.scheduledPays.isEmpty()) {
                                    ScheduledPay scheduledPay = DataScheduledPay.scheduledPays.get(position);
                                    new AlertDialog.Builder(view.getContext())
                                            .setMessage("Совершить операцию?")
                                            .setPositiveButton("Совершить", (dialogInterface, i) -> {
                                                ScheduledPayDao scheduledPayDao = DataBaseApp.getInstance(view.getContext()).scheduledPayDao();
                                                AccountsDao accountsDao = DataBaseApp.getInstance(view.getContext()).accountsDao();
                                                ExpDao expDao = DataBaseApp.getInstance(view.getContext()).expDao();
                                                TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(view.getContext()).typeOfExpDao();

                                                AccountsDB accountsDB = accountsDao.getAccDBByName(scheduledPay.getName_acc());
                                                TypeOfExpDB typeOfExpDB = typeOfExpDao.getTypeExpDBByName(scheduledPay.getName());
                                                Accounts account = null;
                                                int accIndex = -1;
                                                TypeOfExpenses typeOfExpense = null;
                                                int typeIndex = -1;

                                                for (int j = 0; j < DataAccounts.accounts.size(); j++) {
                                                    account = DataAccounts.accounts.get(j);
                                                    if (account.getName_acc().equals(scheduledPay.getName_acc())) {
                                                        accIndex = j;
                                                        break;
                                                    }
                                                }

                                                for (int j = 0; j < DataTypesExpenses.typesOfExpenses.size(); j++) {
                                                    typeOfExpense = DataTypesExpenses.typesOfExpenses.get(j);
                                                    if (typeOfExpense.getName().equals(scheduledPay.getName())) {
                                                        typeIndex = j;
                                                        break;
                                                    }
                                                }

                                                if (account != null && account.getValue() >= scheduledPay.getValue()) {

                                                    account.setValue(account.getValue() + scheduledPay.getValue());
                                                    accountsDB.value = account.getValue();
                                                    DataAccounts.accounts.set(accIndex, account);
                                                    accountsDao.update(accountsDB);
                                                    if (DataAccounts.adapter != null) {
                                                        DataAccounts.adapter.notifyDataSetChanged();
                                                    }

                                                    if (typeIndex != -1) {
                                                        typeOfExpense.setValue(typeOfExpense.getValue() - scheduledPay.getRealValue());
                                                        typeOfExpDB.value -= scheduledPay.getRealValue();
                                                        DataTypesExpenses.typesOfExpenses.set(typeIndex, typeOfExpense);
                                                        typeOfExpDao.update(typeOfExpDB);
                                                        DataTypesExpenses.adapter.notifyDataSetChanged();
                                                    }

                                                    Expenses newExp = new Expenses(scheduledPay.getName(), scheduledPay.getValue(), scheduledPay.getCur_Abbreviation(), scheduledPay.getName_acc());
                                                    newExp.setRealValue(scheduledPay.getRealValue());
                                                    newExp.id = (int) expDao.insert(new ExpDB(typeOfExpDB.id, newExp.getValue(), newExp.getRealValue(), accountsDB.currents_id, accountsDB.id, newExp.getDate_operation()));
                                                    DataExpenses.expenses.add(0, newExp);
                                                    if (DataExpenses.expenses.size() >= 20) {
                                                        DataExpenses.expenses.remove(20);
                                                    }
                                                    DataExpenses.adapter.notifyDataSetChanged();

                                                    scheduledPayDao.deleteById(scheduledPay.id);
                                                    DataScheduledPay.scheduledPays.remove(scheduledPay);
                                                    DataScheduledPay.adapter.notifyDataSetChanged();
                                                    checkEmptyViews(newView);
                                                }
                                                else {
                                                    Toast.makeText(view.getContext(), "На счете недостаточно средств",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.cancel())
                                            .setCancelable(false)
                                            .create().show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );

        ItemTouchHelper.SimpleCallback touchHelperCallbackSchPay = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView,
                                  @NotNull RecyclerView.ViewHolder viewHolder,
                                  @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (!DataScheduledPay.scheduledPays.isEmpty()) {
                    ScheduledPay scheduledPay = DataScheduledPay.scheduledPays.get(viewHolder.getAdapterPosition());
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Удалить запись?")
                            .setPositiveButton("Удалить", (dialogInterface, i) -> {
                                ScheduledPayDao scheduledPayDao = DataBaseApp.getInstance(view.getContext()).scheduledPayDao();
                                scheduledPayDao.deleteById(scheduledPay.id);
                                DataScheduledPay.scheduledPays.remove(scheduledPay);
                                DataScheduledPay.adapter.notifyDataSetChanged();
                                checkEmptyViews(newView);
                            })
                            .setNegativeButton("Отмена", (dialogInterface, i) -> Objects.requireNonNull(DataScheduledPay.adapter).notifyItemChanged(viewHolder.getAdapterPosition()))
                            .setCancelable(false)
                            .create().show();
                }
            }
        };
        ItemTouchHelper itemTouchHelperSchPay = new ItemTouchHelper(touchHelperCallbackSchPay);
        itemTouchHelperSchPay.attachToRecyclerView(DataScheduledPay.recyclerView);

        Spinner filter = (Spinner) view.findViewById(R.id.exp_filter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.exp_filters);
                switch (choose[selectedItemPosition]) {
                    case "Нет":
                        TypeOfExpDao typeOfExpDao = DataBaseApp.getInstance(view.getContext()).typeOfExpDao();
                        List<TypeOfExpenses> typeOfExpensesList = typeOfExpDao.getAllExpType();
                        DataTypesExpenses.typesOfExpenses.clear();
                        DataTypesExpenses.typesOfExpenses.addAll(typeOfExpensesList);
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                    case "7 дней":
                        GregorianCalendar gregorianCalendar = new GregorianCalendar();
                        gregorianCalendar.add(Calendar.DAY_OF_YEAR, -7);
                        ExpDao expDao = DataBaseApp.getInstance(view.getContext()).expDao();
                        List<Expenses> expensesList = expDao.getByTime(gregorianCalendar);
                        for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                            typeOfExpenses.setValue(0);
                        }
                        for (Expenses expense : expensesList) {
                            for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                                if (expense.getName().equals(typeOfExpenses.getName())) {
                                    typeOfExpenses.setValue(typeOfExpenses.getValue() - expense.getRealValue());
                                    break;
                                }
                            }
                        }
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                    case "30 дней":
                        GregorianCalendar gregorianCalendar30 = new GregorianCalendar();
                        gregorianCalendar30.add(Calendar.DAY_OF_YEAR, -30);
                        ExpDao expDao30 = DataBaseApp.getInstance(view.getContext()).expDao();
                        List<Expenses> expensesList30 = expDao30.getByTime(gregorianCalendar30);
                        for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                            typeOfExpenses.setValue(0);
                        }
                        for (Expenses expense : expensesList30) {
                            for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                                if (expense.getName().equals(typeOfExpenses.getName())) {
                                    typeOfExpenses.setValue(typeOfExpenses.getValue() - expense.getRealValue());
                                    break;
                                }
                            }
                        }
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                    case "Текущий месяц":
                        GregorianCalendar gregorianCalendarNM = new GregorianCalendar();
                        gregorianCalendarNM.set(Calendar.DAY_OF_MONTH, 0);
                        gregorianCalendarNM.set(Calendar.HOUR, 0);
                        gregorianCalendarNM.set(Calendar.MINUTE, 0);
                        ExpDao expDaoNM = DataBaseApp.getInstance(view.getContext()).expDao();
                        List<Expenses> expensesListNM = expDaoNM.getByTime(gregorianCalendarNM);
                        for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                            typeOfExpenses.setValue(0);
                        }
                        for (Expenses expense : expensesListNM) {
                            for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                                if (expense.getName().equals(typeOfExpenses.getName())) {
                                    typeOfExpenses.setValue(typeOfExpenses.getValue() - expense.getRealValue());
                                    break;
                                }
                            }
                        }
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                    case "3 месяца":
                        GregorianCalendar gregorianCalendar3M = new GregorianCalendar();
                        gregorianCalendar3M.add(Calendar.MONTH, -3);
                        ExpDao expDao3M = DataBaseApp.getInstance(view.getContext()).expDao();
                        List<Expenses> expensesList3M = expDao3M.getByTime(gregorianCalendar3M);
                        for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                            typeOfExpenses.setValue(0);
                        }
                        for (Expenses expense : expensesList3M) {
                            for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                                if (expense.getName().equals(typeOfExpenses.getName())) {
                                    typeOfExpenses.setValue(typeOfExpenses.getValue() - expense.getRealValue());
                                    break;
                                }
                            }
                        }
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                    case "1 год":
                        GregorianCalendar gregorianCalendar1Y = new GregorianCalendar();
                        gregorianCalendar1Y.add(Calendar.YEAR, -1);
                        ExpDao expDao1Y = DataBaseApp.getInstance(view.getContext()).expDao();
                        List<Expenses> expensesList1Y = expDao1Y.getByTime(gregorianCalendar1Y);
                        for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                            typeOfExpenses.setValue(0);
                        }
                        for (Expenses expense : expensesList1Y) {
                            for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                                if (expense.getName().equals(typeOfExpenses.getName())) {
                                    typeOfExpenses.setValue(typeOfExpenses.getValue() - expense.getRealValue());
                                    break;
                                }
                            }
                        }
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                    case "3 года":
                        GregorianCalendar gregorianCalendar3Y = new GregorianCalendar();
                        gregorianCalendar3Y.add(Calendar.YEAR, -3);
                        ExpDao expDao3Y = DataBaseApp.getInstance(view.getContext()).expDao();
                        List<Expenses> expensesList3Y = expDao3Y.getByTime(gregorianCalendar3Y);
                        for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                            typeOfExpenses.setValue(0);
                        }
                        for (Expenses expense : expensesList3Y) {
                            for (TypeOfExpenses typeOfExpenses : DataTypesExpenses.typesOfExpenses) {
                                if (expense.getName().equals(typeOfExpenses.getName())) {
                                    typeOfExpenses.setValue(typeOfExpenses.getValue() - expense.getRealValue());
                                    break;
                                }
                            }
                        }
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }
}