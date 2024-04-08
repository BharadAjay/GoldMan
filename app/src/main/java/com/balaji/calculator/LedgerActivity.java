package com.balaji.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import com.balaji.calculator.adapter.LedgerAdapter;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ItemLedger;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class LedgerActivity extends AppCompatActivity implements OnRVItemClickListener<ItemLedger> {
    ImageView ivBack;
    TextInputLayout edtFilterLedger;
    LedgerAdapter adapterLedger;
    List<ItemLedger> listItemLedger = new ArrayList<>();
    RecyclerView rvLedger;
    BillFragment billFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

        ivBack = findViewById(R.id.ivBack);
        edtFilterLedger = findViewById(R.id.edtFilterLedger);
        rvLedger = findViewById(R.id.rvLedger);

        initView();

        ivBack.setOnClickListener(v -> finish());

        edtFilterLedger.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

//        listItemLedger.add(new ItemLedger("Book Infotech", 1000, 10, 10, "+911234567890", "Rajkot"));
//        listItemLedger.add(new ItemLedger("Gold Book", 1000, 10, 10, "+911234567890", "Morbi"));
//        listItemLedger.add(new ItemLedger("Book Infotech", 1000, 10, 10, "+911234567890", "Rajkot"));

        adapterLedger = new LedgerAdapter(this, listItemLedger, this);

        rvLedger.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvLedger.setAdapter(adapterLedger);
    }

    void initView() {
        sendData();
    }

    void filter(String text) {
        List<ItemLedger> temp = new ArrayList<ItemLedger>();
        for (ItemLedger item : listItemLedger) {
            if (item.getAccountName().toLowerCase().contains(text))
                temp.add(item);
        }

        adapterLedger.updateList(temp);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;

        }
        return haveConnectedWifi;
    }

    void sendData() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("LEDGER");
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(this, "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemCLick(int position, ItemLedger item) {
        Intent intentLedgerTransaction = new Intent(this, LedgerTransactionActivity.class);
        startActivity(intentLedgerTransaction);
    }
}