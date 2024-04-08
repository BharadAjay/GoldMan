package com.balaji.calculator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balaji.calculator.adapter.GroupAdapter;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.Group;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment implements OnRVItemClickListener<Group> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BillFragment billFragment;
    ProductDetailFragment fragmentProductDetail;
    public GroupAdapter groupAdapter;
    ImageView ivBack;
    String groupName;
    TextView tvProductName;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupFragment() {
        // Required empty public constructor
    }

    public GroupFragment(BillFragment billFragment, String groupName) {

        this.billFragment = billFragment;
        this.groupName = groupName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
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

        ivBack = view.findViewById(R.id.ivBack);
        tvProductName = view.findViewById(R.id.tvProductsTitle);
        tvProductName.setText(groupName);
        getDataFromServer();

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        groupAdapter = new GroupAdapter(getActivity(), billFragment.groups, this);

        RecyclerView recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewProducts.setLayoutManager(linearLayoutManager);
        recyclerViewProducts.setAdapter(groupAdapter);

        return view;
    }

    private void getDataFromServer() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                Log.d("SendMSG", "getDataToServer: " + groupName);
                                String[] splitedItem = groupName.split("-");

                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("GROUPS:" + splitedItem[0].trim());
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

    @Override
    public void onItemCLick(int position, Group item) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentProductDetail = new ProductDetailFragment(billFragment, groupName + ":" + item.getProductgroupName());
        transaction.replace(R.id.frame_full, fragmentProductDetail, "ProductDetailFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}