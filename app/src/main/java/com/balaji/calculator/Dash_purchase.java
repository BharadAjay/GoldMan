package com.balaji.calculator;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Dash_purchase extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_purchase);
        imageView = findViewById(R.id.ivBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dash_purchase.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        EditText itname;

        itname=findViewById(R.id.edtITNameJama);
        itname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(Dash_purchase.this);
                dialog.setContentView(R.layout.searchable_list_dialog);
                dialog.setTitle("Title");

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = (int) (displayMetrics.heightPixels * 0.4);
                int width = (int) (displayMetrics.widthPixels * 0.9);
                dialog.getWindow().setLayout(width, height);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                dialog.show();

            }
        });

    }

}