package com.example.financialassistant;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.financialassistant.dao.AccountsDao;
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
import com.example.financialassistant.database.DataBase;
import com.example.financialassistant.models.Accounts;
import com.example.financialassistant.models.Expenses;
import com.example.financialassistant.models.ScheduledPay;
import com.example.financialassistant.models.TypeOfExpenses;
import com.example.financialassistant.modelsDB.AccountsDB;
import com.example.financialassistant.modelsDB.ExpDB;
import com.example.financialassistant.modelsDB.TypeOfExpDB;

import java.util.List;

public class NotificationConfirm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int Id = intent.getIntExtra("Id", -1);
        DataBase instance = Room.databaseBuilder(context, DataBase.class, DataBaseApp.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        ScheduledPayDao scheduledPayDao = instance.scheduledPayDao();
        ScheduledPay scheduledPay = scheduledPayDao.getById(Id);
        if (scheduledPay != null) {
            AccountsDao accountsDao = instance.accountsDao();
            ExpDao expDao = instance.expDao();
            TypeOfExpDao typeOfExpDao = instance.typeOfExpDao();

            AccountsDB accountsDB = accountsDao.getAccDBByName(scheduledPay.getName_acc());
            TypeOfExpDB typeOfExpDB = typeOfExpDao.getTypeExpDBByName(scheduledPay.getName());

            Accounts account = null;
            int accIndex = -1;
            TypeOfExpenses typeOfExpense = null;
            int typeIndex = -1;

            if (DataAccounts.accounts.size() == 0) {
                List<Accounts> accountsList = accountsDao.getAll();
                DataAccounts.accounts.addAll(accountsList);
                List<TypeOfExpenses> typeOfExpenses = typeOfExpDao.getAllExpType();
                DataTypesExpenses.typesOfExpenses.addAll(typeOfExpenses);
                List<Expenses> expenses = expDao.getLast20();
                DataExpenses.expenses.addAll(expenses);
                List<ScheduledPay> payList = scheduledPayDao.getAll();
                DataScheduledPay.scheduledPays.addAll(payList);
            }

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
                    if (DataTypesExpenses.adapter != null)
                        DataTypesExpenses.adapter.notifyDataSetChanged();
                }

                Expenses newExp = new Expenses(scheduledPay.getName(), scheduledPay.getValue(), scheduledPay.getCur_Abbreviation(), scheduledPay.getName_acc());
                newExp.setRealValue(scheduledPay.getRealValue());
                newExp.id = (int) expDao.insert(new ExpDB(typeOfExpDB.id, newExp.getValue(), newExp.getRealValue(), accountsDB.currents_id, accountsDB.id, newExp.getDate_operation()));
                DataExpenses.expenses.add(0, newExp);
                if (DataExpenses.expenses.size() >= 20) {
                    DataExpenses.expenses.remove(20);
                }
                if (DataExpenses.adapter != null)
                    DataExpenses.adapter.notifyDataSetChanged();

                scheduledPayDao.deleteById(scheduledPay.id);

                for (int i = 0; i < DataScheduledPay.scheduledPays.size(); i++) {
                    if (DataScheduledPay.scheduledPays.get(i).id == scheduledPay.id) {
                        DataScheduledPay.scheduledPays.remove(i);
                        i--;
                    }
                }

                if (DataScheduledPay.adapter != null)
                    DataScheduledPay.adapter.notifyDataSetChanged();

                if (DataViews.emptyLastExp != null) {
                    DataViews.emptyLastExp.setVisibility(View.GONE);
                    if (DataScheduledPay.scheduledPays.size() == 0) {
                        DataViews.emptyScheduledPay.setVisibility(View.VISIBLE);
                        if (DataScheduledPay.recyclerView != null) {
                            DataScheduledPay.recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
                cancelNotification(context, Id);
            }
            else {
                Toast.makeText(context, "На счете недостаточно средств",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void cancelNotification(Context context, int notifyId) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(notifyId);
    }
}
