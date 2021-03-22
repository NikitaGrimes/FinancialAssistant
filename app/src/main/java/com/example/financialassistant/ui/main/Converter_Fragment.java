package com.example.financialassistant.ui.main;

import android.content.Intent;
import android.os.Bundle;

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
public class Converter_Fragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView fromConvert;
    TextView toConvert;
    Button fromSpinner;
    Button toSpinner;

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
        fromSpinner.setText(DataCurrents.fromCurrency);
        toSpinner.setText(DataCurrents.toCurrency);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_currency_button:
                String temp = DataCurrents.fromCurrency;
                fromSpinner.setText(DataCurrents.toCurrency);
                toSpinner.setText(DataCurrents.fromCurrency);
                DataCurrents.fromCurrency = DataCurrents.toCurrency;
                DataCurrents.toCurrency = temp;
                JSONObject object = new JSONObject();
                try {
                    object.put("from", new JSONObject().put("currency", DataCurrents.fromCurrency));
                    object.put("to", new JSONObject().put("currency", DataCurrents.toCurrency));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}