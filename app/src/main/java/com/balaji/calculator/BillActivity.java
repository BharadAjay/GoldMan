package com.balaji.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class BillActivity extends AppCompatActivity {
    BillFragment billFragment = new BillFragment();
    FrameLayout frame_full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        frame_full = findViewById(R.id.frame_full);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_full, billFragment, "HomeFragment");
        transaction.commit();
    }
}