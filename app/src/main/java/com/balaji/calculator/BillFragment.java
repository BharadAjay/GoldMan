package com.balaji.calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.adapter.ProductSearchAdapter;
import com.balaji.calculator.helper.OnSocketError;
import com.balaji.calculator.helper.SocketParam;
import com.balaji.calculator.helper.SocketStatus;
import com.balaji.calculator.helper.SocketTask;
import com.balaji.calculator.helper.TCPClient;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.BillTypeModel;
import com.balaji.calculator.model.Group;
import com.balaji.calculator.model.ItemLedger;
import com.balaji.calculator.model.ItemLedgerTransaction;
import com.balaji.calculator.model.ItemModel;
import com.balaji.calculator.model.OldGoldModel;
import com.balaji.calculator.model.PaymentDetailsModel;
import com.balaji.calculator.model.ProductItem;
import com.balaji.calculator.model.TagResponse;
import com.balaji.calculator.qrcode.QrScanActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BillFragment extends Fragment implements TextWatcher, OnRVItemClickListener<String> {



    public BillFragment billFragment;

    @BindView(R.id.edt_22_carat)
    EditText edt_22_carat;

    @BindView(R.id.edt_item_name)
    EditText edt_item_name;

    @BindView(R.id.edtItemTagNoImage)
    EditText edt_tag_no;
    @BindView(R.id.edt_piece)
    EditText edt_piece;
    @BindView(R.id.edt_net_weight)
    EditText edt_net_weight;

    @BindView(R.id.edt_gross_weight)
    EditText edt_gross_weight;

    @BindView(R.id.edt_ghat_weight)
    EditText edt_ghat_weight;

    @BindView(R.id.edt_less_weight)
    EditText edt_less_weight;
    @BindView(R.id.spinner_carat)
    Spinner spinner_carat;

    @BindView(R.id.edt_item_price)
    EditText edt_item_price;

    @BindView(R.id.edt_make_charge)
    EditText edt_make_charge;

    @BindView(R.id.spinner_making)
    Spinner spinner_making;

    @BindView(R.id.edt_making_total)
    EditText edt_making_total;

    @BindView(R.id.edt_charge1)
    EditText edt_charge1;

    @BindView(R.id.edt_charge2)
    EditText edt_charge2;

    @BindView(R.id.edt_cust_name)
    EditText edt_cust_name;

    @BindView(R.id.edt_customer_name)
    EditText edt_customer_name;

    @BindView(R.id.edt_date)
    EditText edt_date;

    @BindView(R.id.txt_total)
    TextView txt_total;

    @BindView(R.id.rec_item)
    RecyclerView rec_item;

    @BindView(R.id.txt_gst)
    TextView txt_gst;
    @BindView(R.id.edt_gst)
    public EditText edt_gst;

    @BindView(R.id.txt_final_total)
    TextView txt_final_total;

    @BindView(R.id.txt_final_net_total)
    TextView txt_final_net_total;

    @BindView(R.id.btn_add)
    Button btn_add;

    @BindView(R.id.btnSetting)
    AppCompatImageButton btnSetting;

    @BindView(R.id.coLayout)
    CoordinatorLayout coLayout;

    @BindView(R.id.nestedScrollViewFilterLedger)
    NestedScrollView nestedScrollView;
    @BindView(R.id.fabCart)
    FloatingActionButton fabCart;

    boolean is_edit;
    int item_id;
    Snackbar snackbar;
    List<String> carat_list = new ArrayList<>();
    List<String> making_list = new ArrayList<>();
    List<PhysicalAuditItem> products = new ArrayList<>();
    List<ProductItem> avilableProducts = new ArrayList<>();
    ProductFragment fragmentProduct;
    PhysicalAuditFragment fragmentPhysicalAudit;
    LedgerFragment fragmentLedger;
    CartFragment fragmentCart;
    ImageFragment imageFragment;
    List<Group> groups = new ArrayList<>();
    List<ItemModel> cartProducts = new ArrayList<>();
    List<ItemModel> list_item;
    List<ItemLedger> listItemLedger = new ArrayList<>();
    List<ItemLedgerTransaction> listItemLedgerTransaction = new ArrayList<>();
    List<BillTypeModel> listBillTypeModel = new ArrayList<>();
    String[] listCustomerNames, arrNarration, arrCompanyBank;
    List<String> arrItemNames;
    List<String> listBillType = new ArrayList<>();
    DbHelper dbHelper;
    String LabourValueCal = "Actual Value";
    String LabourOn = "Net";

    String RateOn = "Net";
    String command ="";
    double Touch = 0.0;
    String imgSavedPath;
    Double total = 0.0;
    Double sub_total = 0.0;
    Double finaltotal = 0.0;
    Double finalNetWeight = 0.0;
    boolean isAnySelected = false;
    boolean allowDisconnect = true;
    int voucherId = 0;
    String AccountNumber = "";
    String img;
    File[] listFile;
    File imgDirectory;
    AlertDialog alertDialogBill;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    View customDialogViewBill;
    MaterialButton mbtnDismissDialogBill;
    List<String> productSearchList = new ArrayList<>();
    ProductSearchAdapter productSearchAdapter;
    RecyclerView rvItemSearchBill;
    TextInputLayout edtProductSearchDialogBill;
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
                        processQrCode();
                    }
                }
            });
    ActivityResultLauncher<ScanOptions> qrScanActivityResultLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    edt_tag_no.setText(result.getContents());
                    processQrCode();
                }
            }
    );
    ActivityResultLauncher<Intent> directorySelectionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            getContext().getContentResolver().takePersistableUriPermission(result.getData().getData(), takeFlags);
            SecurePreferences.savePreferences(getActivity(), "filestorageuri", result.getData().getData().toString());

            Uri baseDocumentTreeUri = Uri.parse(result.getData().getData().toString());
            String[] split = baseDocumentTreeUri.getPath().split(":");

            SecurePreferences.savePreferences(getActivity(), "filesLocationSplit", "/" + split[1]);
        } else {
            Log.e("FileUtility", "Some Error Occurred : " + result);
            generateLog();
        }
    });

    TCPClient mTcpClient = null;
    SocketTask socketTask = null;
    TCPClient.OnMessageReceived tcpListener;
    private OnSocketError onSocketError;
    private static final String TAG = "HomeFragment";
    BillFragment homefragment;

    private void processQrCode() {

        if (edt_tag_no.getText().toString().trim() == null || edt_tag_no.getText().toString().isEmpty()) {
            return;
        }
        if (SecurePreferences.getStringPreference(getActivity(), "ip").isEmpty()) {
            Toast.makeText(getActivity(), "Please set ip first.", Toast.LENGTH_SHORT).show();
            setStatus(SocketStatus.ERROR);
            return;
        }
        if (haveNetworkConnection()) {
            if (socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (socketTask.getTcpClient() != null) {
                        if (socketTask.getTcpClient().getSocket() != null) {
                            if (socketTask.getTcpClient().getSocket().isConnected()) {
                                // clear krvo
                                TCPClient.bitmap=null;
                                socketTask.getTcpClient().sendMessageGetImage("TAGIMAGE:" + edt_tag_no.getText().toString().trim());

                                socketTask.getTcpClient().sendMessageGetUTF("TAG:" + edt_tag_no.getText().toString().trim());

                                if ("Checked".equals(SecurePreferences.getStringPreference(getActivity(), "DirectPrint"))) {
                                    socketTask.getTcpClient().sendMessageGetUTF("TAGPRINT:" + edt_tag_no.getText().toString().trim());
                                }
                            } else {
                                setStatus(SocketStatus.DISCONNECTED);
                            }
                        } else {
                            setStatus(SocketStatus.DISCONNECTED);
                        }
                    } else {
                        setStatus(SocketStatus.DISCONNECTED);
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {





        homefragment = this;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bill, container, false);
        ButterKnife.bind(this, view);
        dbHelper = new DbHelper(getActivity());
        customDialogViewBill = inflater.inflate(R.layout.dialog_search_item_bill_layout, null);
        mbtnDismissDialogBill = customDialogViewBill.findViewById(R.id.mbtnDismissDialogBill);
        rvItemSearchBill = customDialogViewBill.findViewById(R.id.rvItemSearchBill);
        edtProductSearchDialogBill = customDialogViewBill.findViewById(R.id.edtProductSearchDialogBill);

        homefragment.allowDisconnect = true;

        productSearchAdapter = new ProductSearchAdapter(getContext(), productSearchList, this);

        initView();

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                    if (socketTask != null) {
                        new Thread(() -> {
                            // Run whatever background code you want here.
                            if (socketTask.getTcpClient() != null) {
                                if (socketTask.getTcpClient().getSocket() != null) {
                                    if (socketTask.getTcpClient().getSocket().isConnected()) {
//                                        socketTask.getTcpClient().sendMessage("PRODUCTS");

                                    } else {
                                        setStatus(SocketStatus.DISCONNECTED);
                                    }
                                } else {
                                    setStatus(SocketStatus.DISCONNECTED);
                                }
                            } else {
                                setStatus(SocketStatus.DISCONNECTED);
                            }
                        }).start();
                    }
                } else
                    Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu = new PopupMenu(getContext(), btnSetting);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.optionSetting) {
                            Intent intentSettings = new Intent(getContext(), SettingActivity.class);
                            startActivity(intentSettings);
                        } else if (item.getItemId() == R.id.optionProducts) {
                            allowDisconnect = false;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentProduct = new ProductFragment(homefragment);
                            transaction.replace(R.id.frame_full, fragmentProduct, "ProductFragment");
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (item.getItemId() == R.id.optionTagImage) {
                            allowDisconnect = false;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            imageFragment = new ImageFragment(homefragment);
                            transaction.replace(R.id.frame_full, imageFragment, "ImageFragment");
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (item.getItemId() == R.id.optionPhysicalAudit) {
                            allowDisconnect = false;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentPhysicalAudit = new PhysicalAuditFragment(homefragment);
                            transaction.replace(R.id.frame_full, fragmentPhysicalAudit, "PhysicalAuditFragment");
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (item.getItemId() == R.id.optionCustomerFeedback) {
                            allowDisconnect = false;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            CustomerFeedback fragmentCustomerFeedback = new CustomerFeedback(homefragment);
                            transaction.replace(R.id.frame_full, fragmentCustomerFeedback, "CustomerFeedback");
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (item.getItemId() == R.id.optionLedger) {
                            allowDisconnect = false;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentLedger = new LedgerFragment(homefragment);
                            transaction.replace(R.id.frame_full, fragmentLedger, "LedgerFragment");
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (item.getItemId() == R.id.optionDeleteAll) {
                            onDeleteAll();
                            DbHelper.deleteDb();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        mbtnDismissDialogBill.setOnClickListener(v -> {
            alertDialogBill.dismiss();
        });

        fabCart.setOnClickListener(v -> {
//            if (haveNetworkConnection()) {
//                if (socketTask != null) {
//                    Toast.makeText(getActivity(), "Here", Toast.LENGTH_SHORT).show();
//                    new Thread(() -> {
//                        // Run whatever background code you want here.
//                        if (socketTask.getTcpClient() != null) {
//                            if (socketTask.getTcpClient().getSocket() != null) {
//                                if (socketTask.getTcpClient().getSocket().isConnected()) {
////                                    socketTask.getTcpClient().sendMessage("CUSTOMERLIST");
//                                } else {
//                                    setStatus(SocketStatus.DISCONNECTED);
//                                }
//                            } else {
//                                setStatus(SocketStatus.DISCONNECTED);
//                            }
//                        } else {
//                            setStatus(SocketStatus.DISCONNECTED);
//                        }
//                    }).start();
//                }
//            } else
//                Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();

            allowDisconnect = false;
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentCart = new CartFragment(homefragment);
            transaction.replace(R.id.frame_full, fragmentCart, "CartFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            onAddClick();
        });

        tcpListener = message1 -> {
            getActivity().runOnUiThread(() -> {
                try {
                    if (message1 != null) {
                        String[] msg = message1.split("!");
                        if (msg.length > 1) {
                            if (msg[0].equals("PRODUCTS")) {
                                Gson gson = new Gson();
                                ProductItem[] list = gson.fromJson(msg[1], ProductItem[].class);
                                avilableProducts.clear();
                                int totalCount = 0;
                                for (ProductItem pd : list) {
                                    avilableProducts.add(new ProductItem(pd.getProductName() + " - " + pd.getCount()));
                                    totalCount += pd.getCount();
                                }
                                fragmentProduct.productAdapter.notifyDataSetChanged();
//                                fragmentPhysicalAudit.productAdapter.notifyDataSetChanged();

                                fragmentProduct.tvProductsTitle.setText("Products - " + totalCount);
                            } else if (msg[0].equals("AUDIT_PRODUCTS")) {
                                Gson gson = new Gson();
                                ProductItem[] list = gson.fromJson(msg[1], ProductItem[].class);
                                products.clear();
                                for (ProductItem pd : list) {
                                    products.add(new PhysicalAuditItem(pd.getProductName(), 0));
                                }
//                                fragmentProduct.productAdapter.notifyDataSetChanged();
                                fragmentPhysicalAudit.productAdapter.notifyDataSetChanged();

                            } else if (msg[0].equals("GROUPS")) {
                                Gson gson = new Gson();
                                ProductItem[] list = gson.fromJson(msg[1], ProductItem[].class);
                                groups.clear();

                                for (ProductItem pd : list) {
                                    Log.d("pd.getNAme", pd.getProductgroupName());
                                    groups.add(new Group(pd.getProductgroupName() + " - " + pd.getCount()));
                                }
                                fragmentProduct.fragmentGroup.groupAdapter.notifyDataSetChanged();
                            } else if (msg[0].equals("LEDGER")) {
                                Gson gson = new Gson();
                                ItemLedger[] list = gson.fromJson(msg[1], ItemLedger[].class);
                                listItemLedger.clear();

                                for (ItemLedger itemLedger : list) {
                                    listItemLedger.add(new ItemLedger(itemLedger.getAccountName(), itemLedger.getAccountNo(), itemLedger.getAcGpName(), itemLedger.getOpningAmt(),
                                            itemLedger.getOpnigFine(), itemLedger.getOpeningSilver(), itemLedger.getMoblie(), itemLedger.getCity()));
                                }

                                fragmentLedger.refreshData();
                                fragmentLedger.isDataLoadingLedger(false);
                            } else if (msg[0].equals("CUSTOMERLIST")) {
                                Gson gson = new Gson();
                                listCustomerNames = gson.fromJson(msg[1], String[].class);
//                                listItemLedger.clear();
//                                listCustomerNames = new String[list.length];
//
//                                int i = 0;
//                                for (String itemLedger : list) {
////                                    Toast.makeText(getActivity(), "" + itemLedger.getAccountName(), Toast.LENGTH_SHORT).show();
////                                    listItemLedger.add(new ItemLedger(itemLedger.getAccountName(), itemLedger.getAccountNo(), itemLedger.getOpningAmt(), itemLedger.getOpnigFine(), itemLedger.getOpeningSilver(), itemLedger.getMoblie(), itemLedger.getCity()));
//                                    listCustomerNames[i] = itemLedger;
////                                    Log.d("limitDtaa", "onCreateView: "+itemLedger.getAccountName());
//                                    i++;
//                                    break;
//                                }

                                //fragmentLedger.adapterLedger.notifyDataSetChanged();
                            } else if (msg[0].equals("ItemNameList")) {
                                Gson gson = new Gson();
                                int i = 0;
//                                productSearchList = Arrays.asList(gson.fromJson(msg[1], String[].class));

                                productSearchList.clear();
                                for (String s : gson.fromJson(msg[1], String[].class)) {
                                    productSearchList.add(i, s);
                                    productSearchAdapter.notifyItemInserted(i);
                                    i++;
                                }
//                                Toast.makeText(getActivity(), "ItemNameList RCVD", Toast.LENGTH_SHORT).show();
//                                productSearchAdapter.notifyDataSetChanged();
                            } else if (msg[0].equals("CUSTOMERDETAIL")) {
                                Gson gson;
                                GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                                gson = gsonBuilder.create();
                                ItemLedger itemLedgerList = gson.fromJson(msg[1], ItemLedger.class);
//                                listItemLedger.clear();
//                                listItemLedger.add(itemLedgerList);

                                fragmentCart.fragmentCustomerDetails.fillData(itemLedgerList);

//                                listCustomerNames = new String[list.length];
//
//                                int i = 0;
//                                for (String itemLedger : list) {
////                                    Toast.makeText(getActivity(), "" + itemLedger.getAccountName(), Toast.LENGTH_SHORT).show();
////                                    listItemLedger.add(new ItemLedger(itemLedger.getAccountName(), itemLedger.getAccountNo(), itemLedger.getOpningAmt(), itemLedger.getOpnigFine(), itemLedger.getOpeningSilver(), itemLedger.getMoblie(), itemLedger.getCity()));
//                                    listCustomerNames[i] = itemLedger;
////                                    Log.d("limitDtaa", "onCreateView: "+itemLedger.getAccountName());
//                                    i++;
//                                    break;
//                                }

                                //fragmentLedger.adapterLedger.notifyDataSetChanged();
                            } else if (msg[0].equals("ACCOUNTDETAIL")) {
                                Gson gson = new Gson();
                                listItemLedgerTransaction.clear();

                                for (ItemLedgerTransaction itemLedgerTransaction : gson.fromJson(msg[1], ItemLedgerTransaction[].class)) {
                                    listItemLedgerTransaction.add(new ItemLedgerTransaction(itemLedgerTransaction.getBook(), itemLedgerTransaction.getVchNo(),
                                            itemLedgerTransaction.getVchDate(), itemLedgerTransaction.getCrAmt(), itemLedgerTransaction.getDrAmt(),
                                            itemLedgerTransaction.getCrFine(), itemLedgerTransaction.getDrFine(), itemLedgerTransaction.getCrSilver(),
                                            itemLedgerTransaction.getDrSilver()));
                                }
                                fragmentLedger.fragmentLedgerTransaction.adapterLedgerTransaction.notifyDataSetChanged();

                            } else if (msg[0].equals("ACCOUNTNO")) {
                                AccountNumber = msg[1];
                                fragmentCart.fragmentCustomerDetails.sendImage(msg[1]);
                            } else if (msg[0].equals("SAVE_ACCOUNT")) {

                            } else if (msg[0].equals("VIEWBILL")) {
                            } else if (msg[0].equals("TAGLISTS")) {

                            } else if (msg[0].equals("BillType")) {
                                Gson gson = new Gson();
                                BillTypeModel[] arrayBillType = gson.fromJson(msg[1], BillTypeModel[].class);
                                listBillType.clear();
                                listBillTypeModel.clear();
                                for (BillTypeModel btm : arrayBillType) {
                                    listBillTypeModel.add(new BillTypeModel(btm.getBillName(), btm.getTaxType(), btm.getBillId()));
                                    listBillType.add(btm.getBillName());
                                }
//                                fragmentCart.billTypeArrayAdapter.notifyDataSetChanged();
                            } else if (msg[0].equals("PAYMENTDETAILS")) {
                                voucherId = Integer.parseInt(msg[1]);
                                if (voucherId > 0) {
                                    homefragment.fragmentCart.fragmentCustomerDetails.fragmentPaymentDetails.alertDialogPaymentDetails = homefragment.fragmentCart.fragmentCustomerDetails.fragmentPaymentDetails.materialAlertDialogBuilder.show();
                                    onDeleteAll();

                                    homefragment.fragmentCart.fragmentCustomerDetails.fragmentPaymentDetails.sendBillViewRequest();
                                }
                            } else if (msg[0].equals("PRINTBILL")) {
                            } else if (msg[0].equals("SAVE")) {
                                imageFragment.snackbarMessage("SAVE");
                            } else if (msg[0].equals("FAIL")) {
                                imageFragment.snackbarMessage("FAIL");
                            } else if (msg[0].equals("NARRATIONLIST")) {
                                Gson gson = new Gson();
                                arrNarration = gson.fromJson(msg[1], String[].class);
//                                arrNarration = new String[10];
//                                arrNarration[0] = "ATM";
                            } else if (msg[0].equals("BANKLIST")) {
                                Gson gson = new Gson();
                                arrCompanyBank = gson.fromJson(msg[1], String[].class);
                            }
                        } else if (message1.equals(SocketParam.CONNECTED)) {
                            setStatus(SocketStatus.CONNECTED);
                        } else if (message1.equals("SAVE")) {
                            imageFragment.snackbarMessage("SAVE");
                        } else if (msg[0].equals("FAIL")) {
                            imageFragment.snackbarMessage("FAIL");
                        } else if (msg[0].equals("Tag Not Found")) {
                            imageFragment.snackbarMessage("Tag Not Found");
                        } else {
                            handleQrcodeResponse(message1);
                        }
//                        else if (!message1.equals("Done") && !message1.equals("SAVE")) {
//                            handleQrcodeResponse(message1);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    generateLog();
                }
            });
        };

        onSocketError = error -> {
            getActivity().runOnUiThread(() -> {
                setStatus(SocketStatus.ERROR);
            });
        };

        edt_tag_no.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    processQrCode();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    void connectSocket() {
        if (socketTask != null) {
//            new Thread(() -> {
//                // Run whatever background code you want here.
//                socketTask.getTcpClient().sendMessage("");
//            }).start();
            if (socketTask.getTcpClient() != null)
                socketTask.getTcpClient().stopClient();

            socketTask.cancel(true);
            socketTask = null;
        }
        String ip = "";
        int port = 0;
        setStatus(SocketStatus.CONNECTING);
        if (!SecurePreferences.getStringPreference(getActivity(), "ip").isEmpty()) {
            ip = SecurePreferences.getStringPreference(getActivity(), "ip");
        } else {
            setStatus(SocketStatus.ERROR);
            return;
        }
        if (SecurePreferences.getIntegerPreference(getActivity(), "port") != 0) {
            port = SecurePreferences.getIntegerPreference(getActivity(), "port");
        } else {
            setStatus(SocketStatus.ERROR);
            return;
        }

        socketTask = new SocketTask(getActivity(), ip, port, tcpListener, mTcpClient, onSocketError);
        socketTask.execute();
    }

    void getBillType() {
        if (haveNetworkConnection()) {
            if (socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (socketTask.getTcpClient() != null) {
                        if (socketTask.getTcpClient().getSocket() != null) {
                            if (socketTask.getTcpClient().getSocket().isConnected()) {
                                socketTask.getTcpClient().sendMessageGetUTF("BillType");
                            } else {
                                setStatus(SocketStatus.DISCONNECTED);
                            }
                        } else {
                            setStatus(SocketStatus.DISCONNECTED);
                        }
                    } else {
                        setStatus(SocketStatus.DISCONNECTED);
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    void getItemNames() {
        if (haveNetworkConnection()) {
            if (socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (socketTask.getTcpClient() != null) {
                        if (socketTask.getTcpClient().getSocket() != null) {
                            if (socketTask.getTcpClient().getSocket().isConnected()) {
                                socketTask.getTcpClient().sendMessageGetUTF("ItemNameList");
                            } else {
                                setStatus(SocketStatus.DISCONNECTED);
                            }
                        } else {
                            setStatus(SocketStatus.DISCONNECTED);
                        }
                    } else {
                        setStatus(SocketStatus.DISCONNECTED);
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    private void handleQrcodeResponse(String message1) {
        try {
            Gson gson = new Gson();
            TagResponse res = gson.fromJson(message1, TagResponse.class);
            edt_item_name.setText(res.getItName());
            LabourValueCal = res.getLabourValuCal();
            LabourOn = res.getLabourOn();
            Touch = res.getTouch();
            edt_gross_weight.setText("" + res.getGrWet());
            edt_less_weight.setText("" + res.getLessWet());
            edt_net_weight.setText("" + res.getNetWet());
            edt_ghat_weight.setText("" + res.getGhatWet());
            edt_tag_no.setText(res.getTagNo());
            edt_make_charge.setText("" + res.getLbrRate());
            edt_22_carat.setText("" + res.getRate());
            edt_piece.setText("" + res.getPsc());

            if (res.getLbrType().equals("Per")) {
                spinner_making.setSelection(0);
            } else if (res.getLbrType().equals("Pcs")) {
                spinner_making.setSelection(1);
            } else {
                spinner_making.setSelection(2);
            }

            if (res.getLbrRate() == 0 && res.getLbrAmt() > 0.0) {
                spinner_making.setSelection(1);
                edt_make_charge.setText("" + res.getLbrAmt());
            }

            edt_charge1.setText("" + res.getOtherAmt());
            if(res.getRate()>0){
                edt_22_carat.setText("" + res.getRate());
            }
            else if(res.getMRP()>0){
                //edt_22_carat.setText("" + res.getMRP());
                edt_item_price.setText("" + res.getMRP());
                RateOn = res.getRateOn();


            }
            img = res.getTagImage();

            imgSavedPath = saveToInternalStorage(TCPClient.bitmap, res.getTagNo());

            Log.d("imgSavedPath", "imgSavedPath: " + imgSavedPath);

            getView().findViewById(R.id.btn_add).performClick();
        } catch (Exception e) {
            e.printStackTrace();
            generateLog();
        }
    }

    void filter(String text) {
        List<String> temp = new ArrayList<String>();
        for (String item : productSearchList) {
            if (item.toLowerCase().contains(text))
                temp.add(item);
        }

        productSearchAdapter.updateList(temp);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        if (SecurePreferences.getStringPreference(getActivity(), "filestorageuri").isEmpty()) {
            Intent intentDirectory = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            directorySelectionLauncher.launch(intentDirectory);
        }

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS", 0);
        int lastDay = settings.getInt("day", 0);

        if (lastDay != currentDay) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();

            File logDir = new File("sdcard", "Goldman");

            if (logDir.exists()) {
                File file = new File(logDir, "GoldmanLog.txt");

                if (file.exists()) {
                    boolean deleted = file.delete();
                }
            }
        }

        isAnySelected = false;
        isAnySelected();

        cartProducts.clear();

        btnSetting.performClick();
        carat_list.add("22 carat");

        making_list.clear();
        making_list.add("Per");
        making_list.add("Pcs");
        making_list.add("Grm");

        spinner_carat.setAdapter(new CustomAdapter());

        spinner_carat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                calculatePrice();
                calculateMaking();
                calculateTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_making.setAdapter(new MakingAdapter());
        spinner_making.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculateMaking();
                calculatePrice();
                calculateTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_22_carat.addTextChangedListener(this);

        edt_gross_weight.addTextChangedListener(this);
        edt_less_weight.addTextChangedListener(this);
        edt_ghat_weight.addTextChangedListener(this);

        edt_tag_no.addTextChangedListener(this);

        edt_make_charge.addTextChangedListener(this);

        edt_charge1.addTextChangedListener(this);
        edt_charge2.addTextChangedListener(this);
        edt_gst.addTextChangedListener(this);

        list_item = dbHelper.getItems();

        if (!list_item.isEmpty()) {
            rec_item.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            rec_item.setNestedScrollingEnabled(false);
            rec_item.setAdapter(new ItemAdapter());
            rec_item.setVisibility(View.VISIBLE);
        } else {
            rec_item.setVisibility(View.GONE);
        }

        edt_tag_no.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edt_tag_no.getRight() - edt_tag_no.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    allowDisconnect = false;
                    ScanOptions options = new ScanOptions();
                    options.setBeepEnabled(true);
                    options.setOrientationLocked(true);
                    options.setCaptureActivity(QrScanActivity.class);
                    qrScanActivityResultLauncher.launch(options);

                    return true;
                }
            }
            return false;
        });


        edt_item_name.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edt_tag_no.getRight() - edt_tag_no.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    alertDialogBill.show();
                    return true;
                }
            }
            return false;
        });

        Calendar calender = Calendar.getInstance();
        edt_date.setText(calender.get(Calendar.DAY_OF_MONTH) + "-" + (calender.get(Calendar.MONTH) + 1) + "-" + calender.get(Calendar.YEAR));

        if (!SecurePreferences.getStringPreference(getActivity(), "22Carat").isEmpty())
            edt_22_carat.setText(SecurePreferences.getStringPreference(getActivity(), "22Carat"));

        if (SecurePreferences.getStringPreference(getActivity(), "Gst").isEmpty())
            edt_gst.setText("3");
        else
            edt_gst.setText(SecurePreferences.getStringPreference(getActivity(), "Gst"));


        rvItemSearchBill.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvItemSearchBill.setAdapter(productSearchAdapter);

        edtProductSearchDialogBill.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        calculateGstPrice();

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
        materialAlertDialogBuilder.setView(customDialogViewBill)
                .setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
        alertDialogBill = materialAlertDialogBuilder.create();
    }

    public void onDeleteAll() {
        if (list_item != null && list_item.size() > 0) {
            try {
                if (imgDirectory.exists()) {
                    File imgDelete = new File(imgDirectory.toString());

                    if (imgDelete.isDirectory()) {
                        listFile = imgDelete.listFiles();

                        for (int i = 0; i < listFile.length; i++) {
                            if (listFile[i].exists())
                                listFile[i].delete();
                        }
                    }
                }
            } catch (Exception ep) {
                ep.printStackTrace();
                generateLog();
            }


            dbHelper.deleteAll();
            list_item.clear();
            rec_item.setVisibility(View.GONE);
            calculateMaking();
            calculatePrice();
            calculateTotal();
            calculateGstPrice();
            rec_item.setAdapter(new ItemAdapter());
            edt_customer_name.setText("");
            dbHelper.close();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        calculatePrice();
        calculateMaking();
        calculateTotal();
        calculateGstPrice();

        if (!edt_tag_no.getText().toString().isEmpty()) {
            System.out.println("edt tag " + edt_tag_no.getText());
        }
    }

    private void calculateTotal() {
        total = 0.0;
        if (!edt_item_price.getText().toString().isEmpty())
            total += Double.valueOf(edt_item_price.getText().toString().trim());
        if (!edt_making_total.getText().toString().isEmpty())
            total += Double.valueOf(edt_making_total.getText().toString().trim());
        if (!edt_charge1.getText().toString().isEmpty())
            total += Double.valueOf(edt_charge1.getText().toString().trim());
        if (!edt_charge2.getText().toString().isEmpty())
            total += Double.valueOf(edt_charge2.getText().toString().trim());

        txt_total.setText("Total : " + String.format("%.2f", total));
    }

    private void calculatePrice() {

        Double grossWeight = 0.0;
        try {
            if (!edt_gross_weight.getText().toString().isEmpty()) {
                grossWeight = getAmountFromEditText(edt_gross_weight);
            }
        } catch (Exception e) {
            e.printStackTrace();
            generateLog();
        }

        Double lessWeight = 0.0;
        try {
            if (!edt_less_weight.getText().toString().isEmpty()) {
                lessWeight = getAmountFromEditText(edt_less_weight);
            }
        } catch (Exception e) {
            e.printStackTrace();
            generateLog();
        }
        Double grasWeight = 0.0;
        try {
            if (!edt_ghat_weight.getText().toString().isEmpty()) {
                grasWeight = getAmountFromEditText(edt_ghat_weight);
            }
        } catch (Exception e) {
            e.printStackTrace();
            generateLog();
        }

        edt_net_weight.setText(String.format("%.3f", (subtractUsingBigDecimalOperation(grossWeight, lessWeight))));
        if (!edt_22_carat.getText().toString().isEmpty() && !edt_net_weight.getText().toString().isEmpty() && spinner_carat.getSelectedItemPosition() == 0 && RateOn.equals("Net")) {
            Double amount = (Double.valueOf(edt_22_carat.getText().toString().trim()) * (Double.valueOf(edt_net_weight.getText().toString().trim()) + grasWeight)) / 10;
            edt_item_price.setText(String.format("%.0f", amount));
            edt_item_price.setError(null);
        }else if(RateOn.equals("Pcs") && !edt_22_carat.getText().toString().isEmpty() && !edt_piece.getText().toString().isEmpty()){
            Double amount = (Double.valueOf(edt_22_carat.getText().toString().trim()) * (Double.valueOf(edt_piece.getText().toString().trim()) )) ;
            edt_item_price.setText(String.format("%.0f", amount));
            edt_item_price.setError(null);
        }
        else {
            edt_item_price.setText("");
            edt_item_price.setError(null);
        }
    }

    private static Double subtractUsingBigDecimalOperation(Double a, Double b) {
        return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue();
    }

    private void calculateMaking() {
        if (!edt_make_charge.getText().toString().isEmpty() && !edt_item_price.getText().toString().isEmpty() && spinner_making.getSelectedItemPosition() == 0) {
            Double total = Double.valueOf(edt_item_price.getText().toString().trim());
            Double per = Double.valueOf(edt_make_charge.getText().toString().trim());
            Double making_charge = (total * per) / 100;
            if (LabourOn.equals("Fine")) {
                double tmpTouch = Touch / 100;
                double tmptotal = total / tmpTouch;
                making_charge = (tmptotal * per) / 100;
            }

            edt_making_total.setText(String.format("%.0f", making_charge));
        } else if (!edt_make_charge.getText().toString().isEmpty() && !edt_item_price.getText().toString().isEmpty() && spinner_making.getSelectedItemPosition() == 1 && !edt_piece.getText().toString().isEmpty()) {
            Double charge = Double.valueOf(edt_make_charge.getText().toString().trim());
            Double pieces = Double.parseDouble(edt_piece.getText().toString());
            edt_making_total.setText(String.format("%.0f", charge * pieces));
        } else if (!edt_make_charge.getText().toString().isEmpty() && !edt_net_weight.getText().toString().isEmpty() && spinner_making.getSelectedItemPosition() == 2) {
            Double gram = Double.valueOf(edt_net_weight.getText().toString().trim());
            if (!edt_ghat_weight.getText().toString().isEmpty()) {
                gram = gram + Double.valueOf(edt_ghat_weight.getText().toString());
            }
            Double per = Double.valueOf(edt_make_charge.getText().toString().trim());
            if (gram > 0) {
                if ("Rounding Up".equals(LabourValueCal)) {
                    gram = Math.ceil(gram);
                } else if ("Rounding Down".equals(LabourValueCal)) {
                    gram = Math.floor(gram);
                } else if ("Nearest Ten".equals(LabourValueCal)) {
                    gram = Double.parseDouble(round(gram) + "");
                }

            }
            Double making_charge = (gram * per);
            edt_making_total.setText(String.format("%.0f", making_charge));
        } else {
            edt_making_total.setText("");
        }
    }

    @OnClick(R.id.edt_date)
    public void onDateClick() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edt_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }
    };

    public void onAddClick() {
        SecurePreferences.savePreferences(getActivity(), "22Carat", edt_22_carat.getText().toString().trim());
        if (edt_date.getText().toString().isEmpty())
            edt_date.setError("?");
        else if (edt_item_name.getText().toString().isEmpty())
            edt_item_name.setError("?");
        else if (edt_net_weight.getText().toString().isEmpty())
            edt_net_weight.setError("?");
        else if (total == 0)
            edt_item_price.setError("?");
        else if (edt_piece.getText().toString().isEmpty()) {
            edt_piece.setError("Please enter pieces");
        } else {
            ItemModel itemModel = new ItemModel();
            itemModel.cust_name = edt_cust_name.getText().toString().trim();
            itemModel.bill_date = edt_date.getText().toString().trim();

            if (edt_22_carat.getText().toString().isEmpty())
                itemModel.carat_22 = "0";
            else
                itemModel.carat_22 = edt_22_carat.getText().toString().trim();

            itemModel.carat_18 = "0";
            itemModel.silver = "0";
            Double grossWeight = 0.0;
            if (!edt_gross_weight.getText().toString().isEmpty())
                grossWeight = getAmountFromEditText(edt_gross_weight);

            Double lessWeight = 0.0;
            if (!edt_less_weight.getText().toString().isEmpty())
                lessWeight = getAmountFromEditText(edt_less_weight);

            Double grasWeight = 0.0;
            if (!edt_ghat_weight.getText().toString().isEmpty())
                grasWeight = getAmountFromEditText(edt_ghat_weight);

            itemModel.item_name = edt_item_name.getText().toString().trim();
            itemModel.net_weight = edt_net_weight.getText().toString().trim();
            itemModel.gross_weight = String.format("%.3f", grossWeight);
            itemModel.less_weight = String.format("%.3f", lessWeight);
            itemModel.ghat_weight = String.format("%.3f", grasWeight);
            itemModel.selected_carat = carat_list.get(spinner_carat.getSelectedItemPosition());
            itemModel.item_price = edt_item_price.getText().toString().trim();
            itemModel.itemPcs = (int) Double.parseDouble(edt_piece.getText().toString().trim());
            itemModel.making_charge = edt_make_charge.getText().toString().trim();
            itemModel.selected_varient = making_list.get(spinner_making.getSelectedItemPosition());
            itemModel.making_total = edt_making_total.getText().toString().trim();
            itemModel.other1 = edt_charge1.getText().toString().trim();
            itemModel.other2 = edt_charge2.getText().toString().trim();
            itemModel.tag_no = edt_tag_no.getText().toString().trim();
            itemModel.total = String.format("%.2f", total);
            itemModel.img_path = imgSavedPath;
            itemModel.Touch = Touch;

            if (!is_edit)
                itemModel.img = img;

            if (is_edit)
                dbHelper.updateRecord(itemModel, item_id);
            else
                dbHelper.insertRecord(itemModel);

            edt_item_name.setText("");
            edt_net_weight.setText("");
            edt_gross_weight.setText("");
            edt_less_weight.setText("");
            edt_ghat_weight.setText("");
            edt_make_charge.setText("");
            edt_charge1.setText("");
            edt_charge2.setText("");
            edt_tag_no.setText("");
            edt_piece.setText("");
            btn_add.setText("Add");
            is_edit = false;
            item_id = 0;
            img = null;
            list_item = dbHelper.getItems();

            if (!list_item.isEmpty()) {
                rec_item.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rec_item.setNestedScrollingEnabled(false);
                rec_item.setAdapter(new ItemAdapter());
                rec_item.setVisibility(View.VISIBLE);
            } else {
                rec_item.setVisibility(View.GONE);
            }

            calculateGstPrice();
        }
    }

    private void calculateGstPrice() {
        if (!edt_gst.getText().toString().isEmpty()) {

            SecurePreferences.savePreferences(getActivity(), "Gst", edt_gst.getText().toString().trim());
            Double total = 0.0;
            Double netWeight = 0.0;
            for (int i = 0; i < list_item.size(); i++) {
                total += Double.valueOf(list_item.get(i).total);
                netWeight += Double.valueOf(list_item.get(i).net_weight);
            }

            sub_total = total;

            Double gst = (Double.valueOf(edt_gst.getText().toString().trim()) * total) / 100;
            Double final_total = total + gst;

            finaltotal = final_total;
            finalNetWeight = netWeight;
            txt_gst.setText("GST" + " : " + String.format("%.2f", gst));
            txt_final_total.setText("Total : " + String.format("%.2f", final_total));
            txt_final_net_total.setText("Net Weight : " + String.format("%.3f", finalNetWeight));
        }
    }

    @Override
    public void onItemCLick(int position, String item) {
        edt_item_name.setText(item);
        alertDialogBill.dismiss();
    }

    public class CustomAdapter extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapter() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return carat_list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.simple_item_spinner, null);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(Html.fromHtml(carat_list.get(i)));
            return view;
        }
    }

    public class MakingAdapter extends BaseAdapter {
        LayoutInflater inflate;

        public MakingAdapter() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return making_list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.simple_item_spinner, null);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);

            txt_name.setText(Html.fromHtml(making_list.get(i)));
            return view;
        }
    }

    public void onShareClick() {

        if (list_item != null && list_item.size() > 0) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            PriceDetailFrag priceDetailFrag = new PriceDetailFrag(edt_customer_name.getText().toString().trim(), edt_cust_name.getText().toString().trim(), edt_date.getText().toString().trim(), sub_total, txt_gst.getText().toString().trim(), txt_final_total.getText().toString().trim(), finaltotal, list_item);
            transaction.add(R.id.frame_full, priceDetailFrag, "PriceDetailFrag");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Toast.makeText(getActivity(), "Please add items first", Toast.LENGTH_SHORT).show();
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
        @NonNull
        @Override
        public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_design, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
            ItemModel itemModel = list_item.get(position);
            holder.txt_item_name.setText(itemModel.item_name);
            holder.txt_gross_weight.setText(itemModel.gross_weight);
            holder.txt_less_weight.setText(itemModel.less_weight);
            holder.txt_net_weight.setText(itemModel.net_weight);
            holder.txt_ghat_weight.setText(itemModel.ghat_weight);
            holder.txtItemPcs.setText(String.valueOf(itemModel.itemPcs));

            if (itemModel.selected_carat.contains("18"))
                holder.txt_rate.setText(itemModel.carat_18);
            else if (itemModel.selected_carat.contains("22"))
                holder.txt_rate.setText(itemModel.carat_22);
            else
                holder.txt_rate.setText(itemModel.silver);

            holder.txt_price.setText(itemModel.item_price);
            holder.txt_making_charge.setText(itemModel.making_charge);
            holder.txt_total_making.setText(itemModel.making_total);
            holder.txt_other_1.setText(itemModel.other1);
            holder.txt_other_2.setText(itemModel.other2);
            holder.chipTagNo.setText(itemModel.tag_no);
            holder.chipTagNo.setTypeface(null, Typeface.BOLD);
            holder.txt_total.setText(itemModel.total);

            if (itemModel.isSelected == 1) {
                cartProducts.add(itemModel);
            }

            if (itemModel.img_path != null) {
                holder.altImg.setVisibility(View.GONE);
                Uri imageUri = Uri.parse(itemModel.img_path);
                holder.itemImg.setImageURI(imageUri);
            } else {
                holder.altImg.setVisibility(View.VISIBLE);
            }

//            if (holder.getPosition() + 1 < list_item.size())
//                holder.divider.setVisibility(View.VISIBLE);
//            else
//                holder.divider.setVisibility(View.GONE);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemModel.img_path != null) {
                        File imgDelete = new File(itemModel.img_path);
                        if (imgDelete.exists()) {
                            imgDelete.delete();
                        }
                    }

                    dbHelper.deleteItem(itemModel.item_id);
                    list_item.remove(position);

                    if (!list_item.isEmpty())
                        rec_item.setVisibility(View.VISIBLE);
                    else
                        rec_item.setVisibility(View.GONE);

                    isAnySelected();
                    calculateGstPrice();
                    notifyDataSetChanged();

                }
            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDataForEdit(itemModel);
                    nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });

            holder.btnPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



//                    String CN=edt_customer_name.getText().toString();
//                    String IN=holder.txt_item_name.getText().toString();
//
//                    String tn="Tag No:"+holder.chipTagNo.getText().toString();
//                    String CustomerName="Customer Name:"+edt_customer_name.getText().toString();
//                    String ItemName="Item Name:"+holder.txt_item_name.getText().toString();
//                    String GrossWeight="Gross Weight:"+holder.txt_gross_weight.getText().toString();
//                    String LessWeight="Less Weight:"+holder.txt_less_weight.getText().toString();
//                    String NetWeight="Net Weight:"+holder.txt_net_weight.getText().toString();
//                    String GhatWeight="Ghat Weight:"+holder.txt_ghat_weight.getText().toString();
//                    String Pieces="Pieces:"+holder.txtItemPcs.getText().toString();
//                    String Rate="Rate:"+edt_22_carat.getText().toString();
//                    String Amount="Amount:"+holder.txt_price.getText().toString();
//                    String LRate="L Rate:"+holder.txt_making_charge.getText().toString();
//                    String LAmount="L Amount:"+holder.txt_total_making.getText().toString();
//                    String OCharge1="Other Charge 1:"+holder.txt_other_1.getText().toString();
//                    String OCharge2="Other Charge 2:"+holder.txt_other_2.getText().toString();
//                    String Total="Total:"+holder.txt_total.getText().toString();



                    if(isAnySelected==true)
                    {
                        list_item = dbHelper.getItems();
                        command = "QUOTATION";

                        for (ItemModel itemModel : list_item) {
                            if (itemModel.isSelected == 1) {
                                //isAnySelected = true;
                                //Toast.makeText(getContext(), itemModel.tag_no,Toast.LENGTH_LONG).show();
                               // command+=edt_cust_name.getText().toString()+":"+edt_customer_name.getText().toString()+":"+itemModel.tag_no;
                                command+=":"+itemModel.tag_no;
                                //break;
                           // } else {
                             //  isAnySelected = false;
                            }
                        }
                        Toast.makeText(getContext(), command,Toast.LENGTH_LONG).show();
                        if (haveNetworkConnection()) {
                            if (socketTask != null) {
                                new Thread(() -> {
                                    // Run whatever background code you want here.
                                    if (socketTask.getTcpClient() != null) {
                                        if (socketTask.getTcpClient().getSocket() != null) {
                                            if (socketTask.getTcpClient().getSocket().isConnected()) {
                                                socketTask.getTcpClient().sendMessageGetPDF(command);

//                                                new Handler().postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        openPDFforQuatation(socketTask.getTcpClient().fileName);
//                                                    }
//                                                },5000);
//                                                openPDFforQuatation();

                                                new Thread(()->{
                                                    try {
                                                        Thread.sleep(10000);
                                                    } catch (InterruptedException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                });
                                               // openPDFforQuatation(socketTask.getTcpClient().fileName);
                                                openPDFforQuatation();



                                            } else {
                                                setStatus(SocketStatus.DISCONNECTED);
                                            }
                                        } else {
                                            setStatus(SocketStatus.DISCONNECTED);
                                        }
                                    } else {
                                        setStatus(SocketStatus.DISCONNECTED);
                                    }
                                }).start();
                            }
                        } else
                            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
                    }


                   /* if(holder.chipTagNo.isChecked())
                    {
                        CreatePDF(CN,IN, tn, CustomerName, ItemName, GrossWeight, LessWeight, NetWeight, GhatWeight, Pieces, Rate, Amount, LRate, LAmount, OCharge1, OCharge2, Total);
                    }*/
                }

            });


            if (itemModel.isSelected == 1) {
                holder.chipTagNo.setChecked(true);
//                holder.btnDelete.setIconTintResource(R.color.white);
//                holder.btnDelete.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
//                holder.btnPrint.setIconTintResource(R.color.white);
//                holder.btnPrint.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
//                holder.btnEdit.setIconTintResource(R.color.white);
//                holder.btnEdit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                //sendDataToServer();
                //openPDF();



                holder.layoutCardBody.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_green_40_card, null));
            } else {
                holder.chipTagNo.setChecked(false);
//                holder.btnDelete.setIconTintResource(R.color.white);
//                holder.btnDelete.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green600, null));
//                holder.btnPrint.setIconTintResource(R.color.white);
//                holder.btnPrint.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green600, null));
//                holder.btnEdit.setIconTintResource(R.color.white);
//                holder.btnEdit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green600, null));
                holder.layoutCardBody.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_gray_card, null));
            }

            holder.chipTagNo.setOnClickListener(v -> {
                rec_item.getAdapter().notifyDataSetChanged();
                if (itemModel.isSelected == 1) {
                    itemModel.isSelected = 0;
                    holder.chipTagNo.setChecked(false);
//                    holder.btnDelete.setIconTintResource(R.color.green);
//                    holder.btnDelete.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green100, null));
//                    holder.btnPrint.setIconTintResource(R.color.green);
//                    holder.btnPrint.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green100, null));
//                    holder.btnEdit.setIconTintResource(R.color.green);
//                    holder.btnEdit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green100, null));
//                    holder.chipTagNo.setChipBackgroundColorResource(R.color.white);
//                    holder.chipTagNo.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                    holder.layoutCardBody.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_gray_card, null));
                } else {
                    itemModel.isSelected = 1;
                    holder.chipTagNo.setChecked(true);
//                    holder.btnDelete.setIconTintResource(R.color.white);
//                    holder.btnDelete.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
//                    holder.btnPrint.setIconTintResource(R.color.white);
//                    holder.btnPrint.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
//                    holder.btnEdit.setIconTintResource(R.color.white);
//                    holder.btnEdit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
//                    holder.chipTagNo.setChipBackgroundColorResource(R.color.green);
//                    holder.chipTagNo.setCheckedIconTintResource(R.color.white);
//                    holder.chipTagNo.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));

                    holder.layoutCardBody.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_green_40_card, null));
                }
                dbHelper.updateRecord(itemModel, itemModel.item_id);
                isAnySelected();
            });

//            if (itemModel.img != null) {
//                if (!itemModel.img.isEmpty()) {
//                    Picasso.get()
//                            .load(itemModel.img)
//                            .placeholder(R.drawable.ic_plceholder)
//                            .into(holder.img);
//                } else {
//                    holder.img.setVisibility(View.GONE);
//                }
//            } else {
//                holder.img.setVisibility(View.GONE);
//            }
        }

        @Override
        public int getItemCount() {
            return list_item.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_item_name)
            TextView txt_item_name;
            @BindView(R.id.txt_gross_weight)
            TextView txt_gross_weight;
            @BindView(R.id.txt_less_weight)
            TextView txt_less_weight;
            @BindView(R.id.txt_net_weight)
            TextView txt_net_weight;
            @BindView(R.id.txt_ghat_weight)
            TextView txt_ghat_weight;
            @BindView(R.id.txtItemPcs)
            TextView txtItemPcs;
            @BindView(R.id.txt_rate)
            TextView txt_rate;
            @BindView(R.id.chipTagNo)
            Chip chipTagNo;
            @BindView(R.id.txt_price)
            TextView txt_price;
            @BindView(R.id.txt_making_charge)
            TextView txt_making_charge;
            @BindView(R.id.txt_total_making)
            TextView txt_total_making;
            @BindView(R.id.txt_other_1)
            TextView txt_other_1;
            @BindView(R.id.txt_other_2)
            TextView txt_other_2;
            @BindView(R.id.txt_total)
            TextView txt_total;
            @BindView(R.id.btnPrint)
            MaterialButton btnPrint;
            @BindView(R.id.btnDelete)
            MaterialButton btnDelete;
            @BindView(R.id.btnEdit)
            MaterialButton btnEdit;
            @BindView(R.id.itemImg)
            ImageView itemImg;
            @BindView(R.id.altImg)
            TextView altImg;
            @BindView(R.id.layoutCardBody)
            LinearLayout layoutCardBody;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void setDataForEdit(ItemModel itemModel) {

        edt_22_carat.setText(itemModel.carat_22);
        edt_tag_no.setText(itemModel.tag_no);
        edt_item_name.setText(itemModel.item_name);
        edt_net_weight.setText(itemModel.net_weight);
        edt_gross_weight.setText(itemModel.gross_weight);
        edt_less_weight.setText(itemModel.less_weight);
        edt_ghat_weight.setText(itemModel.ghat_weight);
        edt_piece.setText(String.valueOf(itemModel.itemPcs));
        Touch = itemModel.Touch;

        if (itemModel.selected_carat.contains("22"))
            spinner_carat.setSelection(0);

        if (itemModel.making_charge != null && !itemModel.making_charge.isEmpty()) {
            edt_make_charge.setText(itemModel.making_charge);

            if (itemModel.selected_varient.equalsIgnoreCase("Per"))
                spinner_making.setSelection(0);
            else if (itemModel.selected_varient.equalsIgnoreCase("Pcs"))
                spinner_making.setSelection(1);
            else
                spinner_making.setSelection(2);
        }

        if (itemModel.other1 != null && !itemModel.other1.isEmpty())
            edt_charge1.setText(itemModel.other1);

        if (itemModel.other2 != null && !itemModel.other2.isEmpty())
            edt_charge2.setText(itemModel.other2);

        is_edit = true;
        item_id = itemModel.item_id;
        btn_add.setText("Update");
    }

    private int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
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

    private Double getAmountFromEditText(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return 0.0;
        else return Double.parseDouble(editText.getText().toString().trim());
    }

    @Override
    public void onStart() {
        super.onStart();

        if (allowDisconnect)
            connectSocket();

        allowDisconnect = true;
    }

    @Override
    public void onStop() {
        if (allowDisconnect) {
            try {
                if (socketTask != null) {
                    if (socketTask.getTcpClient() != null)
                        socketTask.getTcpClient().stopClient();
                    socketTask.cancel(true);
                    socketTask = null;
                }
                if (mTcpClient != null)
                    mTcpClient.stopClient();
            } catch (Exception e) {
                e.printStackTrace();
                generateLog();
            }
        }
        generateLog();
        super.onStop();
    }

    void isAnySelected() {
        isAnySelected = false;

        list_item = dbHelper.getItems();

        for (ItemModel itemModel : list_item) {
            if (itemModel.isSelected == 1) {
                isAnySelected = true;
                break;
            } else {
                isAnySelected = false;
            }
        }

        if (isAnySelected)
            fabCart.setVisibility(View.VISIBLE);
        else
            fabCart.setVisibility(View.GONE);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String tagNo) {
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        imgDirectory = cw.getDir("goldman", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(imgDirectory, tagNo + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            generateLog();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                generateLog();
            }
        }
        return imgDirectory.getAbsolutePath() + "/" + tagNo + ".jpg";
    }

    void setStatus(SocketStatus status) {
        try {
            snackbar = Snackbar.make(coLayout, "", Snackbar.LENGTH_INDEFINITE);

            View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
            Button btnActSnackbar = customSnackView.findViewById(R.id.btnActSnackbar);
            TextView tvMsgSnackbar = customSnackView.findViewById(R.id.tvMsgSnackbar);
            CardView cardView = customSnackView.findViewById(R.id.cvBgSnackbar);

            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            snackbarLayout.setPadding(0, 0, 0, 0);
            snackbarLayout.addView(customSnackView, 0);

            btnActSnackbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btnActSnackbar.getText().toString().equals("RETRY")) {
                        connectSocket();
                    } else if (btnActSnackbar.getText().toString().equals("SET")) {
                        Intent intent = new Intent(getContext(), SettingActivity.class);
                        startActivity(intent);
                    }
                }
            });

            switch (status) {
                case ERROR:
                    generateLog();

                    if (SecurePreferences.getStringPreference(getActivity(), "ip").isEmpty() || SecurePreferences.getIntegerPreference(getActivity(), "port") == 0) {
                        btnActSnackbar.setText("SET");
                        tvMsgSnackbar.setText("Please set ip address and port first");
                    }/* else if (!SecurePreferences.getStringPreference(getActivity(), "savedNetwork").isEmpty()) {
                    WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = wifiManager.getConnectionInfo();
                    String ssid = info.getSSID();

                    if (!ssid.replace('"', ' ').trim().equals(SecurePreferences.getStringPreference(getActivity(), "savedNetwork"))) {
                        tvMsgSnackbar.setText("Connected to different network. Please check your wifi connection");
                        btnActSnackbar.setText("RETRY");
                    }
                    else{
                        btnActSnackbar.setText("RETRY");
                        tvMsgSnackbar.setText("Error");
                    }
                }*/ else {
                        btnActSnackbar.setText("RETRY");
                        tvMsgSnackbar.setText("Error - Offline");
                    }

                    cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_negative));
                    snackbar.show();
                    generateLog();
                    break;

                case CONNECTED:
                    btnActSnackbar.setEnabled(false);
                    tvMsgSnackbar.setText("Online");
                    snackbar.setDuration(Snackbar.LENGTH_SHORT);
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_positive));
                    snackbar.show();
                    getBillType();
                    getItemNames();
                    break;

                case CONNECTING:
                    btnActSnackbar.setEnabled(false);
                    tvMsgSnackbar.setText("Connecting");
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_warning));
                    snackbar.show();
                    generateLog();
                    break;

                case DISCONNECTED:
                    btnActSnackbar.setText("RETRY");
                    tvMsgSnackbar.setText("Please refresh connection");
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_negative));
                    snackbar.show();
                    generateLog();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateLog() {
        try {
            Uri baseDocumentTreeUri = Uri.parse(SecurePreferences.getStringPreference(getActivity(), "filestorageuri"));
            DocumentFile directory = DocumentFile.fromTreeUri(getActivity(), baseDocumentTreeUri);
            DocumentFile file;

            if ((file = directory.findFile("GoldmanLog.txt")) == null) {
                file = directory.createFile("text/plain", "GoldmanLog");
            }

            ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(file.getUri(), "w");
            FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());

            int pid = android.os.Process.myPid();

//            String cmd = "logcat -b all -v brief *:W";
//            Process process = Runtime.getRuntime().exec(cmd);

            String command = "logcat -d -v brief *:W";
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String currentLine = null;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine != null && currentLine.contains(String.valueOf(pid))) {
                    result.append(currentLine);
                    result.append("\n");
                }
            }

            fos.write(result.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public  void CreatePDF(String cn,String in,String tagno,String cname,String iname,String grwit,String lwit,String nwit,String gawit,String pieces,String rate,String amt,String lrt,String lamt,String oc1,String oc2,String ttl)
//    {
//        String file1=cn+"--"+in +".pdf";
//        Document document=new Document();
//        try
//        {
//            //String path= Environment.getExternalStorageDirectory()+"/Download";
//            String path= String.valueOf(getActivity().getFilesDir());
//            File dir=new File(path);
//            if(!dir.exists())
//            {
//                dir.mkdirs();
//                dir.setWritable(true);
//            }
//            File file=new File(dir,file1);
//
//            if(file.exists())
//                file.setWritable(true);
//            if(!file.exists()) {
//                file.createNewFile();
//            }
//            FileOutputStream fout=new FileOutputStream(file);
//
//
//
//            PdfWriter.getInstance(document,fout);
//
//            document.open();
//
//            Paragraph p0=new Paragraph("Customer Details");
//            Paragraph p1=new Paragraph(tagno);
//            Paragraph p2=new Paragraph(cname);
//            Paragraph p3=new Paragraph(iname);
//            Paragraph p4=new Paragraph(grwit);
//            Paragraph p5=new Paragraph(lwit);
//            Paragraph p6=new Paragraph(nwit);
//            Paragraph p7=new Paragraph(gawit);
//            Paragraph p8=new Paragraph(pieces);
//            Paragraph p9=new Paragraph(rate);
//            Paragraph p10=new Paragraph(amt);
//            Paragraph p11=new Paragraph(lrt);
//            Paragraph p12=new Paragraph(lamt);
//            Paragraph p13=new Paragraph(oc1);
//            Paragraph p14=new Paragraph(oc2);
//            Paragraph p15=new Paragraph(ttl);
//
//            Font paraFont=new Font(Font.FontFamily.COURIER);
//            p0.setAlignment(Paragraph.TITLE);
//
//            p1.setAlignment(Paragraph.ALIGN_LEFT);
//            p1.setFont(paraFont);
//
//            p2.setAlignment(Paragraph.ALIGN_LEFT);
//            p2.setFont(paraFont);
//
//            p3.setAlignment(Paragraph.ALIGN_LEFT);
//            p3.setFont(paraFont);
//
//            p4.setAlignment(Paragraph.ALIGN_LEFT);
//            p4.setFont(paraFont);
//
//            p5.setAlignment(Paragraph.ALIGN_LEFT);
//            p5.setFont(paraFont);
//
//            p6.setAlignment(Paragraph.ALIGN_LEFT);
//            p6.setFont(paraFont);
//
//            p7.setAlignment(Paragraph.ALIGN_LEFT);
//            p7.setFont(paraFont);
//
//            p8.setAlignment(Paragraph.ALIGN_LEFT);
//            p8.setFont(paraFont);
//
//            p9.setAlignment(Paragraph.ALIGN_LEFT);
//            p9.setFont(paraFont);
//
//            p10.setAlignment(Paragraph.ALIGN_LEFT);
//            p10.setFont(paraFont);
//
//            p11.setAlignment(Paragraph.ALIGN_LEFT);
//            p11.setFont(paraFont);
//
//            p12.setAlignment(Paragraph.ALIGN_LEFT);
//            p12.setFont(paraFont);
//
//            p13.setAlignment(Paragraph.ALIGN_LEFT);
//            p13.setFont(paraFont);
//
//            p14.setAlignment(Paragraph.ALIGN_LEFT);
//            p14.setFont(paraFont);
//
//            p15.setAlignment(Paragraph.ALIGN_LEFT);
//            p15.setFont(paraFont);
//
//            document.add(p0);
//            document.add(p1);
//            document.add(p2);
//            document.add(p3);
//            document.add(p4);
//            document.add(p5);
//            document.add(p6);
//            document.add(p7);
//            document.add(p8);
//            document.add(p9);
//            document.add(p10);
//            document.add(p11);
//            document.add(p12);
//            document.add(p13);
//            document.add(p14);
//            document.add(p15);
//
//
//
//
//
//
//
//
//        } catch (DocumentException e) {
//            throw new RuntimeException(e);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        finally
//        {
//            document.close();
//        }
//        openPDFforCotation(file1);
//    }


    void openPDFforQuatation(String filename) {
        File pdfOpenFile = getActivity().getFilesDir();
        //File pdfFile=new File(Environment.getExternalStorageDirectory()+ "/Download" );
        File fileOpen = new File(pdfOpenFile, filename);

        Uri path = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", fileOpen);
       // Uri path = Uri.fromFile(fileOpen);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
//            getActivity().startActivity(Intent.createChooser(pdfOpenintent, "Open File Using..."));
            getActivity().startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }



    void openPDFforQuatation() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh:mm:aa_");
        Date now=new Date();
        String fileName = formatter.format(now) +"Invoice.pdf";


        File pdfOpenFile = getActivity().getFilesDir();
        //File pdfFile=new File(Environment.getExternalStorageDirectory()+ "/Download" );
        File fileOpen = new File(pdfOpenFile,fileName);

        Uri path = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", fileOpen);
        // Uri path = Uri.fromFile(fileOpen);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
//            getActivity().startActivity(Intent.createChooser(pdfOpenintent, "Open File Using..."));
            getActivity().startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }



}