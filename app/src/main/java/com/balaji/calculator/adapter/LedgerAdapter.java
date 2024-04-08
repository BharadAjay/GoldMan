package com.balaji.calculator.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ItemLedger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.Item> {
    Context context;
    List<ItemLedger> itemLedgerList;
    OnRVItemClickListener<ItemLedger> listener;

    public LedgerAdapter(Context context, List<ItemLedger> itemLedgerList, OnRVItemClickListener<ItemLedger> listener) {
        this.context = context;
        this.itemLedgerList = itemLedgerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_ledger, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.mtvNameLedger.setText(itemLedgerList.get(position).getAccountName());
        holder.mtvAccountNoLedger.setText(itemLedgerList.get(position).getAccountNo());
        holder.mtvAmountLedger.setText("\u20B9" + String.format(Locale.getDefault(), "%.2f", Math.abs(itemLedgerList.get(position).getOpningAmt())));
        if (itemLedgerList.get(position).getOpningAmt() < 0) {
            holder.mtvAmountLedger.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.clr_negative, null));
            holder.mtvAmountLedger.append(" Dr");
        } else {
            holder.mtvAmountLedger.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blue, null));
            holder.mtvAmountLedger.append(" Cr");
        }
        holder.mtvGoldFineLedger.setText(String.format(Locale.getDefault(), "%02.3f", Math.abs(itemLedgerList.get(position).getOpnigFine())));
        if (itemLedgerList.get(position).getOpnigFine() < 0) {
            holder.mtvGoldFineLedger.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.clr_negative, null));
            holder.mtvGoldFineLedger.append(" Dr");
        } else {
            holder.mtvGoldFineLedger.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.blue, null));
            holder.mtvGoldFineLedger.append(" Cr");
        }
        holder.mtvSilverFineLedger.setText(String.valueOf(itemLedgerList.get(position).getOpeningSilver()));
        holder.mtvMobileLedger.setText(itemLedgerList.get(position).getMoblie());
        holder.mtvCityLedger.setText(itemLedgerList.get(position).getCity());
        holder.mbtnWhatsAppLedger.setOnClickListener(v -> {
            if (!itemLedgerList.get(position).getMoblie().isEmpty()) {

                PackageManager packageManager = context.getPackageManager();
                Intent intentWhatsApp = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + itemLedgerList.get(position).getMoblie() + "&text=" + URLEncoder.encode("Pay your pending amount of " + "\u20B9" + String.format(Locale.getDefault(), "%.2f", Math.abs(itemLedgerList.get(position).getOpningAmt())) + " quickly", "UTF-8");
                    intentWhatsApp.setPackage("com.whatsapp");
                    intentWhatsApp.setData(Uri.parse(url));
                    if (intentWhatsApp.resolveActivity(packageManager) != null) {
                        context.startActivity(intentWhatsApp);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        holder.mbtnCallLedger.setOnClickListener(v -> {
            if (!itemLedgerList.get(position).getMoblie().isEmpty()) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + itemLedgerList.get(position).getMoblie()));
                context.startActivity(intentCall);
            }
        });
        holder.mbtnMessageLedger.setOnClickListener(v -> {
            if (!itemLedgerList.get(position).getMoblie().isEmpty()) {
                Intent intentSMS = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + itemLedgerList.get(position).getMoblie()));
                intentSMS.putExtra("sms_body", "Pay your pending amount of " + "\u20B9" + String.format(Locale.getDefault(), "%.2f", Math.abs(itemLedgerList.get(position).getOpningAmt())) + " quickly");
                context.startActivity(intentSMS);
            }
        });
        holder.mbtnShareLedger.setOnClickListener(v -> {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/plain");
            intentShare.putExtra(Intent.EXTRA_SUBJECT, "Payment Remainder");
            intentShare.putExtra(Intent.EXTRA_TEXT, "Pay your pending amount of " + "\u20B9" + String.format(Locale.getDefault(), "%.2f", Math.abs(itemLedgerList.get(position).getOpningAmt())) + " quickly");
            context.startActivity(Intent.createChooser(intentShare, "Share"));
        });

//        if (itemLedgerList.get(position).getMoblie() == "") {
//            holder.mbtnWhatsAppLedger.setEnabled(false);
//            holder.mbtnCallLedger.setEnabled(false);
//            holder.mbtnMessageLedger.setEnabled(false);
//        } else {
//            holder.mbtnWhatsAppLedger.setEnabled(true);
//            holder.mbtnCallLedger.setEnabled(true);
//            holder.mbtnMessageLedger.setEnabled(true);
//        }

        holder.itemView.setOnClickListener(v -> {
            listener.onItemCLick(position, itemLedgerList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return itemLedgerList.size();
    }

    public void updateList(List<ItemLedger> itemLedgerList) {
        this.itemLedgerList = itemLedgerList;
        notifyDataSetChanged();
    }

    public class Item extends RecyclerView.ViewHolder {
        MaterialTextView mtvNameLedger, mtvAccountNoLedger, mtvAmountLedger, mtvGoldFineLedger, mtvSilverFineLedger, mtvMobileLedger, mtvCityLedger;
        MaterialButton mbtnWhatsAppLedger, mbtnCallLedger, mbtnMessageLedger, mbtnShareLedger;

        public Item(@NonNull View itemView) {
            super(itemView);

            mtvNameLedger = itemView.findViewById(R.id.mtvNameLedger);
            mtvAccountNoLedger = itemView.findViewById(R.id.mtvAccountNoLedger);
            mtvAmountLedger = itemView.findViewById(R.id.mtvAmountLedger);
            mtvGoldFineLedger = itemView.findViewById(R.id.mtvGoldFineLedger);
            mtvSilverFineLedger = itemView.findViewById(R.id.mtvSilverFineLedger);
            mtvMobileLedger = itemView.findViewById(R.id.mtvMobileLedger);
            mtvCityLedger = itemView.findViewById(R.id.mtvCityLedger);
            mbtnWhatsAppLedger = itemView.findViewById(R.id.mbtnWhatsAppLedger);
            mbtnCallLedger = itemView.findViewById(R.id.mbtnCallLedger);
            mbtnMessageLedger = itemView.findViewById(R.id.mbtnMessageLedger);
            mbtnShareLedger = itemView.findViewById(R.id.mbtnShareLedger);
        }
    }
}
