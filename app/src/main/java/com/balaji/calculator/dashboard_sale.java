package com.balaji.calculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.adapter.udharAdapter;
import com.balaji.calculator.model.udharModel;
import com.balaji.calculator.utils.Utills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class dashboard_sale extends AppCompatActivity {
    ImageView imageView;
    private RecyclerView recyclerView;
    private static final String PREF_NAME = "MyPrefs";
    private udharAdapter adapter;
    private static final String KEY_JSON_DATA = "json_data";

    private Button btnSaveNewUdhar;
    private ArrayList<udharModel> accounts = new ArrayList<>();
    private Map<String, String> accountNameToAccNoMap = new HashMap<>();
    private EditText edtITNameUdhar, edtITCodeUdhar, edtGrWtUdhar, edtLsWtUdhar, edtNtWtUdhar, edtTchUdhar, edtAmtUdhar, edtOtherAmtUdhar;
    private EditText edtWstUdhar, edtTotalTouchUdhar, edtWeightUdhar, edtOthWeightUdhar, edtFinalWeightUdhar, edtNtAmtUdhar, edtRateUdhar;
    private EditText edtOtherRateUdhar, edtPcsUdhar;
 //   private billDetailAdapter adapterbilldetail;
 @BindView(R.id.edtItemTagNoImage)
 EditText edt_tag_no;
    private Button btnUpdate, btnSave;
    private EditText edtTagNumberUdhar;
  //  private ImageView imageView;

    boolean allowDisconnect = true;
    private static final String TAG = "HomeFragment";

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    allowDisconnect = true;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Log.d(TAG, "onActivityResult: " + data.getExtras().get("code"));

                        edt_tag_no.setText(data.getExtras().get("code").toString());
                        //processQrCode();
                    }
                }
            });





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sale);

       // btnUpdate = findViewById(R.id.btnUpdate);

        btnSave = findViewById(R.id.btnSave);

        edtPcsUdhar = findViewById(R.id.edtPcsUdhar);

        btnSaveNewUdhar = findViewById(R.id.btnSaveNewUdhar);



        edtITNameUdhar = findViewById(R.id.edtITNameUdhar);
        edtITCodeUdhar = findViewById(R.id.edtITCodeUdhar);

        edtGrWtUdhar = findViewById(R.id.edtGrWtUdhar);
        edtLsWtUdhar = findViewById(R.id.edtLsWtUdhar);

        edtNtWtUdhar = findViewById(R.id.edtNtWtUdhar);
        edtTchUdhar = findViewById(R.id.edtTchUdhar);

        edtWstUdhar = findViewById(R.id.edtWstUdhar);
        edtTotalTouchUdhar = findViewById(R.id.edtTotalTouchUdhar);

        edtWeightUdhar = findViewById(R.id.edtWeightUdhar);
        edtOthWeightUdhar = findViewById(R.id.edtOthWeightUdhar);

        edtFinalWeightUdhar = findViewById(R.id.edtFinalWeightUdhar);
        edtNtAmtUdhar = findViewById(R.id.edtNtAmtUdhar);

        edtAmtUdhar = findViewById(R.id.edtAmtUdhar);
        edtRateUdhar = findViewById(R.id.edtRateUdhar);

        edtOtherAmtUdhar = findViewById(R.id.edtOtherAmtUdhar);
        edtOtherRateUdhar = findViewById(R.id.edtOtherRateUdhar);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString(KEY_JSON_DATA, "");





//        String id = getIntent().getStringExtra("id");

        String tagNo = getIntent().getStringExtra("tagNo");

        String tch = getIntent().getStringExtra("tch");
        String wst = getIntent().getStringExtra("wst");
        String otherate = getIntent().getStringExtra("otherate");
        String rate = getIntent().getStringExtra("rate");
        String pcs = getIntent().getStringExtra("pcs");



//        String id = getIntent().getStringExtra("id");

        if (tagNo != null) {
            //   btnUpdate.setVisibility(View.VISIBLE);
            btnSaveNewUdhar.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

        }
        else {
            //  btnUpdate.setVisibility(View.GONE);
            btnSaveNewUdhar.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
        }

//Update code is here


      //  displayAccountsFromJsonTagNO(jsonData, tagNo);

        // imageView = findViewById(R.id.imgBackUdhar);

        final Dialog dialog = new Dialog(dashboard_sale.this);
        dialog.setContentView(R.layout.searchable_list_dialog);
        dialog.setTitle("Title");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) (displayMetrics.heightPixels * 0.4);
        int width = (int) (displayMetrics.widthPixels * 0.9);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        recyclerView = dialog.findViewById(R.id.listItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new udharAdapter(accounts, new udharAdapter.dialogClickListener<udharModel>() {
            @Override
            public void onItemCLick(int position, udharModel item) {
                String itName = item.getEdtITNameUdhar();
                edtITNameUdhar.setText(itName);
                String accountNo = item.getEdtITCode();
                edtITCodeUdhar.setText(accountNo);
                Toast.makeText(getApplicationContext(), accountNo, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
       // recyclerView.setAdapter(adapter);

        edtITNameUdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  displayAccountsFromJson(jsonData);

                Button btnCloseDialog = dialog.findViewById(R.id.btnClose);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
       // recyclerView.setAdapter(adapter);

        edtITNameUdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    displayAccountsFromJson(jsonData);

                Button btnCloseDialog = dialog.findViewById(R.id.btnClose);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        edtRateUdhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculation();

            }
        });








        EditText editText1 = findViewById(R.id.edtITNameUdhar);





        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(dashboard_sale.this);
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





        btnSave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String acc_name=edtITNameUdhar.getText().toString();
           // String acc_no=
        }
    });






        imageView = findViewById(R.id.ivBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard_sale.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

       // BillEntry objbill = new BillEntry();
        btnSaveNewUdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from input fields or other sources
                String tagNo = edtTagNumberUdhar.getText().toString().trim();
                String itCode = edtITCodeUdhar.getText().toString().trim();
                String itName = edtITNameUdhar.getText().toString().trim();
                String grWt = edtGrWtUdhar.getText().toString().trim();
                String lsWt = edtLsWtUdhar.getText().toString().trim();
                String ntWt = edtNtWtUdhar.getText().toString().trim();
                String tch = edtTchUdhar.getText().toString().trim();
                String wst = edtWstUdhar.getText().toString().trim();
                String totalTouch = edtTotalTouchUdhar.getText().toString().trim();
                String weight = edtWeightUdhar.getText().toString().trim();
                String othWeight = edtOthWeightUdhar.getText().toString().trim();
                String finalWeight = edtFinalWeightUdhar.getText().toString().trim();
                String othAmt = edtOtherAmtUdhar.getText().toString().trim();
                String othRate = edtOtherRateUdhar.getText().toString().trim();
                String amt = edtAmtUdhar.getText().toString().trim();
                String rate = edtRateUdhar.getText().toString().trim();
                String ntAmt = edtNtAmtUdhar.getText().toString().trim();
                String pcs = edtPcsUdhar.getText().toString().trim();

                // Create an instance of your addUdhardb class
               // addUdharJamadb dbAdapter = new addUdharJamadb(saleActivity.this);

                // Insert data into the "udhar" table
               // dbAdapter.addNewUdhar(tagNo, itCode, itName, grWt, lsWt, ntWt, tch, wst, totalTouch, weight, othWeight, finalWeight, othAmt, othRate, amt, rate, ntAmt, pcs);
// Clear or reset input fields
                edtTagNumberUdhar.setText("");
                edtITCodeUdhar.setText("");
                edtITNameUdhar.setText("");
                edtGrWtUdhar.setText("");
                edtLsWtUdhar.setText("");
                edtPcsUdhar.setText("");
                edtTchUdhar.setText("");
                edtWstUdhar.setText("");
                edtOthWeightUdhar.setText("");
                edtOtherAmtUdhar.setText("");
                edtOtherRateUdhar.setText("");
                edtRateUdhar.setText("");
                // Clear other input fields

                // Refresh the screen by updating the list of records or any other necessary UI updates
                // You may need to re-fetch the list of records and update your RecyclerView or UI elements here.

                // Display a success message or perform other actions as needed
                Toast.makeText(getApplicationContext(), "Udhar stored Successfully", Toast.LENGTH_SHORT).show();

                // Close the database connection
               // dbAdapter.close();
                Intent i = new Intent(dashboard_sale.this, Dashboard.class);
                startActivity(i);


            }
        });

        calculation();



    }

    private void displayAccountsFromJson(String jsonData) {
        try {
            accounts.clear();
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject outerObject = jsonArray.getJSONObject(i);
                JSONArray accountArray = outerObject.optJSONArray("Product");

                if (accountArray != null) {
                    for (int j = 0; j < accountArray.length(); j++) {
                        JSONObject accountObject = accountArray.getJSONObject(j);
                        String itCode = accountObject.optString("ItCode", "");
                        String itName = accountObject.optString("ItName", "");
                        accountNameToAccNoMap.put(itName, itCode);
                        udharModel account = new udharModel(itName, itCode);
                        accounts.add(account);
                    }
                }
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayAccountsFromJsonTagNO(String jsonData, String tagNo) {
        try {
            accounts.clear();
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject outerObject = jsonArray.getJSONObject(i);
                JSONArray accountArray = outerObject.optJSONArray("TagStock");

                if (accountArray != null) {
                    for (int j = 0; j < accountArray.length(); j++) {
                        JSONObject accountObject = accountArray.getJSONObject(j);
                        String LsWt = accountObject.optString("LsWt", "");
                        String OtherAmt = accountObject.optString("OtherAmt", "");
                        String NtWt = accountObject.optString("NtWt", "");
                        String GrWt = accountObject.optString("GrWt", "");
                        String itCode = accountObject.optString("ItCode", "");
                        String itName = accountObject.optString("ItName", "");
                        String accountTagNo = accountObject.optString("TagNo", ""); // Get the TagNo

                        udharModel account = new udharModel(LsWt, OtherAmt, NtWt, GrWt, itName, itCode);
                        account.setTagNo(accountTagNo);
                        accounts.add(account);
                        if (account.getTagNo().equals(tagNo)) {

                            Log.d("Debug", "TagNo Matched: " + tagNo);

                            if (edtOtherAmtUdhar != null) {
                                edtOtherAmtUdhar.setText(account.getOtherAmt());
                            }
                            if (edtNtWtUdhar != null) {
                                edtNtWtUdhar.setText(account.getNtWt());
                            }
                            if (edtGrWtUdhar != null) {
                                edtGrWtUdhar.setText(account.getGrWt());
                            }
                            if (edtLsWtUdhar != null) {
                                edtLsWtUdhar.setText(account.getLsWt());
                            }
                            if (edtITCodeUdhar != null) {
                                edtITCodeUdhar.setText(account.getItCode());
                            }
                            if (edtITNameUdhar != null) {
                                edtITNameUdhar.setText(account.getItName());
                            }
                            break;
                        } else {
//                            displayAccountsFromJson(jsonData);
                        }
                    }
//                    Toast.makeText(this, "" + accounts.size(), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void calculation() {
        try {
            String strGrWt = Utills.getStringOrZero(edtGrWtUdhar.getText().toString().trim());
            double GrWt = Double.parseDouble(strGrWt);

            String strLsWt = Utills.getStringOrZero(edtLsWtUdhar.getText().toString().trim());
            double LsWt = Double.parseDouble(strLsWt);

            double NtWt = GrWt - LsWt;
            edtNtWtUdhar.setText(Utills.formatThreeDigitAfterPoint(NtWt));

            String strTch = Utills.getStringOrZero(edtTchUdhar.getText().toString().trim());
            double Tch = Double.parseDouble(strTch);

            String strWst = Utills.getStringOrZero(edtWstUdhar.getText().toString().trim());
            double Wst = Double.parseDouble(strWst);

            double TotalTouch = Tch + Wst;
            edtTotalTouchUdhar.setText(Utills.formatTwoDigitAfterPoint(TotalTouch));

            double weight = NtWt;
            if (TotalTouch != 0.0) {
                weight = NtWt * (TotalTouch / 100);
            }
            edtWeightUdhar.setText(Utills.formatThreeDigitAfterPoint(weight));


            String strOtherAmt = Utills.getStringOrZero(edtOtherAmtUdhar.getText().toString().trim());
            double OtherAmt = Double.parseDouble(strOtherAmt);

            String strOtherRate = Utills.getStringOrZero(edtOtherRateUdhar.getText().toString());
            double OtherRate = Double.parseDouble(strOtherRate);

            if (OtherRate != 0.0) {
                Log.d("OtherAmt value is", String.valueOf(OtherAmt));

                double OtherWeight = OtherAmt / (OtherRate / 10);

                edtOthWeightUdhar.setText(Utills.formatThreeDigitAfterPoint(OtherWeight));
            }

            String strOthWeight = Utills.getStringOrZero(edtOthWeightUdhar.getText().toString().trim());
            double OthWeight = Double.parseDouble(strOthWeight);

            double FinalWeight = weight + OthWeight;
            edtFinalWeightUdhar.setText(Utills.formatThreeDigitAfterPoint(FinalWeight));

            String strRate = Utills.getStringOrZero(edtRateUdhar.getText().toString().trim());
            double Rate = 0.0;

            if (Utills.isDecimal(strRate)) {
                Rate = Double.parseDouble(strRate);
            }

            double Amt = 0.0;
            if (Rate != 0.0) {
                Amt = NtWt * Rate / 10;
            }

            edtAmtUdhar.setText(Utills.formatTwoDigitAfterPoint(Amt));

            double NtAmt = Amt;
            if (OtherRate == 0.0) {
                NtAmt = OtherAmt + Amt;
            }

            edtNtAmtUdhar.setText(Utills.formatTwoDigitAfterPoint(NtAmt));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


}