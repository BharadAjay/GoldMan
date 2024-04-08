package com.balaji.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class activity_sales_order extends AppCompatActivity
{
    fragment_sales_order fragmentSalesOrder=new fragment_sales_order();
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);

        frameLayout=(FrameLayout)findViewById(R.id.frame_full);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_full, fragmentSalesOrder, "HomeFragment");
        transaction.commit();
    }
}