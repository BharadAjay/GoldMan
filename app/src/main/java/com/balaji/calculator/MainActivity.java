package com.balaji.calculator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    ImageFragment imageFragment;
    private long pressedTime;
    CoordinatorLayout coordinatorLayoutMain;
    FloatingActionButton fabAppsMain;
    ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
    });
    String[] perms = {"android.permission.CAMERA", "android.permission.ACCESS_NETWORK_STATE", "android.permission.INTERNET", "android.permission.SEND_SMS",
            "android.permission.ACCESS_WIFI_STATE", "android.permission.FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    BottomSheetDialog bottomSheetDialog;
    LinearLayout llBillAppsMain,llDashboard, llRepairAppsMain,llSalesOrderAppMain,llLedgerAppsMain,llFeedbackAppsMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayoutMain = findViewById(R.id.coordinatorLayoutMain);
        fabAppsMain = findViewById(R.id.fabAppsMain);

        requestPermissionLauncher.launch(perms);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_apps_layout);
        llBillAppsMain = bottomSheetDialog.findViewById(R.id.llBillAppsMain);
        llDashboard =bottomSheetDialog.findViewById(R.id.llDashboard);
        llRepairAppsMain = bottomSheetDialog.findViewById(R.id.llRepairAppsMain);
        llSalesOrderAppMain=bottomSheetDialog.findViewById(R.id.llSalesOrderAppMain);
//       llLedgerAppsMain=bottomSheetDialog.findViewById(R.id.llLedgerAppsMain);
//        llFeedbackAppsMain=bottomSheetDialog.findViewById(R.id.llFeedbackAppsMain);


        fabAppsMain.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });

        llBillAppsMain.setOnClickListener(v -> {
            startActivity(new Intent(this, BillActivity.class));
            bottomSheetDialog.dismiss();
        });
        llDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this,Dashboard.class));
            bottomSheetDialog.dismiss();
        });

        llRepairAppsMain.setOnClickListener(v -> {
            startActivity(new Intent(this, RepairActivity.class));
            bottomSheetDialog.dismiss();
        });

        llSalesOrderAppMain.setOnClickListener(v -> {
            startActivity(new Intent(this, activity_sales_order.class));
            bottomSheetDialog.dismiss();
        });

//        llLedgerAppsMain.setOnClickListener(v -> {
//            startActivity(new Intent(this,LedgerTransactionActivity.class));
//            bottomSheetDialog.dismiss();
//        });

//        llFeedbackAppsMain.setOnClickListener(v->{
//            bottomSheetDialog.dismiss();
//        });



    }

    private FragmentActivity getActivity() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Snackbar snackbarConfirmation = Snackbar.make(coordinatorLayoutMain, "Press back again to exit Goldman", Snackbar.LENGTH_SHORT);
            snackbarConfirmation.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
//            snackbarConfirmation.setAnchorView(fabAppsMain);
            snackbarConfirmation.show();
        }
        pressedTime = System.currentTimeMillis();
    }
}