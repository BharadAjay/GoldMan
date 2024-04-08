package com.balaji.calculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CashReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashReceiveFragment extends Fragment {

    ImageView ivBack;
    EditText edtAmountCashReceive;
    Chip chip5000CashReceive, chip2500CashReceive, chip1000CashReceive, chip100CashReceive, chip100PercentCashReceive;
    MaterialButton btnAddCashReceive;
    MaterialTextView mtvTitleCashReceive, mtvRemainingAmountCashReceive;
    int addAmount = 0, cashAmount = 0, roundAmount = 0, bankAmount = 0;
    double remainingAmount = 0, grandTotal = 0, tempRemainingAmount = 0.0;
    String title = "";
    boolean isEdit = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CashReceiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CashReceiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CashReceiveFragment newInstance(String param1, String param2) {
        CashReceiveFragment fragment = new CashReceiveFragment();
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

            title = getArguments().getString("title");
            cashAmount = getArguments().getInt("cashAmount");
            roundAmount = getArguments().getInt("roundAmount");
            remainingAmount = getArguments().getDouble("balance");
            grandTotal = getArguments().getDouble("grandTotal");
            isEdit = getArguments().getBoolean("isEdit");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (title.equals("Cash Amount")) {
        Bundle b = new Bundle();
        b.putInt("cashAmount", cashAmount);
//            getActivity().getSupportFragmentManager().setFragmentResult("CashAmountResult", b);
//        } else if (title.equals("Round Amount")) {
//            Bundle b = new Bundle();
        b.putInt("roundAmount", roundAmount);
        getActivity().getSupportFragmentManager().setFragmentResult("CashReceiveResult", b);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_receive, container, false);

        ivBack = view.findViewById(R.id.ivBack);
        edtAmountCashReceive = view.findViewById(R.id.edtAmountCashReceive);
        chip100PercentCashReceive = view.findViewById(R.id.chip100PercentCashReceive);
        chip5000CashReceive = view.findViewById(R.id.chip5000CashReceive);
        chip2500CashReceive = view.findViewById(R.id.chip2500CashReceive);
        chip1000CashReceive = view.findViewById(R.id.chip1000CashReceive);
        chip100CashReceive = view.findViewById(R.id.chip100CashReceive);
        btnAddCashReceive = view.findViewById(R.id.btnAddCashReceive);
        mtvTitleCashReceive = view.findViewById(R.id.mtvTitleCashReceive);
        mtvRemainingAmountCashReceive = view.findViewById(R.id.mtvRemainingAmountCashReceive);

        initView();

        chip100PercentCashReceive.setOnClickListener(v -> {
            edtAmountCashReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) > 0) {
                addAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountCashReceive.getText().toString());
                edtAmountCashReceive.setText(String.valueOf((int) Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) + addAmount));
            }
        });

        chip5000CashReceive.setOnClickListener(v -> {
            edtAmountCashReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) > 5000) {
                addAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountCashReceive.getText().toString());
                edtAmountCashReceive.setText(String.valueOf(addAmount + 5000));
            }
        });

        chip2500CashReceive.setOnClickListener(v -> {
            edtAmountCashReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) > 2500) {
                addAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountCashReceive.getText().toString());
                edtAmountCashReceive.setText(String.valueOf(addAmount + 2500));
            }
        });

        chip1000CashReceive.setOnClickListener(v -> {
            edtAmountCashReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) > 1000) {
                addAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountCashReceive.getText().toString());
                edtAmountCashReceive.setText(String.valueOf(addAmount + 1000));
            }
        });

        chip100CashReceive.setOnClickListener(v -> {
            edtAmountCashReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) > 100) {
                addAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountCashReceive.getText().toString());
                edtAmountCashReceive.setText(String.valueOf(addAmount + 100));
            }
        });

        btnAddCashReceive.setOnClickListener(v -> {
            if (((int) (Double.parseDouble(edtAmountCashReceive.getText().toString()))) <= remainingAmount) {
                edtAmountCashReceive.setError(null);
                if (title.equals("Cash Amount"))
                    cashAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : (int) (Double.parseDouble(edtAmountCashReceive.getText().toString()));
                else if (title.equals("Round Amount"))
                    roundAmount = edtAmountCashReceive.getText().toString().isEmpty() ? 0 : (int) Double.parseDouble(edtAmountCashReceive.getText().toString());

                getActivity().onBackPressed();
            } else
                edtAmountCashReceive.setError("Please enter lesser value than remaining amount");
        });

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        return view;
    }

    void initView() {
        mtvTitleCashReceive.setText(title);

        if (title.equals("Cash Amount")) {
            edtAmountCashReceive.setText(String.valueOf(cashAmount));
        } else if (title.equals("Round Amount")) {
            edtAmountCashReceive.setText(String.valueOf(roundAmount));
        }

        edtAmountCashReceive.setSelectAllOnFocus(true);
        mtvRemainingAmountCashReceive.setText('\u20B9' + String.format(Locale.getDefault(), "%.2f", remainingAmount));

//        tempRemainingAmount = remainingAmount - (edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtAmountCashReceive.getText().toString()));
        remainingAmount += edtAmountCashReceive.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtAmountCashReceive.getText().toString());

        edtAmountCashReceive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    mtvRemainingAmountCashReceive.setText("\u20B9" + String.format(Locale.getDefault(), "%.2f", (remainingAmount - Double.parseDouble(edtAmountCashReceive.getText().toString()))));

                    if (Double.parseDouble(mtvRemainingAmountCashReceive.getText().toString().substring(1)) < 0)
                        mtvRemainingAmountCashReceive.setTextColor(getResources().getColor(R.color.design_default_color_error, null));
                    else
                        mtvRemainingAmountCashReceive.setTextColor(getResources().getColor(R.color.black, null));
                }
            }
        });
    }
}