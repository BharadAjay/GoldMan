package com.balaji.calculator;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerDetailsFragment extends Fragment {

    ImageView ivBack;
    ImageButton ibImageCustomerDetails;
    ArrayAdapter arrayAdapterCustomerName;

    PaymentDetailsFragment fragmentPaymentDetails;
    BillFragment billFragment;
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

//    ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//        @Override
//        public void onActivityResult(ActivityResult result) {
//            Uri uri = result.getData().getData();
//            ibImageCustomerDetails.setImageURI(uri);
//            Toast.makeText(getContext(), "called" + uri, Toast.LENGTH_SHORT).show();
//        }
//    });

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerDetailsFragment() {
        // Required empty public constructor
    }

    public CustomerDetailsFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerDetailsFragment newInstance(String param1, String param2) {
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (billFragment.allowDisconnect)
            billFragment.connectSocket();

        billFragment.allowDisconnect = true;
    }

    @Override
    public void onStop() {
        if (billFragment.allowDisconnect) {
            try {
                if (billFragment.socketTask != null) {
                    if (billFragment.socketTask.getTcpClient() != null)
                        billFragment.socketTask.getTcpClient().stopClient();
                    billFragment.socketTask.cancel(true);
                    billFragment.socketTask = null;
                }
                if (billFragment.mTcpClient != null)
                    billFragment.mTcpClient.stopClient();
            } catch (Exception e) {

                e.printStackTrace();
                billFragment.generateLog();
            }
        }
        super.onStop();

        billFragment.generateLog();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            arrayAdapterCustomerName = new ArrayAdapter(getContext(), R.layout.drop_down_item_customer_name, billFragment.listCustomerNames);
            autoCompleteNameCustomerDetails.setAdapter(arrayAdapterCustomerName);
            autoCompleteNameCustomerDetails.setDropDownBackgroundResource(R.drawable.bg_new_green_drop_down_customer_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            billFragment.generateLog();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);

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
                billFragment.allowDisconnect = false;
                sendCustomerData();

                Bundle args = new Bundle();
                args.putBundle("CartFragmentBundle", (Bundle) getArguments().clone());
//                args.putDouble("Previous OS");
                args.putDouble("OpeningAmount", account.getDrAmt());
                args.putString("AccountNumber", account.getAccountNo());

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentPaymentDetails = new PaymentDetailsFragment(billFragment);
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

        billFragment.allowDisconnect = true;
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
                billFragment.generateLog();

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
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {

                                String response = "";
                                String s = encode();
                                int totalLength = s.length();
                                int index = 50000;
                                if (totalLength <= index) {
                                    messageFromSeriver = null;
                                    billFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTIMAGE:" + accountNo + ":" + s);
                                } else {
                                    boolean flag = true;
                                    int start = 0;
                                    while (flag) {
                                        int end = start + index;
                                        String s1 = s.substring(start, end);
                                        messageFromSeriver = null;
                                        billFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTIMAGE:" + accountNo + ":" + s1);

                                        String msg = messageFromSeriver;
                                        start = end;
                                        if ((end + index) >= totalLength) {
                                            s1 = s.substring(start, (totalLength - 1));
                                            messageFromSeriver = null;
                                            billFragment.socketTask.getTcpClient().sendMessageGetUTF("ACCOUNTIMAGE:" + accountNo + ":" + s1);
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

//        if (edtAddressCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtAddressCustomerDetails.setError("Please enter valid address");
//            return false;
//        } else
//            edtAddressCustomerDetails.setError("");
//
//        if (edtCityCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtCityCustomerDetails.setError("Please enter valid city");
//            return false;
//        } else
//            edtCityCustomerDetails.setError("");
//
//        if (edtPhoneCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtPhoneCustomerDetails.setError("Please enter valid phone");
//            return false;
//        } else
//            edtPhoneCustomerDetails.setError("");
//
//        if (edtRefNameCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtRefNameCustomerDetails.setError("Please enter valid reference name");
//            return false;
//        } else
//            edtRefNameCustomerDetails.setError("");
//
//        if (edtPANCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtPANCustomerDetails.setError("Please enter valid PAN number");
//            return false;
//        } else
//            edtPANCustomerDetails.setError("");
//
//        if (edtDrvLicenseCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtDrvLicenseCustomerDetails.setError("Please enter valid driving license");
//            return false;
//        } else
//            edtDrvLicenseCustomerDetails.setError("");
//
//        if (edtElectionCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtElectionCustomerDetails.setError("Please enter valid election card number");
//            return false;
//        } else
//            edtElectionCustomerDetails.setError("");
//
//        if (edtAadharCustomerDetails.getEditText().getText().toString().isEmpty() || edtAadharCustomerDetails.getEditText().getText().toString().length() < 12) {
//            edtAadharCustomerDetails.setError("Please enter valid aadhar card number");
//            return false;
//        } else
//            edtAadharCustomerDetails.setError("");
//
//        if (edtGSTNoCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtGSTNoCustomerDetails.setError("Please enter valid GST number");
//            return false;
//        } else
//            edtGSTNoCustomerDetails.setError("");
//
//        if (edtDOBCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtDOBCustomerDetails.setError("Please select valid date of birth");
//            return false;
//        } else
//            edtDOBCustomerDetails.setError("");
//
//        if (edtAnniversaryCustomerDetails.getEditText().getText().toString().isEmpty()) {
//            edtAnniversaryCustomerDetails.setError("Please select valid anniversary date");
//            return false;
//        } else
//            edtAnniversaryCustomerDetails.setError("");
        return true;
    }

    void sendCustomerData() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
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
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("CUSTOMERDATA:" + gson.toJson(itemLedger));
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
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("CUSTOMERDETAIL:" + msg);
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