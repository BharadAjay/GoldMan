package com.balaji.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.balaji.calculator.adapter.LedgerTransactionAdapter;
import com.balaji.calculator.model.ItemLedgerTransaction;

import java.util.ArrayList;
import java.util.List;

public class LedgerTransactionActivity extends AppCompatActivity {
    ImageView ivBack;
    RecyclerView rvLedgerTransaction;
    List<ItemLedgerTransaction> listItemLedgerTransaction = new ArrayList<>();
    LedgerTransactionAdapter adapterLedgerTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_transaction);

        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());
        rvLedgerTransaction = findViewById(R.id.rvLedgerTransaction);

//        listItemLedgerTransaction.add(new ItemLedgerTransaction("Goldbook", 001, 100.0, 80.0, 10.0, 5.0, 10.0, 5.0));
//        listItemLedgerTransaction.add(new ItemLedgerTransaction("Book Infotech", 001, 100.0, 80.0, 10.0, 5.0, 10.0, 5.0));
//        listItemLedgerTransaction.add(new ItemLedgerTransaction("Goldbook", 001, 100.0, 80.0, 10.0, 5.0, 10.0, 5.0));

        adapterLedgerTransaction = new LedgerTransactionAdapter(this, listItemLedgerTransaction);

        rvLedgerTransaction.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvLedgerTransaction.setAdapter(adapterLedgerTransaction);
    }
}