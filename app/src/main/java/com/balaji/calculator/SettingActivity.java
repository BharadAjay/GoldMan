package com.balaji.calculator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balaji.calculator.helper.SocketTask;
import com.balaji.calculator.helper.TCPClient;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingActivity extends AppCompatActivity {
    private TCPClient mTcpClient = null;
    SocketTask socketTask;
    TextView osVersion, tvLogsLocationSettings;
    TextView appVersion, tvConnectedWifiSettings;
    MaterialSwitch swtDirectPrint;
    LinearLayout llLogsDirectorySettings;

    ActivityResultLauncher<Intent> directorySelectionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            this.getContentResolver().takePersistableUriPermission(result.getData().getData(), takeFlags);
            SecurePreferences.savePreferences(this, "filestorageuri", result.getData().getData().toString());

            Uri baseDocumentTreeUri = Uri.parse(result.getData().getData().toString());
            String[] split = baseDocumentTreeUri.getPath().split(":");

            SecurePreferences.savePreferences(this, "filesLocationSplit", "/" + split[1]);

            tvLogsLocationSettings.setText("/" + split[1]);
        } else
            Log.e("FileUtility", "Some Error Occurred : " + result);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();

        osVersion = findViewById(R.id.osVersion);
        appVersion = findViewById(R.id.appVersion);
        EditText etIp = findViewById(R.id.edtIp);
        EditText etPort = findViewById(R.id.edtPort);
        swtDirectPrint = findViewById(R.id.swtDirectPrint);
        tvConnectedWifiSettings = findViewById(R.id.tvConnectedWifiSettings);
        llLogsDirectorySettings = findViewById(R.id.llLogsDirectorySettings);
        tvLogsLocationSettings = findViewById(R.id.tvLogsLocationSettings);

        osVersion.setText("Version: " + Build.VERSION.RELEASE);
        appVersion.setText("Version: " + BuildConfig.VERSION_NAME);
//        tvConnectedWifiSettings.setText(ssid.replace('"', ' ').trim());

        if ("Checked".equals(SecurePreferences.getStringPreference(this, "DirectPrint"))) {
            swtDirectPrint.setChecked(true);
        }
        if (!SecurePreferences.getStringPreference(this, "ip").isEmpty()) {
            etIp.setText(SecurePreferences.getStringPreference(this, "ip"));
        }
        if (SecurePreferences.getIntegerPreference(this, "port") != 0) {
            etPort.setText("" + SecurePreferences.getIntegerPreference(this, "port"));
        }
        if (!SecurePreferences.getStringPreference(this, "savedNetwork").isEmpty()) {
            tvConnectedWifiSettings.setText(SecurePreferences.getStringPreference(this, "savedNetwork"));
        }
        if (!SecurePreferences.getStringPreference(this, "filesLocationSplit").isEmpty())
            tvLogsLocationSettings.setText(SecurePreferences.getStringPreference(this, "filesLocationSplit"));

        findViewById(R.id.mbtnSetSetting).setOnClickListener(v -> {
            socketTask = new SocketTask(this, etIp.getText().toString().trim(), Integer.parseInt(etPort.getText().toString()), onMessage -> {
            }, mTcpClient, onError -> {
            });

            SecurePreferences.savePreferences(this, "ip", etIp.getText().toString().trim());
            SecurePreferences.savePreferences(this, "port", Integer.parseInt(etPort.getText().toString()));
            SecurePreferences.savePreferences(this, "savedNetwork", ssid.replace('"', ' ').trim());

            Toast.makeText(this, "Ip address Saved", Toast.LENGTH_SHORT).show();
        });

        llLogsDirectorySettings.setOnClickListener(v -> {
            Intent intentDirectory = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            directorySelectionLauncher.launch(intentDirectory);
        });

        swtDirectPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SecurePreferences.savePreferences(SettingActivity.this, "DirectPrint", "Checked");
                } else {
                    SecurePreferences.savePreferences(SettingActivity.this, "DirectPrint", "Unchecked");
                }
            }
        });

        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onDestroy() {
        try {
            if (mTcpClient != null) {
                mTcpClient.sendMessageGetUTF("bye");
                mTcpClient.stopClient();
                socketTask.cancel(true);
                socketTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}