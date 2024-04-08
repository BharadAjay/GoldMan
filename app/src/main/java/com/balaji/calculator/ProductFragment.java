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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balaji.calculator.adapter.ProductAdapter;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements OnRVItemClickListener<ProductItem> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BillFragment billFragment;
    public ProductAdapter productAdapter;
    ImageView ivBack;
    public TextView tvProductsTitle;
    public GroupFragment fragmentGroup;
    EditText edtProductSearch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    public ProductFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        Log.d("opopo", "onCreate: opopop");
        getDataFromServer();

        ivBack = view.findViewById(R.id.ivBack);
        tvProductsTitle = view.findViewById(R.id.tvProductsTitle);
        edtProductSearch = view.findViewById(R.id.edtProductSearch);

        edtProductSearch.addTextChangedListener(new TextWatcher() {
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

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        productAdapter = new ProductAdapter(getActivity(), billFragment.avilableProducts, this);

        RecyclerView recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewProducts.setLayoutManager(linearLayoutManager);
        recyclerViewProducts.setAdapter(productAdapter);

        tvProductsTitle.setText(tvProductsTitle.getText().toString() + " - " + productAdapter.getItemCount());

        return view;
    }

    void filter(String text) {
        List<ProductItem> temp = new ArrayList<ProductItem>();
        for (ProductItem item : billFragment.avilableProducts) {
            if (item.getProductName().toLowerCase().contains(text))
                temp.add(item);
        }

        productAdapter.updateList(temp);
    }

    @Override
    public void onItemCLick(int position, ProductItem item) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentGroup = new GroupFragment(billFragment, item.getProductName());
        transaction.replace(R.id.frame_full, fragmentGroup, "GroupFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getDataFromServer() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("PRODUCTS");
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
}