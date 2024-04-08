package com.balaji.calculator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.balaji.calculator.adapter.CartAdapter;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.BillTypeModel;
import com.balaji.calculator.model.ItemModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements OnRVItemClickListener<ItemModel> {

    ImageView ivBack;
    RecyclerView recyclerViewCart;
    BillFragment billFragment;
    DbHelper dbHelper;
    List<ItemModel> items;
    public List<ItemModel> selectedItems = new ArrayList<>();
    public CartAdapter cartAdapter;
    ArrayAdapter billTypeArrayAdapter;
    MaterialAutoCompleteTextView autoCompleteBillType;
    LinearLayout cartBottomSheetLayout;
    BottomSheetBehavior cartBottomSheetBehavior;
    double total = 0, discount = 0, taxes = 0, gst = 0;
    int discountPercentage = 0;
    MaterialTextView cartBottomSheetGrandTotal, cartBottomSheetTotal, cartBottomSheetTaxes;
    EditText edtCartBottomSheetDiscountPercentage, edtCartBottomSheetDiscountAmount;
    MaterialButton cartBottomSheetNextButton;
    CustomerDetailsFragment fragmentCustomerDetails;
    BillTypeModel modelBillType;
    boolean taxType = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    public CartFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        if (haveNetworkConnection()) {
//            if (homeFragment.socketTask != null) {
//                Toast.makeText(getActivity(), "Here", Toast.LENGTH_SHORT).show();
//                new Thread(() -> {
//                    // Run whatever background code you want here.
//                    if (homeFragment.socketTask.getTcpClient() != null) {
//                        if (homeFragment.socketTask.getTcpClient().getSocket() != null) {
//                            if (homeFragment.socketTask.getTcpClient().getSocket().isConnected()) {
//                                homeFragment.socketTask.getTcpClient().sendMessage("BillType");
//                            } else {
//                            }
//                        } else {
//                        }
//                    } else {
//                    }
//                }).start();
//            }
//        } else
//            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
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
                System.out.println("onDestroy.");
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

        calculateDiscountPercentage();
        calculateDiscountAmount(taxType);

        billTypeArrayAdapter = new ArrayAdapter(getContext(), R.layout.drop_down_item_bill_type, billFragment.listBillType.toArray());
        autoCompleteBillType.setAdapter(billTypeArrayAdapter);
        autoCompleteBillType.setDropDownBackgroundResource(R.drawable.bg_gray_card);
//        autoCompleteBillType.setText(autoCompleteBillType.getAdapter().getItem(0).toString(), false);
        autoCompleteBillType.showDropDown();
        autoCompleteBillType.setListSelection(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        ivBack = view.findViewById(R.id.ivBack);
        autoCompleteBillType = view.findViewById(R.id.autoCompleteBillType);
        cartBottomSheetLayout = view.findViewById(R.id.cartBottomSheetLayout);
        cartBottomSheetGrandTotal = view.findViewById(R.id.cartBottomSheetGrandTotal);
        cartBottomSheetTotal = view.findViewById(R.id.cartBottomSheetTotal);
        cartBottomSheetTaxes = view.findViewById(R.id.cartBottomSheetTaxes);
        edtCartBottomSheetDiscountPercentage = view.findViewById(R.id.edtCartBottomSheetDiscountPercentage);
        edtCartBottomSheetDiscountAmount = view.findViewById(R.id.edtCartBottomSheetDiscountAmount);
        cartBottomSheetNextButton = view.findViewById(R.id.cartBottomSheetNextButton);
        dbHelper = new DbHelper(getActivity());
        initView();

        cartAdapter = new CartAdapter(getActivity(), selectedItems, this, billFragment.edt_gst.getText().toString());

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setAdapter(cartAdapter);

        //Cart Bottom Sheet
        cartBottomSheetBehavior = BottomSheetBehavior.from(cartBottomSheetLayout);

        cartBottomSheetNextButton.setOnClickListener(v -> {
            if (!autoCompleteBillType.getText().toString().equals("")) {
                billFragment.allowDisconnect = false;

                autoCompleteBillType.setError(null);

                Bundle args = new Bundle();
                args.putDouble("total", total);
                args.putDouble("discount", discount);
                args.putDouble("taxes", taxes);
                args.putInt("billId", modelBillType.getBillId());

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentCustomerDetails = new CustomerDetailsFragment(billFragment);
                fragmentCustomerDetails.setArguments(args);

                transaction.replace(R.id.frame_full, fragmentCustomerDetails, "CustomerDetailsFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            } else
                autoCompleteBillType.setError("Please select bill type");
        });

        autoCompleteBillType.setOnItemClickListener((parent, view1, position, id) -> {
            modelBillType = billFragment.listBillTypeModel.get(position);

            calculateAmount(modelBillType.getTaxType());
        });
        return view;
    }

    @Override
    public void onItemCLick(int position, ItemModel item) {
        item.isSelected = 0;
        dbHelper.updateRecord(item, item.item_id);
        selectedItems.remove(position);
        recyclerViewCart.getAdapter().notifyItemRemoved(position);
        calculateAmount(taxType);
    }

    private void initView() {
        getCustomerNames();

        edtCartBottomSheetDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateDiscountPercentage();
            }
        });

        edtCartBottomSheetDiscountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateDiscountAmount(modelBillType.getTaxType());
            }
        });

        calculateAmount(taxType);
    }

    void calculateDiscountPercentage() {
        discountPercentage = edtCartBottomSheetDiscountPercentage.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtCartBottomSheetDiscountPercentage.getText().toString());

        double tmptotal = 0;
        for (ItemModel itemModel : items) {
            if (itemModel.isSelected == 1) {

                tmptotal += Double.parseDouble(itemModel.total);
            }
        }
        discount = (tmptotal * discountPercentage) / 100;

        total = tmptotal;
        tmptotal -= discount;
        taxes = (tmptotal * gst) / 100;
        cartBottomSheetGrandTotal.setText(String.format(Locale.getDefault(), "%.2f", (tmptotal + taxes)));
        cartBottomSheetTotal.setText(String.format(Locale.getDefault(), "%.2f", total));
        cartBottomSheetTaxes.setText(String.format(Locale.getDefault(), "%.2f", taxes));
    }

    void calculateDiscountAmount(boolean taxType) {
        discount = getAmountFromEditText(edtCartBottomSheetDiscountAmount);
        double tmptotal = 0;
        for (ItemModel itemModel : items) {
            if (itemModel.isSelected == 1) {
                tmptotal += Double.parseDouble(itemModel.total);
            }
        }

        total = tmptotal;
        tmptotal -= discount;
        gst = taxType ? Double.parseDouble(billFragment.edt_gst.getText().toString()) : 0;
        taxes = (tmptotal * gst) / 100;
        cartBottomSheetGrandTotal.setText(String.format(Locale.getDefault(), "%.2f", (tmptotal + taxes)));
        cartBottomSheetTotal.setText(String.format(Locale.getDefault(), "%.2f", total));
        cartBottomSheetTaxes.setText(String.format(Locale.getDefault(), "%.2f", taxes));
    }

    void calculateAmount(boolean taxType) {
        items = dbHelper.getItems();
        selectedItems.clear();
        total = 0;
        taxes = 0;
        gst = taxType ? Double.parseDouble(billFragment.edt_gst.getText().toString()) : 0;
        double tmptotal = 0;
        for (ItemModel itemModel : items) {
            if (itemModel.isSelected == 1) {
                selectedItems.add(itemModel);
                tmptotal += Double.parseDouble(itemModel.total);
            }
        }
        total = tmptotal;
        tmptotal -= discount;
        taxes = (tmptotal * gst) / 100;
        cartBottomSheetTotal.setText(String.format(Locale.getDefault(), "%.2f", total));
        cartBottomSheetGrandTotal.setText(String.format(Locale.getDefault(), "%.2f", tmptotal + taxes));
        cartBottomSheetTaxes.setText(String.format(Locale.getDefault(), "%.2f", taxes));
    }

    private Double getAmountFromEditText(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return 0.0;
        else return Double.parseDouble(editText.getText().toString().trim());
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

    void getCustomerNames() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("CUSTOMERLIST");
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
}