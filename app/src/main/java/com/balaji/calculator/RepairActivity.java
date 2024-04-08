package com.balaji.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class RepairActivity extends AppCompatActivity {

    RepairBuildFragment repairBuildFragment = new RepairBuildFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.replace_container, repairBuildFragment, "RepairBuildFragment");
        transaction.commit();
    }
}