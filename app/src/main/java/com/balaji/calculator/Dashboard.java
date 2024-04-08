package com.balaji.calculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {
Button sale,purchase,Rate;
    LinearLayout linearDatebill;
    EditText accountNamedata;
    ImageView imageView, imageViewlist;
    TextView date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        imageViewlist = findViewById(R.id.imgView);

        imageViewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard_Bill_entry_list.class);
                startActivity(intent);
                finish();
            }
        });

        accountNamedata = findViewById(R.id.edtAccountNameBill);

        EditText editText1 = findViewById(R.id.edtAccountNameBill);
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(Dashboard.this);
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



                //calender
        linearDatebill = findViewById(R.id.linearDatebill);
        date = findViewById(R.id.txtDate);
        linearDatebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int year = calendar.get(java.util.Calendar.YEAR);
                int month = calendar.get(java.util.Calendar.MONTH);
                int dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);
//                Log.d("date", String.valueOf(month));

                DatePickerDialog datePickerDialog = new DatePickerDialog(Dashboard.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Handle the selected date
                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                date.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                // Set the initial date to the current date
                datePickerDialog.updateDate(year, month, dayOfMonth);
                // Show the date picker dialog
                datePickerDialog.show();
            }
        });


        sale=findViewById(R.id.Act_Dash_sale);
        purchase= findViewById(R.id.Act_Dash_purchase);
        Rate=findViewById(R.id.Act_Dash_Rate);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), dashboard_sale.class);
                startActivity(intent);
                finish();
            }
        });
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dash_purchase.class);
                startActivity(intent);
                finish();
            }
        });
        Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dash_Rate.class);
                startActivity(intent);
                finish();
            }
        });
    }
}