package com.balaji.calculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.model.ItemLedgerTransaction;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Locale;

public class LedgerTransactionAdapter extends RecyclerView.Adapter<LedgerTransactionAdapter.Item> {
    Context context;
    List<ItemLedgerTransaction> listItemLedgerTransaction;

    public LedgerTransactionAdapter(Context context, List<ItemLedgerTransaction> listItemLedgerTransaction) {
        this.context = context;
        this.listItemLedgerTransaction = listItemLedgerTransaction;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_ledger_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.mtvBookLedgerTransaction.setText(listItemLedgerTransaction.get(position).getBook());
        holder.mtvVoucherNoLedgerTransaction.setText(String.valueOf(listItemLedgerTransaction.get(position).getVchNo()));
        holder.mtvVoucherDateLedgerTransaction.setText(String.valueOf(listItemLedgerTransaction.get(position).getVchDate()));
        if (listItemLedgerTransaction.get(position).getCrAmt() != 0.0) {
            holder.mtvCreditAmountLedgerTransaction.setText("₹" + String.format(Locale.getDefault(), "%.0f", listItemLedgerTransaction.get(position).getCrAmt()));
        }
        if (listItemLedgerTransaction.get(position).getDrAmt() != 0.0) {
            holder.mtvDebitAmountLedgerTransaction.setText("₹" + String.format(Locale.getDefault(), "%.0f", listItemLedgerTransaction.get(position).getDrAmt()));
        }
        if (listItemLedgerTransaction.get(position).getCrFine() != 0.0) {
            holder.mtvCreditGoldLedgerTransaction.setText(String.format(Locale.getDefault(), "%.3f", listItemLedgerTransaction.get(position).getCrFine()));
        }
        if (listItemLedgerTransaction.get(position).getDrFine() != 0.0) {
            holder.mtvDebitGoldLedgerTransaction.setText(String.format(Locale.getDefault(), "%.3f", listItemLedgerTransaction.get(position).getDrFine()));
        }
//        holder.mtvCreditSilverLedgerTransaction.setText(String.valueOf(listItemLedgerTransaction.get(position).getCrSilver()));
//        holder.mtvDebitSilverLedgerTransaction.setText(String.valueOf(listItemLedgerTransaction.get(position).getDrSilver()));
    }

    @Override
    public int getItemCount() {
        return listItemLedgerTransaction.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        MaterialTextView mtvBookLedgerTransaction, mtvVoucherNoLedgerTransaction, mtvVoucherDateLedgerTransaction, mtvCreditAmountLedgerTransaction, mtvDebitAmountLedgerTransaction, mtvCreditGoldLedgerTransaction,
                mtvDebitGoldLedgerTransaction, mtvCreditSilverLedgerTransaction, mtvDebitSilverLedgerTransaction;

        public Item(@NonNull View itemView) {
            super(itemView);
            mtvBookLedgerTransaction = itemView.findViewById(R.id.mtvBookLedgerTransaction);
            mtvVoucherNoLedgerTransaction = itemView.findViewById(R.id.mtvVoucherNoLedgerTransaction);
            mtvVoucherDateLedgerTransaction = itemView.findViewById(R.id.mtvVoucherDateLedgerTransaction);
            mtvCreditAmountLedgerTransaction = itemView.findViewById(R.id.mtvCreditAmountLedgerTransaction);
            mtvDebitAmountLedgerTransaction = itemView.findViewById(R.id.mtvDebitAmountLedgerTransaction);
            mtvCreditGoldLedgerTransaction = itemView.findViewById(R.id.mtvCreditGoldLedgerTransaction);
            mtvDebitGoldLedgerTransaction = itemView.findViewById(R.id.mtvDebitGoldLedgerTransaction);
//            mtvCreditSilverLedgerTransaction = itemView.findViewById(R.id.mtvCreditSilverLedgerTransaction);
//            mtvDebitSilverLedgerTransaction = itemView.findViewById(R.id.mtvDebitSilverLedgerTransaction);
        }
    }
}
