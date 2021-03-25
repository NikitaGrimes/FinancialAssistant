package com.example.financialassistant.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financialassistant.Currencies;
import com.example.financialassistant.MainActivity;
import com.example.financialassistant.R;
import com.example.financialassistant.data.DataCurrents;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Converter_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Converter_Fragment extends Fragment implements View.OnLongClickListener, View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView fromConvert;
    TextView toConvert;
    Button fromSpinner;
    Button toSpinner;
    String enteredValue;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Converter_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calculator_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Converter_Fragment newInstance(String param1, String param2) {
        Converter_Fragment fragment = new Converter_Fragment();
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

        View rootView =  inflater.inflate(R.layout.fragment_converter, container, false);
        fromConvert = (TextView) rootView.findViewById(R.id.firstEditTextNumberDecimal);
        toConvert = (TextView)rootView.findViewById(R.id.secondEditTextNumberDecimal);
        fromSpinner = (Button) rootView.findViewById(R.id.first_spinner_calculator);
        toSpinner = (Button) rootView.findViewById(R.id.second_spinner_calculator);
        int[] buttonsID = { R.id.change_currency_button, R.id.calc_0_button, R.id.calc_1_button,
                R.id.calc_2_button, R.id.calc_3_button, R.id.calc_4_button, R.id.calc_5_button,
                R.id.calc_6_button, R.id.calc_7_button, R.id.calc_8_button, R.id.calc_9_button,
                R.id.calc_dot_button, R.id.calc_del_button};
        for (int id : buttonsID) {
            rootView.findViewById(id).setOnClickListener((View.OnClickListener) this);
        };
        rootView.findViewById(R.id.calc_del_button).setOnLongClickListener((View.OnLongClickListener) this);
        fromSpinner.setText(DataCurrents.fromCurrency);
        toSpinner.setText(DataCurrents.toCurrency);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fromSpinner != null) {
            fromSpinner.setText(DataCurrents.fromCurrency);
            toSpinner.setText(DataCurrents.toCurrency);
        }
        if (fromConvert != null && fromConvert.getText() != "") {
            String res = Convert_Currency(enteredValue);
            toConvert.setText(res);
        }
        if (fromConvert != null && fromConvert.getText() == "") {
            enteredValue = "0";
            fromConvert.setText(enteredValue);
            toConvert.setText("0");
        }
    }

    public String Convert_Currency(String fromCur){
        double tempDouble = Double.parseDouble(fromCur);
        String fromCurName = DataCurrents.fromCurrency;
        String toCurName = DataCurrents.toCurrency;
        double firstValue = 1, secondValue = 1;
        for (int i = 0, k = 0; k != 2; i++){
            String temp = DataCurrents.currentList.get(i).getCur_Abbreviation();
            if (fromCurName.equals(temp)){
                firstValue = DataCurrents.currentList.get(i).getCur_OfficialRate();
                k++;
            }
            else if (toCurName.equals(temp)){
                secondValue = DataCurrents.currentList.get(i).getCur_OfficialRate();
                k++;
            }
        }
        double rate = secondValue / firstValue;
        double resRate = tempDouble * rate;
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", resRate);
        return result;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_currency_button:
                String temp = DataCurrents.fromCurrency;
                fromSpinner.setText(DataCurrents.toCurrency);
                toSpinner.setText(DataCurrents.fromCurrency);
                DataCurrents.fromCurrency = DataCurrents.toCurrency;
                DataCurrents.toCurrency = temp;
                String result = Convert_Currency(enteredValue);
                toConvert.setText(result);
                JSONObject object = new JSONObject();
                try {
                    object.put("from", new JSONObject().put("currency", DataCurrents.fromCurrency));
                    object.put("to", new JSONObject().put("currency", DataCurrents.toCurrency));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.calc_1_button:
            case R.id.calc_2_button:
            case R.id.calc_3_button:
            case R.id.calc_4_button:
            case R.id.calc_5_button:
            case R.id.calc_6_button:
            case R.id.calc_7_button:
            case R.id.calc_8_button:
            case R.id.calc_9_button:
                Button button = (Button) view.findViewById(view.getId());
                String str = (String) button.getText();
                if (enteredValue.equals("0")) {
                    enteredValue = str;
                    fromConvert.setText(enteredValue);
                    String res = Convert_Currency(enteredValue);
                    toConvert.setText(res);
                }
                else {
                    enteredValue += str;
                    fromConvert.setText(enteredValue);
                    String res = Convert_Currency(enteredValue);
                    toConvert.setText(res);
                }
                break;
            case R.id.calc_0_button:
                String str_0 = "0";
                if (!enteredValue.equals("0")) {
                    enteredValue += str_0;
                    fromConvert.setText(enteredValue);
                    String res = Convert_Currency(enteredValue);
                    toConvert.setText(res);
                }
                break;
            case R.id.calc_dot_button:
                String str_1 = ".";
                if (!enteredValue.contains(str_1)) {
                    enteredValue += str_1;
                    fromConvert.setText(enteredValue);
                    String res = Convert_Currency(enteredValue);
                    toConvert.setText(res);
                }
                break;
            case R.id.calc_del_button:
                if (enteredValue.length() == 1) {
                    enteredValue = "0";
                    fromConvert.setText(enteredValue);
                    String res = Convert_Currency(enteredValue);
                    toConvert.setText(res);
                }
                else {
                    enteredValue = enteredValue.substring(0, enteredValue.length() - 1);
                    fromConvert.setText(enteredValue);
                    String res = Convert_Currency(enteredValue);
                    toConvert.setText(res);
                }
                if (enteredValue.equals("0") || enteredValue.equals("0.")) {
                    toConvert.setText("0");
                }
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.calc_del_button:
                enteredValue = "0";
                fromConvert.setText(enteredValue);
                toConvert.setText("0");
                break;
        }
        return true;
    }
}