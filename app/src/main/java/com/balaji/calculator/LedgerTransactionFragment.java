package com.balaji.calculator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.balaji.calculator.adapter.LedgerTransactionAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LedgerTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedgerTransactionFragment extends Fragment {
    ImageView ivBack;
    RecyclerView rvLedgerTransaction;
    LedgerTransactionAdapter adapterLedgerTransaction;
    BillFragment billFragment;
    String AccountNo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LedgerTransactionFragment() {
        // Required empty public constructor
    }

    public LedgerTransactionFragment(BillFragment billFragment, String AccountNo) {
        this.billFragment = billFragment;
        this.AccountNo = AccountNo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LedgerTransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LedgerTransactionFragment newInstance(String param1, String param2) {
        LedgerTransactionFragment fragment = new LedgerTransactionFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ledger_transaction, container, false);

        ivBack = view.findViewById(R.id.ivBack);
        rvLedgerTransaction = view.findViewById(R.id.rvLedgerTransaction);

        getDataFromServer();

        ivBack.setOnClickListener(v -> getActivity().onBackPressed());

//        homeFragment.listItemLedgerTransaction.add(new ItemLedgerTransaction("Goldbook", 001, 100.0, 80.0, 10.0, 5.0, 10.0, 5.0));

        adapterLedgerTransaction = new LedgerTransactionAdapter(getContext(), billFragment.listItemLedgerTransaction);
        rvLedgerTransaction.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvLedgerTransaction.setAdapter(adapterLedgerTransaction);

        return view;
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

    private void getDataFromServer() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTDETAIL:" + AccountNo);
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
}