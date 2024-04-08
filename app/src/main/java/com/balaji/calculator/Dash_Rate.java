package com.balaji.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Dash_Rate extends AppCompatActivity {
    ImageView ivBack;
    private int intlayout= 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_rate);
        ivBack = findViewById(R.id.ivBack);
        intlayout=2;
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dash_Rate.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
//
//    @Override
//    public void onBackPressed() {
//       setContentView(R.layout.activity_dashboard);
//    }
}