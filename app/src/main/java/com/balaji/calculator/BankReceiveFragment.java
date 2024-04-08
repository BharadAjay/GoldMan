package com.balaji.calculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankReceiveFragment extends Fragment {
    ImageView ivBack;
    EditText edtAmountBankReceive;
    Chip chip5000BankReceive, chip2500BankReceive, chip1000BankReceive, chip100BankReceive, chip100PercentBankReceive;
    MaterialButton mbtnAddBankReceive, mbtnClearCardAmountBankReceive, mbtnClearOnlineAmountBankReceive, mbtnClearChequeAmountBankReceive, mbtnSaveBankReceive;
    MaterialTextView mtvRemainingAmountBankReceive, mtvCardAmountBankReceive, mtvOnlineAmountBankReceive, mtvChequeAmountBankReceive;
    MaterialAutoCompleteTextView autoCompleteNarrationBankNameBankReceive, autoCompleteCompanyBankNameBankReceive, autoCompletePaymentMethodBankReceive;
    TextInputLayout edtCodeBankReceive, edtClientBankNameBankReceive, autoCompletePaymentMethodLayoutBankReceive;
    ArrayAdapter arrayAdapterNarration, arrayAdapterClientBankName, arrayAdapterCompanyBankName, arrayAdapterPaymentMethod;
    int addAmount = 0, bankAmount = 0, cardAmount = 0, onlineAmount = 0, chequeAmount = 0;
    double remainingAmount = 0, tempRemainingAmount = 0.0;
    boolean isEdit = false;
    String paymentMethod = "", narration = "", clientBankName = "", companyBankName = "", code = "";
    BillFragment billFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BankReceiveFragment() {
        // Required empty public constructor
    }

    public BankReceiveFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankRecieveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankReceiveFragment newInstance(String param1, String param2) {
        BankReceiveFragment fragment = new BankReceiveFragment();
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

            cardAmount = getArguments().getInt("cardAmount");
            onlineAmount = getArguments().getInt("onlineAmount");
            chequeAmount = getArguments().getInt("chequeAmount");
            bankAmount = getArguments().getInt("bankAmount");
            remainingAmount = getArguments().getDouble("balance");
            isEdit = getArguments().getBoolean("isEdit");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
//            String[] banks = {"Axis", "BOB"};
//            String[] narration = {"ATM", "Cheque"};
            String[] arrPaymentMethod = {"Card", "Online", "Cheque"};

//            arrayAdapterClientBankName = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, banks);
//            autoCompleteClientBankNameBankReceive.setAdapter(arrayAdapterClientBankName);
//            autoCompleteClientBankNameBankReceive.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);

            arrayAdapterPaymentMethod = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, arrPaymentMethod);
            autoCompletePaymentMethodBankReceive.setAdapter(arrayAdapterPaymentMethod);
            autoCompletePaymentMethodBankReceive.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);

            arrayAdapterCompanyBankName = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, billFragment.arrCompanyBank);
            autoCompleteCompanyBankNameBankReceive.setAdapter(arrayAdapterCompanyBankName);
            autoCompleteCompanyBankNameBankReceive.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);

            arrayAdapterNarration = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, billFragment.arrNarration);
            autoCompleteNarrationBankNameBankReceive.setAdapter(arrayAdapterNarration);
            autoCompleteNarrationBankNameBankReceive.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_receive, container, false);

        ivBack = view.findViewById(R.id.ivBack);
        edtAmountBankReceive = view.findViewById(R.id.edtAmountBankReceive);
        chip100PercentBankReceive = view.findViewById(R.id.chip100PercentBankReceive);
        chip5000BankReceive = view.findViewById(R.id.chip5000BankReceive);
        chip2500BankReceive = view.findViewById(R.id.chip2500BankReceive);
        chip1000BankReceive = view.findViewById(R.id.chip1000BankReceive);
        chip100BankReceive = view.findViewById(R.id.chip100BankReceive);
        mbtnAddBankReceive = view.findViewById(R.id.mbtnAddBankReceive);
        mbtnSaveBankReceive = view.findViewById(R.id.mbtnSaveBankReceive);
        autoCompletePaymentMethodLayoutBankReceive = view.findViewById(R.id.autoCompletePaymentMethodLayoutBankReceive);
        autoCompletePaymentMethodBankReceive = view.findViewById(R.id.autoCompletePaymentMethodBankReceive);
        autoCompleteNarrationBankNameBankReceive = view.findViewById(R.id.autoCompleteNarrationBankNameBankReceive);
        edtClientBankNameBankReceive = view.findViewById(R.id.edtClientBankNameBankReceive);
        autoCompleteCompanyBankNameBankReceive = view.findViewById(R.id.autoCompleteCompanyBankNameBankReceive);
        mtvRemainingAmountBankReceive = view.findViewById(R.id.mtvRemainingAmountBankReceive);
        mtvCardAmountBankReceive = view.findViewById(R.id.mtvCardAmountBankReceive);
        mtvOnlineAmountBankReceive = view.findViewById(R.id.mtvOnlineAmountBankReceive);
        mtvChequeAmountBankReceive = view.findViewById(R.id.mtvChequeAmountBankReceive);
        mbtnClearCardAmountBankReceive = view.findViewById(R.id.mbtnClearCardAmountBankReceive);
        mbtnClearOnlineAmountBankReceive = view.findViewById(R.id.mbtnClearOnlineAmountBankReceive);
        mbtnClearChequeAmountBankReceive = view.findViewById(R.id.mbtnClearChequeAmountBankReceive);
        edtCodeBankReceive = view.findViewById(R.id.edtCodeBankReceive);

        initView();

        mbtnClearCardAmountBankReceive.setOnClickListener(v -> {
            cardAmount = 0;
            mtvCardAmountBankReceive.setText("₹0");
            calculateRemainingAmount();
        });

        mbtnClearOnlineAmountBankReceive.setOnClickListener(v -> {
            onlineAmount = 0;
            mtvOnlineAmountBankReceive.setText("₹0");
            calculateRemainingAmount();
        });

        mbtnClearChequeAmountBankReceive.setOnClickListener(v -> {
            chequeAmount = 0;
            mtvChequeAmountBankReceive.setText("₹0");
            calculateRemainingAmount();
        });

        chip100PercentBankReceive.setOnClickListener(v -> {
            edtAmountBankReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountBankReceive.getText().toString().substring(1)) > 0) {
//                addAmount = edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountBankReceive.getText().toString());
                edtAmountBankReceive.setText(String.valueOf((int) remainingAmount));
            }
        });

        chip5000BankReceive.setOnClickListener(v -> {
            edtAmountBankReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountBankReceive.getText().toString().substring(1)) > 5000) {
                addAmount = edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountBankReceive.getText().toString());
                edtAmountBankReceive.setText(String.valueOf(addAmount + 5000));
            }
        });

        chip2500BankReceive.setOnClickListener(v -> {
            edtAmountBankReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountBankReceive.getText().toString().substring(1)) > 2500) {
                addAmount = edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountBankReceive.getText().toString());
                edtAmountBankReceive.setText(String.valueOf(addAmount + 2500));
            }
        });

        chip1000BankReceive.setOnClickListener(v -> {
            edtAmountBankReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountBankReceive.getText().toString().substring(1)) > 1000) {
                addAmount = edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountBankReceive.getText().toString());
                edtAmountBankReceive.setText(String.valueOf(addAmount + 1000));
            }
        });

        chip100BankReceive.setOnClickListener(v -> {
            edtAmountBankReceive.clearFocus();
            if (Double.parseDouble(mtvRemainingAmountBankReceive.getText().toString().substring(1)) > 100) {
                addAmount = edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtAmountBankReceive.getText().toString());
                edtAmountBankReceive.setText(String.valueOf(addAmount + 100));
            }
        });

        autoCompletePaymentMethodBankReceive.setOnItemClickListener((parent, view1, position, id) -> {
            paymentMethod = autoCompletePaymentMethodBankReceive.getText().toString();
        });

        autoCompleteNarrationBankNameBankReceive.setOnItemClickListener((parent, view1, position, id) -> {
            narration = autoCompleteNarrationBankNameBankReceive.getText().toString();
        });

        autoCompleteCompanyBankNameBankReceive.setOnItemClickListener((parent, view1, position, id) -> {
            companyBankName = autoCompleteCompanyBankNameBankReceive.getText().toString();
        });

        mbtnAddBankReceive.setOnClickListener(v -> {
            if (paymentMethod.equals("")) {
                autoCompletePaymentMethodLayoutBankReceive.setError("Please select payment method");
                autoCompletePaymentMethodBankReceive.requestFocus();
            } else {
                autoCompletePaymentMethodLayoutBankReceive.setError(null);

                if (((int) (Double.parseDouble(edtAmountBankReceive.getText().toString()))) <= remainingAmount) {
                    edtAmountBankReceive.setError(null);

                    if (paymentMethod.equals("Card")) {
                        cardAmount = Integer.parseInt(edtAmountBankReceive.getText().toString());
                        mtvCardAmountBankReceive.setText('\u20B9' + edtAmountBankReceive.getText().toString());
                    } else if (paymentMethod.equals("Online")) {
                        onlineAmount = Integer.parseInt(edtAmountBankReceive.getText().toString());
                        mtvOnlineAmountBankReceive.setText('\u20B9' + edtAmountBankReceive.getText().toString());
                    } else {
                        chequeAmount = Integer.parseInt(edtAmountBankReceive.getText().toString());
                        mtvChequeAmountBankReceive.setText('\u20B9' + edtAmountBankReceive.getText().toString());
                    }
                } else
                    edtAmountBankReceive.setError("Please enter lesser value than remaining amount");

                code = edtCodeBankReceive.getEditText().getText().toString().isEmpty() ? "" : edtCodeBankReceive.getEditText().getText().toString();
                clientBankName = edtClientBankNameBankReceive.getEditText().getText().toString().isEmpty() ? "" : edtClientBankNameBankReceive.getEditText().getText().toString();
                clearData();

                calculateRemainingAmount();
            }
        });

        mbtnSaveBankReceive.setOnClickListener(v -> {
            bankAmount = (int) (cardAmount + onlineAmount + chequeAmount);
        });

        ivBack.setOnClickListener(v -> {
//            bankAmount = edtAmountBankReceive.getText().toString().isEmpty() ? 0 : (int) (Double.parseDouble(edtAmountBankReceive.getText().toString()));
            getActivity().onBackPressed();
        });

        return view;
    }

    void initView() {
        mtvCardAmountBankReceive.setText('\u20B9' + String.valueOf(cardAmount));
        mtvOnlineAmountBankReceive.setText('\u20B9' + String.valueOf(onlineAmount));
        mtvChequeAmountBankReceive.setText('\u20B9' + String.valueOf(chequeAmount));
//        edtAmountBankReceive.setText(String.valueOf(bankAmount));

        edtAmountBankReceive.setSelectAllOnFocus(true);
        mtvRemainingAmountBankReceive.setText("\u20B9" + String.format(Locale.getDefault(), "%.2f", remainingAmount));

        tempRemainingAmount = remainingAmount - (edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtAmountBankReceive.getText().toString()));
        remainingAmount += edtAmountBankReceive.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtAmountBankReceive.getText().toString());

        edtAmountBankReceive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    mtvRemainingAmountBankReceive.setText("\u20B9" + String.format(Locale.getDefault(), "%.2f", (remainingAmount - Double.parseDouble(edtAmountBankReceive.getText().toString()) - cardAmount - onlineAmount - chequeAmount)));
                    if (Double.parseDouble(mtvRemainingAmountBankReceive.getText().toString().substring(1)) < 0)
                        mtvRemainingAmountBankReceive.setTextColor(getResources().getColor(R.color.design_default_color_error, null));
                    else
                        mtvRemainingAmountBankReceive.setTextColor(getResources().getColor(R.color.black, null));
                }
            }
        });
    }

    void calculateRemainingAmount() {
        mtvRemainingAmountBankReceive.setText("\u20B9" + String.format(Locale.getDefault(), "%.2f", (remainingAmount - cardAmount - onlineAmount - chequeAmount)));
    }

    void clearData() {
        paymentMethod = "";
        edtAmountBankReceive.setText("0");
        autoCompletePaymentMethodBankReceive.setText("");
        autoCompleteCompanyBankNameBankReceive.setText("");
        edtClientBankNameBankReceive.getEditText().setText("");
        autoCompleteNarrationBankNameBankReceive.setText("");
        edtCodeBankReceive.getEditText().setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Bundle b = new Bundle();
        b.putInt("cardAmount", cardAmount);
        b.putInt("onlineAmount", onlineAmount);
        b.putInt("chequeAmount", chequeAmount);
        b.putInt("bankAmount", bankAmount);
        b.putString("narration", narration);
        b.putString("clientBankName", clientBankName);
        b.putString("companyBankName", companyBankName);
        b.putString("code", code);

        getActivity().getSupportFragmentManager().setFragmentResult("BankReceiveResult", b);
    }
}