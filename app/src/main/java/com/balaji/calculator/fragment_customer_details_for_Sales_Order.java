package com.balaji.calculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.balaji.calculator.model.ItemLedger;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class fragment_customer_details_for_Sales_Order extends Fragment
{
    ImageView ivBack;
    ImageButton ibImageCustomerDetails;
    ArrayAdapter arrayAdapterCustomerName;

    PaymentDetailsFragment fragmentPaymentDetails;

    fragment_sales_order salesOrderFragment;

    AutoCompleteTextView autoCompleteNameCustomerDetails;
    TextInputLayout edtAddressCustomerDetails, edtCityCustomerDetails, edtPhoneCustomerDetails, edtRefNameCustomerDetails, edtPANCustomerDetails,
            edtDrvLicenseCustomerDetails, edtElectionCustomerDetails, edtAadharCustomerDetails, edtGSTNoCustomerDetails, edtAnniversaryCustomerDetails,
            edtDOBCustomerDetails;

    MaterialTextView tvAccountNoCustomerDetails;
    ExtendedFloatingActionButton efabSaveCustomerDetails;
    Uri uriImage;
    String messageFromSeriver = null;
    ItemLedger account = new ItemLedger();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    boolean isNew = false;
    private static final String TAG = "CustomerDetailFragment";



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public fragment_customer_details_for_Sales_Order() {
        // Required empty public constructor
    }

    public fragment_customer_details_for_Sales_Order(fragment_sales_order  fragmentSalesOrder ) {
        this.salesOrderFragment = fragmentSalesOrder;
    }


    public static fragment_customer_details_for_Sales_Order newInstance(String param1, String param2) {
        fragment_customer_details_for_Sales_Order fragment = new fragment_customer_details_for_Sales_Order();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onStart() {
        super.onStart();

        if (salesOrderFragment.allowDisconnect)
            salesOrderFragment.connectSocket();

        salesOrderFragment.allowDisconnect = true;
    }

    @Override
    public void onStop() {
        if (salesOrderFragment.allowDisconnect) {
            try {
                if (salesOrderFragment.socketTask != null) {
                    if (salesOrderFragment.socketTask.getTcpClient() != null)
                        salesOrderFragment.socketTask.getTcpClient().stopClient();
                    salesOrderFragment.socketTask.cancel(true);
                    salesOrderFragment.socketTask = null;
                }
                if (salesOrderFragment.mTcpClient != null)
                    salesOrderFragment.mTcpClient.stopClient();
            } catch (Exception e) {

                e.printStackTrace();
                salesOrderFragment.generateLog();
            }
        }
        super.onStop();

        salesOrderFragment.generateLog();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            arrayAdapterCustomerName = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, salesOrderFragment.listCustomerNames);
            autoCompleteNameCustomerDetails.setAdapter(arrayAdapterCustomerName);
            autoCompleteNameCustomerDetails.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            salesOrderFragment.generateLog();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_customer_details_for__sales__order, container, false);
        View view = inflater.inflate(R.layout.fragment_customer_details_for__sales__order, container, false);
        ivBack = view.findViewById(R.id.ivBack);
        ibImageCustomerDetails = view.findViewById(R.id.ibImageCustomerDetails);
        autoCompleteNameCustomerDetails = view.findViewById(R.id.autoCompleteNameCustomerDetails);
        tvAccountNoCustomerDetails = view.findViewById(R.id.tvAccountNoCustomerDetails);
        edtAddressCustomerDetails = view.findViewById(R.id.edtAddressCustomerDetails);
        edtCityCustomerDetails = view.findViewById(R.id.edtCityCustomerDetails);
        edtPhoneCustomerDetails = view.findViewById(R.id.edtPhoneCustomerDetails);
        edtRefNameCustomerDetails = view.findViewById(R.id.edtRefNameCustomerDetails);
        edtPANCustomerDetails = view.findViewById(R.id.edtPANCustomerDetails);
        edtDrvLicenseCustomerDetails = view.findViewById(R.id.edtDrvLicenseCustomerDetails);
        edtElectionCustomerDetails = view.findViewById(R.id.edtElectionCustomerDetails);
        edtAadharCustomerDetails = view.findViewById(R.id.edtAadharCustomerDetails);
        edtGSTNoCustomerDetails = view.findViewById(R.id.edtGSTNoCustomerDetails);
        edtDOBCustomerDetails = view.findViewById(R.id.edtDOBCustomerDetails);
        edtAnniversaryCustomerDetails = view.findViewById(R.id.edtAnniversaryCustomerDetails);
        efabSaveCustomerDetails = view.findViewById(R.id.efabSaveCustomerDetails);

        initView();

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        ibImageCustomerDetails.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .cropSquare()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();

        });

        MaterialDatePicker<Long> materialDateOfBirthPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .build();

        MaterialDatePicker<Long> materialAnniversaryDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Anniversary Date")
                .build();

//        edtDOBCustomerDetails.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus)
//                materialDateOfBirthPicker.show(getActivity().getSupportFragmentManager(), null);
//        });

        edtDOBCustomerDetails.getEditText().setOnClickListener(v -> materialDateOfBirthPicker.show(getActivity().getSupportFragmentManager(), null));

        materialDateOfBirthPicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            edtDOBCustomerDetails.getEditText().setText(simpleDateFormat.format(calendar.getTime()));
        });

//        edtAnniversaryCustomerDetails.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus)
//                materialAnniversaryDatePicker.show(getActivity().getSupportFragmentManager(), null);
//        });

        edtAnniversaryCustomerDetails.getEditText().setOnClickListener(v -> materialAnniversaryDatePicker.show(getActivity().getSupportFragmentManager(), null));

        materialAnniversaryDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            edtAnniversaryCustomerDetails.getEditText().setText(simpleDateFormat.format(calendar.getTime()));
        });

        autoCompleteNameCustomerDetails.setOnItemClickListener((parent, view1, position, id) -> {
            autoCompleteNameCustomerDetails.setInputType(InputType.TYPE_NULL);
            isNew = false;
            sendCustomerName(autoCompleteNameCustomerDetails.getText().toString());
        });

        autoCompleteNameCustomerDetails.setOnLongClickListener(v -> {
            autoCompleteNameCustomerDetails.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            isNew = true;
            return true;
        });

        efabSaveCustomerDetails.setOnClickListener(v -> {
            if (isValid()) {
                salesOrderFragment.allowDisconnect = false;
                sendCustomerData();

                Bundle args = new Bundle();
                args.putBundle("CartFragmentBundle", (Bundle) getArguments().clone());
//                args.putDouble("Previous OS");
                args.putDouble("OpeningAmount", account.getDrAmt());
                args.putString("AccountNumber", account.getAccountNo());

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //fragmentPaymentDetails = new PaymentDetailsFragment(salesOrderFragment);
                fragmentPaymentDetails.setArguments(args);

                transaction.replace(R.id.frame_full, fragmentPaymentDetails, "PaymentDetailsFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    void initView() {

        autoCompleteNameCustomerDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                autoCompleteNameCustomerDetails.setError(null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null) {
            uriImage = data.getData();
            ibImageCustomerDetails.setImageURI(uriImage);
            ibImageCustomerDetails.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        salesOrderFragment.allowDisconnect = true;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;

        }
        return haveConnectedWifi;
    }

    String encode() {
        if (uriImage != null) {
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uriImage);

                Bitmap image = BitmapFactory.decodeStream(inputStream, null, null);

                // Encode image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return imageString;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                salesOrderFragment.generateLog();

                return "";
            }
        }
        return null;
    }

    public void sendImage(String accountNo) {
        account.setAccountNo(accountNo);// this is use for send bill detail with account number.... // change by sumit

        if (uriImage == null)
            return;

        if (haveNetworkConnection()) {
            if (salesOrderFragment.socketTask != null) {
                new Thread(() -> {
                    if (salesOrderFragment.socketTask.getTcpClient() != null) {
                        if (salesOrderFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (salesOrderFragment.socketTask.getTcpClient().getSocket().isConnected()) {

                                String response = "";
                                String s = encode();
                                int totalLength = s.length();
                                int index = 50000;
                                if (totalLength <= index) {
                                    messageFromSeriver = null;
                                    salesOrderFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTIMAGE:" + accountNo + ":" + s);
                                } else {
                                    boolean flag = true;
                                    int start = 0;
                                    while (flag) {
                                        int end = start + index;
                                        String s1 = s.substring(start, end);
                                        messageFromSeriver = null;
                                        salesOrderFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTIMAGE:" + accountNo + ":" + s1);

                                        String msg = messageFromSeriver;
                                        start = end;
                                        if ((end + index) >= totalLength) {
                                            s1 = s.substring(start, (totalLength - 1));
                                            messageFromSeriver = null;
                                            salesOrderFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTIMAGE:" + accountNo + ":" + s1);
                                            msg = messageFromSeriver;
                                            response = msg;
                                            flag = false;
                                        } else {
                                            if ((start + index) > totalLength) {
                                                index = (start + index) - (totalLength - 1);

                                            }
                                            Log.d(TAG, "MSG " + msg);
                                            Log.d(TAG, "index " + index);
                                            Log.d(TAG, "start " + start);
                                            Log.d(TAG, "total " + totalLength);

                                        }
                                    }
                                }
                            } else {
//                                setStatus(SocketStatus.DISCONNECTED);
                            }
                        } else {
//                            setStatus(SocketStatus.DISCONNECTED);
                        }
                    } else {
//                        setStatus(SocketStatus.DISCONNECTED);
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    boolean isValid() {
        if (autoCompleteNameCustomerDetails.getText().toString().isEmpty()) {
            autoCompleteNameCustomerDetails.setError("Please enter valid name");
            return false;
        } else
            autoCompleteNameCustomerDetails.setError(null);
        return true;
    }

    void sendCustomerData() {
        if (haveNetworkConnection()) {
            if (salesOrderFragment.socketTask != null) {
                new Thread(() -> {
                    if (salesOrderFragment.socketTask.getTcpClient() != null) {
                        if (salesOrderFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (salesOrderFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                ItemLedger itemLedger = null;

                                itemLedger = new ItemLedger(account.getAccountNo(), autoCompleteNameCustomerDetails.getText().toString(), account.getOpningAmt(), account.getCrAmt(),
                                        account.getDrAmt(), account.getOpnigFine(), account.getOpeningSilver(), edtPhoneCustomerDetails.getEditText().getText().toString(),
                                        edtCityCustomerDetails.getEditText().getText().toString(), edtAddressCustomerDetails.getEditText().getText().toString(),
                                        edtRefNameCustomerDetails.getEditText().getText().toString(), edtPANCustomerDetails.getEditText().getText().toString(),
                                        edtDrvLicenseCustomerDetails.getEditText().getText().toString(), edtElectionCustomerDetails.getEditText().getText().toString(),
                                        edtAadharCustomerDetails.getEditText().getText().toString(), edtGSTNoCustomerDetails.getEditText().getText().toString()
                                        , isNew
                                );

                                Gson gson = new Gson();
                                salesOrderFragment.socketTask.getTcpClient().sendMessageGetUTF("CUSTOMERDATA:" + gson.toJson(itemLedger));
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    void sendCustomerName(String msg) {
        if (haveNetworkConnection()) {
            if (salesOrderFragment.socketTask != null) {
                new Thread(() -> {
                    if (salesOrderFragment.socketTask.getTcpClient() != null) {
                        if (salesOrderFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (salesOrderFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                salesOrderFragment.socketTask.getTcpClient().sendMessageGetUTF("CUSTOMERDETAIL:" + msg);
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    public void fillData(ItemLedger account) {
//        account = homeFragment.listItemLedger.get(0);
        this.account = account;
        tvAccountNoCustomerDetails.setText(account.getAccountNo());
        edtAddressCustomerDetails.getEditText().setText(account.getAddress());
        edtCityCustomerDetails.getEditText().setText(account.getCity());
        edtPhoneCustomerDetails.getEditText().setText(account.getMoblie());
        edtRefNameCustomerDetails.getEditText().setText(account.getRefBy());
        edtPANCustomerDetails.getEditText().setText(account.getPancard());
        edtDrvLicenseCustomerDetails.getEditText().setText(account.getDrivingLicence());
        edtElectionCustomerDetails.getEditText().setText(account.getElectionCard());
        edtAadharCustomerDetails.getEditText().setText(account.getAadhar_Card());
        edtGSTNoCustomerDetails.getEditText().setText(account.getGstTinNo());
        //edtDOBCustomerDetails.getEditText().setText(simpleDateFormat.format(account.getDOB()));
        //edtAnniversaryCustomerDetails.getEditText().setText(simpleDateFormat.format(account.getAnniv()));
    }

}
