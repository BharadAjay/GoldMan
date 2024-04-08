package com.balaji.calculator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.balaji.calculator.adapter.OldGoldItemAdapter;
import com.balaji.calculator.helper.SocketTask;
import com.balaji.calculator.helper.TCPClient;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.OldGoldModel;
import com.balaji.calculator.model.PaymentDetailsModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentDetailsFragment extends Fragment implements OnRVItemClickListener<OldGoldModel> {

    ImageView ivBack;
    MaterialTextView mtvTotalPaymentDetails, mtvDiscountPaymentDetails, mtvTaxPaymentDetails, mtvRoundOffPaymentDetails,
            mtvSubtotalPaymentDetails, mtvOutstandingPaymentDetails, mtvCreditPaymentDetails, mtvGrandTotalPaymentDetails,
            mtvCashAmountPaymentDetails, mtvBankAmountPaymentDetails, mtvRoundAmountPaymentDetails, mtvBalanceAmountPaymentDetails,
            mtvOldGoldAmountPaymentDetails;
    TextInputLayout autoCompleteItemNameLayoutOldGoldBottomSheet, edtGrossWeightOldGoldBottomSheet, edtRateOldGoldBottomSheet, edtAmountOldGoldBottomSheet,
            edtNoteDialogPaymentDetails;
    MaterialAutoCompleteTextView autoCompleteItemNameOldGoldBottomSheet;
    MaterialButton mbtnSavePaymentDetails, mbtnViewDialogPaymentDetails, mbtnPrintDialogPaymentDetails, mbtnDismissDialogPaymentDetails, mbtnDismissOldGoldBottomSheet,
            mbtnSaveOldGoldBottomSheet, mbtnNoteDismissDialogPaymentDetails, mbtnCloseDialogPaymentDetails, mbtnSaveDialogPaymentDetails;
    LinearLayout llCashAmountPaymentDetails, llBankAmountPaymentDetails, llRoundAmountPaymentDetails, llOldGoldAmountPaymentDetails, llNotePaymentDetails;
    public BillFragment billFragment;
    View customDialogViewPaymentDetails, dialog_note_payment_detail_layout;
    AlertDialog alertDialogPaymentDetails, alertDialogNotePaymentDetails;
    MaterialAlertDialogBuilder materialAlertDialogBuilder, materialAlertDialogBuilderNote;
    double total = 0, discount = 0, taxes = 0, subTotal = 0, roundOff = 0, outstanding = 0.00, credit = 0.00, grandTotal = 0, balance = 0, oldGoldGrossWeight = 0, oldGoldRate = 0.0,
            oldGoldAmount = 0.0, totalOldGoldAmount = 0.0;
    int temp = 0, cashAmount = 0, bankAmount = 0, roundAmount = 0, billId = 0, cardAmount = 0, onlineAmount = 0, chequeAmount = 0;
    BottomSheetDialog bottomSheetDialog;
    ArrayAdapter arrayAdapterItemNamesOldGoldBottomSheet;
    RecyclerView rvItemsOldGoldBottomSheet;
    OldGoldItemAdapter oldGoldItemAdapter;
    List<OldGoldModel> itemOldGoldList = new ArrayList<>();
    String note = "", oldGoldItemName = "", narration = "", clientBankName = "", companyBankName = "", code = "";

    SocketTask socketTask = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    public PaymentDetailsFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentDetailsFragment newInstance(String param1, String param2) {
        PaymentDetailsFragment fragment = new PaymentDetailsFragment();
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
        try {
            arrayAdapterItemNamesOldGoldBottomSheet = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, billFragment.productSearchList.toArray());
            autoCompleteItemNameOldGoldBottomSheet.setAdapter(arrayAdapterItemNamesOldGoldBottomSheet);
            autoCompleteItemNameOldGoldBottomSheet.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            billFragment.generateLog();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_details, container, false);

        ivBack = view.findViewById(R.id.ivBack);
        mtvTotalPaymentDetails = view.findViewById(R.id.mtvTotalPaymentDetails);
        mtvDiscountPaymentDetails = view.findViewById(R.id.mtvDiscountPaymentDetails);
        mtvTaxPaymentDetails = view.findViewById(R.id.mtvTaxPaymentDetails);
        mtvRoundOffPaymentDetails = view.findViewById(R.id.mtvRoundOffPaymentDetails);
        mtvSubtotalPaymentDetails = view.findViewById(R.id.mtvSubtotalPaymentDetails);
        mtvOutstandingPaymentDetails = view.findViewById(R.id.mtvOutstandingPaymentDetails);
        mtvCreditPaymentDetails = view.findViewById(R.id.mtvCreditPaymentDetails);
        mtvOldGoldAmountPaymentDetails = view.findViewById(R.id.mtvOldGoldAmountPaymentDetails);
        mtvGrandTotalPaymentDetails = view.findViewById(R.id.mtvGrandTotalPaymentDetails);
        mtvCashAmountPaymentDetails = view.findViewById(R.id.mtvCashAmountPaymentDetails);
        mtvBankAmountPaymentDetails = view.findViewById(R.id.mtvBankAmountPaymentDetails);
        mtvRoundAmountPaymentDetails = view.findViewById(R.id.mtvRoundAmountPaymentDetails);
        mtvBalanceAmountPaymentDetails = view.findViewById(R.id.mtvBalanceAmountPaymentDetails);
        llCashAmountPaymentDetails = view.findViewById(R.id.llCashAmountPaymentDetails);
        llBankAmountPaymentDetails = view.findViewById(R.id.llBankAmountPaymentDetails);
        llRoundAmountPaymentDetails = view.findViewById(R.id.llRoundAmountPaymentDetails);
        llOldGoldAmountPaymentDetails = view.findViewById(R.id.llOldGoldAmountPaymentDetails);
        llNotePaymentDetails = view.findViewById(R.id.llNotePaymentDetails);
        mbtnSavePaymentDetails = view.findViewById(R.id.mbtnSavePaymentDetails);

        customDialogViewPaymentDetails = inflater.inflate(R.layout.dialog_payment_details_layout, container, false);
        mbtnViewDialogPaymentDetails = customDialogViewPaymentDetails.findViewById(R.id.mbtnViewDialogPaymentDetails);
        mbtnPrintDialogPaymentDetails = customDialogViewPaymentDetails.findViewById(R.id.mbtnPrintDialogPaymentDetails);
        mbtnDismissDialogPaymentDetails = customDialogViewPaymentDetails.findViewById(R.id.mbtnDismissDialogPaymentDetails);

        dialog_note_payment_detail_layout = inflater.inflate(R.layout.dialog_note_payment_detail_layout, container, false);
        edtNoteDialogPaymentDetails = dialog_note_payment_detail_layout.findViewById(R.id.edtNoteDialogPaymentDetails);
        mbtnSaveDialogPaymentDetails = dialog_note_payment_detail_layout.findViewById(R.id.mbtnSaveDialogPaymentDetails);
        mbtnCloseDialogPaymentDetails = dialog_note_payment_detail_layout.findViewById(R.id.mbtnCloseDialogPaymentDetails);

        //BottomSheet Code
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_old_gold_layout);
        mbtnDismissOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.mbtnDismissOldGoldBottomSheet);
        mbtnSaveOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.mbtnSaveOldGoldBottomSheet);
        autoCompleteItemNameOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.autoCompleteItemNameOldGoldBottomSheet);
        edtGrossWeightOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.edtGrossWeightOldGoldBottomSheet);
        edtRateOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.edtRateOldGoldBottomSheet);
        edtAmountOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.edtAmountOldGoldBottomSheet);
        autoCompleteItemNameLayoutOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.autoCompleteItemNameLayoutOldGoldBottomSheet);
        rvItemsOldGoldBottomSheet = bottomSheetDialog.findViewById(R.id.rvItemsOldGoldBottomSheet);

        initView();

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        llCashAmountPaymentDetails.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (cashAmount > 0) {
                bundle.putBoolean("isEdit", true);
            }
            bundle.putInt("cashAmount", cashAmount);
            bundle.putInt("roundAmount", roundAmount);
            bundle.putString("title", "Cash Amount");
            bundle.putDouble("balance", balance);
            bundle.putDouble("grandTotal", grandTotal);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            CashReceiveFragment fragmentCashReceive = new CashReceiveFragment();
            fragmentCashReceive.setArguments(bundle);

            transaction.replace(R.id.frame_full, fragmentCashReceive, "CashReceiveFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        llBankAmountPaymentDetails.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (bankAmount > 0) {
                bundle.putBoolean("isEdit", true);
            }

            bundle.putInt("cardAmount", cardAmount);
            bundle.putInt("onlineAmount", onlineAmount);
            bundle.putInt("chequeAmount", chequeAmount);
            bundle.putInt("bankAmount", bankAmount);
            bundle.putDouble("balance", balance);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            BankReceiveFragment fragmentBankReceive = new BankReceiveFragment(billFragment);
            fragmentBankReceive.setArguments(bundle);

            transaction.replace(R.id.frame_full, fragmentBankReceive, "BankReceiveFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        llRoundAmountPaymentDetails.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (roundAmount > 0) {
                bundle.putBoolean("isEdit", true);
            }
            bundle.putInt("cashAmount", cashAmount);
            bundle.putInt("roundAmount", roundAmount);
            bundle.putString("title", "Round Amount");
            bundle.putDouble("balance", balance);
            bundle.putDouble("grandTotal", grandTotal);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            CashReceiveFragment fragmentCashReceive = new CashReceiveFragment();
            fragmentCashReceive.setArguments(bundle);

            transaction.replace(R.id.frame_full, fragmentCashReceive, "CashReceiveFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        llNotePaymentDetails.setOnClickListener(v -> {
            alertDialogNotePaymentDetails.show();
        });

        llOldGoldAmountPaymentDetails.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });

        mbtnSaveDialogPaymentDetails.setOnClickListener(v -> {
            try {
                note = edtNoteDialogPaymentDetails.getEditText().getText().toString();
                alertDialogNotePaymentDetails.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mbtnCloseDialogPaymentDetails.setOnClickListener(v -> alertDialogNotePaymentDetails.dismiss());

        bottomSheetDialog.setOnDismissListener(dialog -> {
            setData();
            clearBottomSheetData();
        });

        //BottomSheet Code
        autoCompleteItemNameOldGoldBottomSheet.setOnItemClickListener((parent, view1, position, id) -> {
            autoCompleteItemNameOldGoldBottomSheet.setError(null);
            oldGoldItemName = autoCompleteItemNameOldGoldBottomSheet.getText().toString();
        });

        mbtnDismissOldGoldBottomSheet.setOnClickListener(v -> bottomSheetDialog.dismiss());

        mbtnSaveOldGoldBottomSheet.setOnClickListener(v -> {
            if (autoCompleteItemNameOldGoldBottomSheet.getText().toString().isEmpty())
                autoCompleteItemNameLayoutOldGoldBottomSheet.setError("Please select item");
            else if (edtGrossWeightOldGoldBottomSheet.getEditText().getText().toString().isEmpty())
                edtGrossWeightOldGoldBottomSheet.setError("Please enter gross weight");
            else if (edtAmountOldGoldBottomSheet.getEditText().getText().toString().isEmpty() || edtAmountOldGoldBottomSheet.getEditText().getText().toString().equals("0.00"))
                edtAmountOldGoldBottomSheet.setError("Please enter amount");
            else {
                autoCompleteItemNameLayoutOldGoldBottomSheet.setError(null);
                edtGrossWeightOldGoldBottomSheet.setError(null);
                edtAmountOldGoldBottomSheet.setError(null);

                if (edtAmountOldGoldBottomSheet.getEditText().getText().toString().isEmpty())
                    oldGoldAmount = oldGoldGrossWeight * (oldGoldRate / 10);

                if ((totalOldGoldAmount + oldGoldAmount) <= subTotal) {
                    itemOldGoldList.add(new OldGoldModel(autoCompleteItemNameOldGoldBottomSheet.getText().toString(), oldGoldGrossWeight, oldGoldRate, oldGoldAmount));
                    oldGoldItemAdapter.notifyDataSetChanged();
                    totalOldGoldAmount += oldGoldAmount;
                    calculateAllAmount();
                    clearBottomSheetData();
                } else
                    edtAmountOldGoldBottomSheet.setError("Please enter lesser value than subtotal");

//                    Toast.makeText(getContext(), "Please enter lesser value than Grand Total", Toast.LENGTH_SHORT).show();
//                    Snackbar snackbar = Snackbar.make(getActivity(), "Please enter lesser value than Grand Total", Snackbar.LENGTH_SHORT).show();
            }
        });

        mbtnSavePaymentDetails.setOnClickListener(v -> {
            sendDataToServer();
        });

        mbtnViewDialogPaymentDetails.setOnClickListener(v -> {
            openPDF();
        });

        mbtnPrintDialogPaymentDetails.setOnClickListener(v -> {
            boolean flag = true;
            while (flag) {
                if (billFragment.voucherId != 0) {
                    sendBillPrintRequest();
                    flag = false;
                } else {
                }
            }
        });

        mbtnDismissDialogPaymentDetails.setOnClickListener(v -> {
            alertDialogPaymentDetails.dismiss();
        });

        materialAlertDialogBuilder.setOnDismissListener(dialog -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            billFragment.onDeleteAll();
            if (billFragment != null) {
                transaction.replace(R.id.frame_full, billFragment, "HomeFragment");
                transaction.commit();
            }

            File appSpecificInternalStorageDirectory = getActivity().getFilesDir();

            for (File subfile : appSpecificInternalStorageDirectory.listFiles()) {
                subfile.delete();
            }
        });

        setData();

        return view;
    }

    void initView() {
        getActivity().getSupportFragmentManager().setFragmentResultListener("CashReceiveResult", getViewLifecycleOwner(), (requestKey, result) -> {
            cashAmount = result.getInt("cashAmount");
            mtvCashAmountPaymentDetails.setText("₹" + cashAmount);

            roundAmount = result.getInt("roundAmount");
            mtvRoundAmountPaymentDetails.setText("₹" + roundAmount);

            mtvBankAmountPaymentDetails.setText("₹" + bankAmount);

            balance = grandTotal - cashAmount - bankAmount - roundAmount;
            mtvBalanceAmountPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", balance));
        });

        getActivity().getSupportFragmentManager().setFragmentResultListener("BankReceiveResult", getViewLifecycleOwner(), (requestKey, result) -> {

            cardAmount = result.getInt("cardAmount");
            onlineAmount = result.getInt("onlineAmount");
            chequeAmount = result.getInt("chequeAmount");
            bankAmount = result.getInt("bankAmount");
            narration = result.getString("narration");
            clientBankName = result.getString("clientBankName");
            companyBankName = result.getString("companyBankName");
            code = result.getString("code");

            mtvBankAmountPaymentDetails.setText("₹" + bankAmount);

            mtvCashAmountPaymentDetails.setText("₹" + cashAmount);

            mtvRoundAmountPaymentDetails.setText("₹" + roundAmount);

            balance = grandTotal - cashAmount - bankAmount - roundAmount;
            mtvBalanceAmountPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", balance));
        });

        //Bottomsheet Code

        edtRateOldGoldBottomSheet.getEditText().setSelectAllOnFocus(true);
        edtGrossWeightOldGoldBottomSheet.getEditText().setSelectAllOnFocus(true);
        edtAmountOldGoldBottomSheet.getEditText().setSelectAllOnFocus(true);

        edtGrossWeightOldGoldBottomSheet.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!edtGrossWeightOldGoldBottomSheet.getEditText().getText().toString().isEmpty())
                edtGrossWeightOldGoldBottomSheet.getEditText().setText(String.format(Locale.getDefault(), "%.3f", Double.parseDouble(edtGrossWeightOldGoldBottomSheet.getEditText().getText().toString())));
        });

        edtGrossWeightOldGoldBottomSheet.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateOldGoldAmount();
            }
        });

        edtRateOldGoldBottomSheet.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateOldGoldAmount();
            }
        });

        edtAmountOldGoldBottomSheet.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                    oldGoldAmount = Double.parseDouble(s.toString());
                else
                    oldGoldAmount = 0.0;
            }
        });

        oldGoldItemAdapter = new OldGoldItemAdapter(getContext(), itemOldGoldList, this);

        rvItemsOldGoldBottomSheet.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvItemsOldGoldBottomSheet.setAdapter(oldGoldItemAdapter);

        Bundle b = getArguments().getBundle("CartFragmentBundle");
        total = b.getDouble("total");
        discount = b.getDouble("discount");
        taxes = b.getDouble("taxes");
        billId = b.getInt("billId");
        //billFragment.AccountNumber = getArguments().getString("AccountNumber");
        if (getArguments().getDouble("OpeningAmount") < 0)
            outstanding = Math.abs(getArguments().getDouble("OpeningAmount"));
        else
            credit = Math.abs(getArguments().getDouble("OpeningAmount"));

        calculateAllAmount();

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
        materialAlertDialogBuilder.setView(customDialogViewPaymentDetails).setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));

        materialAlertDialogBuilderNote = new MaterialAlertDialogBuilder(getContext());
        materialAlertDialogBuilderNote.setView(dialog_note_payment_detail_layout).setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
        alertDialogNotePaymentDetails = materialAlertDialogBuilderNote.create();

        sendNarrationRequest();
        sendBankNameRequest();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }

    private int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    private void sendDataToServer() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                PaymentDetailsModel modelPaymentDetails = new PaymentDetailsModel(Double.parseDouble(mtvTotalPaymentDetails.getText().toString()),
                                        Double.parseDouble(mtvDiscountPaymentDetails.getText().toString()), Double.parseDouble(mtvTaxPaymentDetails.getText().toString()),
                                        Double.parseDouble(mtvRoundOffPaymentDetails.getText().toString()), Double.parseDouble(mtvSubtotalPaymentDetails.getText().toString()),
                                        Double.parseDouble(mtvOutstandingPaymentDetails.getText().toString()), Double.parseDouble(mtvCreditPaymentDetails.getText().toString()),
                                        Double.parseDouble(mtvGrandTotalPaymentDetails.getText().toString()), Double.parseDouble(mtvCashAmountPaymentDetails.getText().toString().substring(1)),
                                        cardAmount, onlineAmount, chequeAmount, Double.parseDouble(mtvRoundAmountPaymentDetails.getText().toString().substring(1)),
                                        Double.parseDouble(mtvBalanceAmountPaymentDetails.getText().toString()), billId, note, billFragment.AccountNumber, narration, clientBankName,
                                        companyBankName, code
                                );

                                Gson gson = new Gson();
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("PAYMENTDETAILS:" + gson.toJson(modelPaymentDetails) + "!" +
                                        gson.toJson(billFragment.fragmentCart.selectedItems) + "!" + gson.toJson(itemOldGoldList));
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    public void sendBillViewRequest() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetPDF("VIEWBILL:" + billFragment.voucherId + ":" + billId);
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    private void sendBillPrintRequest() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("PRINTBILL:" + billFragment.voucherId + ":" + billId);
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    private void sendNarrationRequest() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("NARRATIONLIST");
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    private void sendBankNameRequest() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("BANKLIST");
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    void openPDF() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh:mm:aa_");
        Date now=new Date();
        String fileName = formatter.format(now) +"Invoice.pdf";
        File pdfOpenFile = getActivity().getFilesDir();

        File fileOpen = new File(pdfOpenFile,fileName);
        //File fileOpen = new File(pdfOpenFile,socketTask.getTcpClient().fileName);
       // File pdfOpenFile = getActivity().getFilesDir();

        //File fileOpen = new File(pdfOpenFile, "Invoice.pdf");

        Uri path = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", fileOpen);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
//            getActivity().startActivity(Intent.createChooser(pdfOpenintent, "Open File Using..."));
            getActivity().startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    void calculateOldGoldAmount() {
        oldGoldItemName = autoCompleteItemNameOldGoldBottomSheet.getText().toString();

        try {
            oldGoldGrossWeight = 0;
            oldGoldGrossWeight = Double.parseDouble(edtGrossWeightOldGoldBottomSheet.getEditText().getText().toString());
        } catch (Exception e) {

        }
        try {
            oldGoldRate = 0;
            oldGoldRate = Double.parseDouble(edtRateOldGoldBottomSheet.getEditText().getText().toString());
        } catch (Exception e) {

        }

        if (oldGoldRate == 0.0) {
            try {
                oldGoldAmount = 0;
                oldGoldAmount = Double.parseDouble(edtAmountOldGoldBottomSheet.getEditText().getText().toString());
            } catch (Exception e) {

            }
        } else {
            oldGoldAmount = oldGoldGrossWeight * (oldGoldRate / 10);
        }
        edtAmountOldGoldBottomSheet.getEditText().setText(String.format(Locale.getDefault(), "%.2f", oldGoldAmount));
    }

    void calculateAllAmount() {
        double tmpAmount = (total - discount) + taxes;
        roundOff = round(tmpAmount) - tmpAmount;
        subTotal = (total - discount) + taxes + roundOff;

        grandTotal = subTotal + outstanding - credit - totalOldGoldAmount;
        balance = grandTotal - cashAmount - bankAmount + roundAmount;
    }

    void setData() {
        mtvTotalPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", total));
        mtvDiscountPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", discount));
        mtvTaxPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", taxes));
        mtvSubtotalPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", subTotal));
        mtvRoundOffPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", roundOff));
        mtvGrandTotalPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", grandTotal));
        mtvBalanceAmountPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", balance));
        mtvOutstandingPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", outstanding));
        mtvCreditPaymentDetails.setText(String.format(Locale.getDefault(), "%.2f", credit));
        mtvOldGoldAmountPaymentDetails.setText(String.valueOf(totalOldGoldAmount));
    }

    void clearBottomSheetData() {
        autoCompleteItemNameOldGoldBottomSheet.setText("");
        edtGrossWeightOldGoldBottomSheet.getEditText().setText("");
        edtRateOldGoldBottomSheet.getEditText().setText("");
        edtAmountOldGoldBottomSheet.getEditText().setText("");
    }

    @Override
    public void onItemCLick(int position, OldGoldModel item) {
        totalOldGoldAmount = totalOldGoldAmount - item.getAmount();
        itemOldGoldList.remove(position);
        oldGoldItemAdapter.notifyDataSetChanged();
    }
}