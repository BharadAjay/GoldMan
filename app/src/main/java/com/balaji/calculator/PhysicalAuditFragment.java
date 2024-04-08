package com.balaji.calculator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.balaji.calculator.adapter.PhysicalAuditAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PhysicalAuditFragment extends Fragment {

    ImageView ivBack;
    MaterialButton btnPhysicalAuditDone;
    List<PhysicalAuditItem> products = new ArrayList<>();
    public PhysicalAuditAdapter productAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String items;
    BillFragment billFragment = null;
    Snackbar snackbarPhysicalAudit;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhysicalAuditFragment() {
    }

    public PhysicalAuditFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhysicalAuditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhysicalAuditFragment newInstance(String param1, String param2) {
        PhysicalAuditFragment fragment = new PhysicalAuditFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_physical_audit, container, false);
        getDataFromServer();

        ivBack = view.findViewById(R.id.ivBack);
        btnPhysicalAuditDone = view.findViewById(R.id.btnPhysicalAuditDone);

        snackbarPhysicalAudit = Snackbar.make(container, "", Snackbar.LENGTH_SHORT);
        snackbarPhysicalAudit.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbarPhysicalAudit.setAnchorView(btnPhysicalAuditDone);

        productAdapter = new PhysicalAuditAdapter(getActivity(), billFragment.products);

//        if (billFragment.products.isEmpty()) {
//            snackbarPhysicalAudit.setText("Products not available!");
//            snackbarPhysicalAudit.show();
//        }

        RecyclerView recyclerViewPhysicalAudit = view.findViewById(R.id.recyclerViewPhysicalAudit);
        recyclerViewPhysicalAudit.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPhysicalAudit.setAdapter(productAdapter);

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        btnPhysicalAuditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items = productAdapter.getUpdatedItems();
                sendDataToServer(items);
                snackbarPhysicalAudit.setText("Products saved!");
                Log.d("sent", "onClick: Saved");
            }
        });

        return view;
    }

    private void getDataFromServer() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("AUDIT_PRODUCTS");
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

    private void sendDataToServer(String items) {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("AUDITED_PRODUCTS:" + items);
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
}