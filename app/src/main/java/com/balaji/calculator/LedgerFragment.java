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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balaji.calculator.adapter.LedgerAdapter;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ItemLedger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.itextpdf.text.pdf.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedgerFragment extends Fragment implements OnRVItemClickListener<ItemLedger> {
    ImageView ivBack;
    TextInputLayout edtFilterLedger;
    LedgerAdapter adapterLedger;
    //    List<ItemLedger> listItemLedger = new ArrayList<>();
    RecyclerView rvLedger;
    BillFragment billFragment;
    BottomSheetDialog bottomSheetDialog;
    public LedgerTransactionFragment fragmentLedgerTransaction;
    ChipGroup chipGroupFilterByGroupLedger, chipGroupFilterByCityLedger;
    Chip chipAtoZFilterLedger, chipZtoAFilterLedger, chipHighestFilterLedger, chipLowestFilterLedger, chipNewestFilterLedger, chipOldestFilterLedger;
    String selectedSearchOptions = "";
    List<Chip> selectedGroupOptionsChips = new ArrayList<>();
    List<Chip> selectedCityOptionsChips = new ArrayList<>();
    List<ItemLedger> filteredList = new ArrayList<>();
    MaterialButton mbtnClearFilterLedger;
    Set<String> hashSetGroupNames = new HashSet<String>();
    Set<String> hashSetCityNames = new HashSet<String>();
    boolean inUse = false;
    CircularProgressIndicator cpiLedger;
    MaterialTextView mtvTotalCreditAmountLedger, mtvTotalDebitAmountLedger, mtvTotalCreditDebitAmountLedger, mtvTotalCreditGoldFineLedger, mtvTotalDebitGoldFineLedger,
            mtvTotalCreditDebitGoldFineLedger, mtvTotalCreditSilverFineLedger, mtvTotalDebitSilverFineLedger, mtvTotalCreditDebitSilverFineLedger;
    double totalCreditAmountLedger = 0.0, totalDebitAmountLedger = 0.0, totalCreditDebitAmountLedger = 0.0, totalCreditGoldFineLedger = 0.0, totalDebitGoldFineLedger = 0.0,
            totalCreditDebitGoldFineLedger = 0.0, totalCreditSilverFineLedger = 0.0, totalDebitSilverFineLedger = 0.0, totalCreditDebitSilverFineLedger = 0.0;
    RelativeLayout rlTotalLedgerBottomSheetLayout;
    BottomSheetBehavior bottomSheetBehaviorLedger;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LedgerFragment() {
        // Required empty public constructor
    }

    public LedgerFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LedgerFragment newInstance(String param1, String param2) {
        LedgerFragment fragment = new LedgerFragment();
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
        View view = inflater.inflate(R.layout.fragment_ledger, container, false);

        ivBack = view.findViewById(R.id.ivBack);
        edtFilterLedger = view.findViewById(R.id.edtFilterLedger);
        rvLedger = view.findViewById(R.id.rvLedger);
        cpiLedger = view.findViewById(R.id.cpiLedger);
        rlTotalLedgerBottomSheetLayout = view.findViewById(R.id.rlTotalLedgerBottomSheetLayout);
        mtvTotalCreditAmountLedger = view.findViewById(R.id.mtvTotalCreditAmountLedger);
        mtvTotalDebitAmountLedger = view.findViewById(R.id.mtvTotalDebitAmountLedger);
        mtvTotalCreditDebitAmountLedger = view.findViewById(R.id.mtvTotalCreditDebitAmountLedger);
        mtvTotalCreditGoldFineLedger = view.findViewById(R.id.mtvTotalCreditGoldFineLedger);
        mtvTotalDebitGoldFineLedger = view.findViewById(R.id.mtvTotalDebitGoldFineLedger);
        mtvTotalCreditDebitGoldFineLedger = view.findViewById(R.id.mtvTotalCreditDebitGoldFineLedger);
        mtvTotalCreditSilverFineLedger = view.findViewById(R.id.mtvTotalCreditSilverFineLedger);
        mtvTotalDebitSilverFineLedger = view.findViewById(R.id.mtvTotalDebitSilverFineLedger);
        mtvTotalCreditDebitSilverFineLedger = view.findViewById(R.id.mtvTotalCreditDebitSilverFineLedger);

        //Bottom sheet behavior initializations
        bottomSheetBehaviorLedger = BottomSheetBehavior.from(rlTotalLedgerBottomSheetLayout);

        //Bottom sheet dialog filter initializations
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_ledger_filter_layout);
        mbtnClearFilterLedger = bottomSheetDialog.findViewById(R.id.mbtnClearFilterLedger);
        chipGroupFilterByGroupLedger = bottomSheetDialog.findViewById(R.id.chipGroupFilterByGroupLedger);
        chipGroupFilterByCityLedger = bottomSheetDialog.findViewById(R.id.chipGroupFilterByCityLedger);
        chipAtoZFilterLedger = bottomSheetDialog.findViewById(R.id.chipAtoZFilterLedger);
        chipZtoAFilterLedger = bottomSheetDialog.findViewById(R.id.chipZtoAFilterLedger);
        chipHighestFilterLedger = bottomSheetDialog.findViewById(R.id.chipHighestFilterLedger);
        chipLowestFilterLedger = bottomSheetDialog.findViewById(R.id.chipLowestFilterLedger);
        chipNewestFilterLedger = bottomSheetDialog.findViewById(R.id.chipNewestFilterLedger);
        chipOldestFilterLedger = bottomSheetDialog.findViewById(R.id.chipOldestFilterLedger);

        initView();

        ivBack.setOnClickListener(v -> getActivity().onBackPressed());

        edtFilterLedger.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedSearchOptions = s.toString();
                filter(selectedSearchOptions, selectedGroupOptionsChips, selectedCityOptionsChips);
            }
        });

        edtFilterLedger.setEndIconOnClickListener(v -> {
            bottomSheetDialog.show();
        });

        chipAtoZFilterLedger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipAtoZFilterLedger.setChipStrokeColorResource(R.color.green200);
                chipAtoZFilterLedger.setChipBackgroundColorResource(R.color.green200);
                filteredList.sort((o1, o2) -> o1.getAccountName().compareTo(o2.getAccountName()));
                adapterLedger.updateList(filteredList);
            } else {
                chipAtoZFilterLedger.setChipStrokeColorResource(R.color.gray_line);
                chipAtoZFilterLedger.setChipBackgroundColorResource(R.color.white);
            }
        });

        chipZtoAFilterLedger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipZtoAFilterLedger.setChipStrokeColorResource(R.color.green200);
                chipZtoAFilterLedger.setChipBackgroundColorResource(R.color.green200);
                filteredList.sort((o1, o2) -> o2.getAccountName().compareTo(o1.getAccountName()));
                adapterLedger.updateList(filteredList);
            } else {
                chipZtoAFilterLedger.setChipStrokeColorResource(R.color.gray_line);
                chipZtoAFilterLedger.setChipBackgroundColorResource(R.color.white);
            }
        });

        chipHighestFilterLedger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipHighestFilterLedger.setChipStrokeColorResource(R.color.green200);
                chipHighestFilterLedger.setChipBackgroundColorResource(R.color.green200);
                filteredList.sort((o1, o2) -> (int) (o1.getOpningAmt() - o2.getOpningAmt()));
                adapterLedger.updateList(filteredList);
            } else {
                chipHighestFilterLedger.setChipStrokeColorResource(R.color.gray_line);
                chipHighestFilterLedger.setChipBackgroundColorResource(R.color.white);
            }
        });

        chipLowestFilterLedger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipLowestFilterLedger.setChipStrokeColorResource(R.color.green200);
                chipLowestFilterLedger.setChipBackgroundColorResource(R.color.green200);
                filteredList.sort((o1, o2) -> (int) (o2.getOpningAmt() - o1.getOpningAmt()));
                adapterLedger.updateList(filteredList);
            } else {
                chipLowestFilterLedger.setChipStrokeColorResource(R.color.gray_line);
                chipLowestFilterLedger.setChipBackgroundColorResource(R.color.white);
            }
        });

        chipGroupFilterByGroupLedger.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!inUse) {
                selectedGroupOptionsChips.clear();

                for (int checkedId : checkedIds) {
                    Chip selectedChip = group.findViewById(checkedId);
                    if (selectedChip != null) {
                        selectedGroupOptionsChips.add(selectedChip);
                    }
                }

                filter(selectedSearchOptions, selectedGroupOptionsChips, selectedCityOptionsChips);
            }
        });

        chipGroupFilterByCityLedger.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!inUse) {
                selectedCityOptionsChips.clear();

                for (int checkedId : checkedIds) {
                    Chip selectedChip = group.findViewById(checkedId);
                    if (selectedChip != null) {
                        selectedCityOptionsChips.add(selectedChip);
                    }
                }

                filter(selectedSearchOptions, selectedGroupOptionsChips, selectedCityOptionsChips);
            }
        });

        mbtnClearFilterLedger.setOnClickListener(v -> {
            inUse = true;

            for (Chip selectedChip : selectedGroupOptionsChips) {
                selectedChip.setChecked(false);
            }

            for (Chip selectedChip : selectedCityOptionsChips) {
                selectedChip.setChecked(false);
            }

            adapterLedger.updateList(billFragment.listItemLedger);
            inUse = false;
            selectedGroupOptionsChips.clear();
            bottomSheetDialog.dismiss();
        });

//        billFragment.listItemLedger.add(new ItemLedger("Deep Popat", "001", 10, 10, 0, "+919824631904", "Rajkot"));
//        listItemLedger.add(new ItemLedger("Gold Book", 1000, 10, 10, "+911234567890", "Morbi"));
//        listItemLedger.add(new ItemLedger("Book Infotech", 1000, 10, 10, "+911234567890", "Rajkot"));

        adapterLedger = new LedgerAdapter(getActivity(), billFragment.listItemLedger, this);

        rvLedger.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvLedger.setAdapter(adapterLedger);

        return view;
    }

    void initView() {
        getDataFromServer();
    }

    void filter(String strSearchOptions, List<Chip> listGroupOptions, List<Chip> listCityOptions) {
        List<ItemLedger> temp = new ArrayList<ItemLedger>();
        boolean isFound;

        filteredList.clear();

        if (!strSearchOptions.equals("")) {
            for (ItemLedger item : billFragment.listItemLedger) {
                isFound = false;

                if (item.getAccountName() != null) {
                    if (item.getAccountName().toLowerCase().contains(strSearchOptions))
                        isFound = true;
                }
                if (item.getAccountNo() != null) {
                    if (item.getAccountNo().toLowerCase().contains(strSearchOptions))
                        isFound = true;
                }
                if (item.getMoblie() != null) {
                    if (item.getMoblie().toLowerCase().contains(strSearchOptions))
                        isFound = true;
                }
                if (item.getOpningAmt() != 0.0d) {
                    if (String.valueOf(item.getOpningAmt()).toLowerCase().contains(strSearchOptions))
                        isFound = true;
                }
                if (item.getCity() != null) {
                    if (item.getCity().toLowerCase().contains(strSearchOptions))
                        isFound = true;
                }
                if (isFound)
                    temp.add(item);
            }
            filteredList.addAll(temp);
        } else
            filteredList.addAll(billFragment.listItemLedger);

        if (!listGroupOptions.isEmpty()) {
            temp.clear();
            for (Chip chip : listGroupOptions) {
                for (ItemLedger item : filteredList) {
                    if (item.getAcGpName() != null)
                        if (item.getAcGpName().toLowerCase().contains(chip.getText().toString().toLowerCase()))
                            temp.add(item);
                }
            }
            filteredList.clear();
            filteredList.addAll(temp);
        }

        if (!listCityOptions.isEmpty()) {
            temp.clear();
            for (Chip chip : listCityOptions) {
                for (ItemLedger item : filteredList) {
                    if (item.getCity() != null) {
                        if (item.getCity().toLowerCase().contains(chip.getText().toString().toLowerCase()))
                            temp.add(item);
                    }
                }
            }
            filteredList.clear();
            filteredList.addAll(temp);
        }

        adapterLedger.updateList(filteredList);
        calculateTotalAmount(filteredList);
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

    private void getDataFromServer() {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("LEDGER");
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

    @Override
    public void onItemCLick(int position, ItemLedger item) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentLedgerTransaction = new LedgerTransactionFragment(billFragment, item.getAccountNo());
        transaction.replace(R.id.frame_full, fragmentLedgerTransaction, "LedgerTransactionFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void refreshData() {
        adapterLedger.notifyDataSetChanged();

        filteredList.addAll(billFragment.listItemLedger);

        for (ItemLedger itemLedger : billFragment.listItemLedger) {
            if (itemLedger.getAcGpName() != null)
                hashSetGroupNames.add(itemLedger.getAcGpName().toUpperCase().charAt(0) + itemLedger.getAcGpName().toLowerCase().substring(1));

            if (itemLedger.getCity() != null && !itemLedger.getCity().equals(""))
                hashSetCityNames.add(itemLedger.getCity().toUpperCase().charAt(0) + itemLedger.getCity().toLowerCase().substring(1));
        }

        calculateTotalAmount(billFragment.listItemLedger);

        List<String> listSortedHashSetGroupNames = new ArrayList<>(hashSetGroupNames);
        Collections.sort(listSortedHashSetGroupNames);

        for (String groupName : listSortedHashSetGroupNames) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip_layout, chipGroupFilterByGroupLedger, false);
            chip.setText(groupName);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipStrokeColorResource(R.color.green200);
                    chip.setChipBackgroundColorResource(R.color.green200);
                } else {
                    chip.setChipStrokeColorResource(R.color.gray_line);
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            });
            chipGroupFilterByGroupLedger.addView(chip);
        }

        List<String> listSortedHashSetCityNames = new ArrayList<>(hashSetCityNames);
        Collections.sort(listSortedHashSetCityNames);

        for (String cityName : listSortedHashSetCityNames) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.single_chip_layout, chipGroupFilterByGroupLedger, false);
            chip.setText(cityName);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipStrokeColorResource(R.color.green200);
                    chip.setChipBackgroundColorResource(R.color.green200);
                } else {
                    chip.setChipStrokeColorResource(R.color.gray_line);
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            });
            chipGroupFilterByCityLedger.addView(chip);
        }
    }

    public void isDataLoadingLedger(boolean isLoading) {
        if (isLoading) {
            cpiLedger.setVisibility(View.VISIBLE);
            rvLedger.setVisibility(View.GONE);
        } else {
            cpiLedger.setVisibility(View.GONE);
            rvLedger.setVisibility(View.VISIBLE);
        }
    }

    void calculateTotalAmount(List<ItemLedger> listItemLedger) {
        totalCreditAmountLedger = 0.0;
        totalDebitAmountLedger = 0.0;
        totalCreditDebitAmountLedger = 0.0;
        totalCreditGoldFineLedger = 0.0;
        totalDebitGoldFineLedger = 0.0;
        totalCreditDebitGoldFineLedger = 0.0;
        totalCreditSilverFineLedger = 0.0;
        totalDebitSilverFineLedger = 0.0;
        totalCreditDebitSilverFineLedger = 0.0;

        for (ItemLedger itemLedger : listItemLedger) {
            if (itemLedger.getOpningAmt() > 0)
                totalCreditAmountLedger += itemLedger.getOpningAmt();
            else
                totalDebitAmountLedger += itemLedger.getOpningAmt();

            if (itemLedger.getOpnigFine() > 0)
                totalCreditGoldFineLedger += itemLedger.getOpnigFine();
            else
                totalDebitGoldFineLedger += itemLedger.getOpnigFine();

            if (itemLedger.getOpeningSilver() > 0)
                totalCreditSilverFineLedger += itemLedger.getOpeningSilver();
            else
                totalDebitSilverFineLedger += itemLedger.getOpeningSilver();
        }

        totalCreditDebitAmountLedger = totalCreditAmountLedger + totalDebitAmountLedger;
        totalCreditDebitGoldFineLedger = totalCreditGoldFineLedger + totalDebitGoldFineLedger;
        totalCreditDebitSilverFineLedger = totalCreditSilverFineLedger + totalDebitSilverFineLedger;

        mtvTotalCreditAmountLedger.setText(String.format(Locale.getDefault(), "%.2f", totalCreditAmountLedger));
        mtvTotalDebitAmountLedger.setText(String.format(Locale.getDefault(), "%.2f", totalDebitAmountLedger));
        mtvTotalCreditDebitAmountLedger.setText(String.format(Locale.getDefault(), "%.2f", totalCreditDebitAmountLedger));
        mtvTotalCreditGoldFineLedger.setText(String.format(Locale.getDefault(), "%.3f", totalCreditGoldFineLedger));
        mtvTotalDebitGoldFineLedger.setText(String.format(Locale.getDefault(), "%.3f", totalDebitGoldFineLedger));
        mtvTotalCreditDebitGoldFineLedger.setText(String.format(Locale.getDefault(), "%.3f", totalCreditDebitGoldFineLedger));
        mtvTotalCreditSilverFineLedger.setText(String.format(Locale.getDefault(), "%.3f", totalCreditSilverFineLedger));
        mtvTotalDebitSilverFineLedger.setText(String.format(Locale.getDefault(), "%.3f", totalDebitSilverFineLedger));
        mtvTotalCreditDebitSilverFineLedger.setText(String.format(Locale.getDefault(), "%.3f", totalCreditDebitSilverFineLedger));
    }
}