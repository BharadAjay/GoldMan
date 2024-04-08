package com.balaji.calculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.OldGoldModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OldGoldItemAdapter extends RecyclerView.Adapter<OldGoldItemAdapter.Item> {
    Context context;
    List<OldGoldModel> itemOldGoldList;
    OnRVItemClickListener<OldGoldModel> listener;

    public OldGoldItemAdapter(Context context, List<OldGoldModel> itemOldGoldList, OnRVItemClickListener<OldGoldModel> listener) {
        this.context = context;
        this.itemOldGoldList = itemOldGoldList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OldGoldItemAdapter.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_old_gold, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.mtvItemNameOldGold.setText(itemOldGoldList.get(position).getItemName());
        holder.mtvItemRateOldGold.setText(String.valueOf(itemOldGoldList.get(position).getRate()));
        holder.mtvItemGrossWeightOldGold.setText(String.valueOf(itemOldGoldList.get(position).getGrossWeight()));
        holder.mtvItemAmountOldGold.setText("\u20B9" + String.valueOf(itemOldGoldList.get(position).getAmount()));
        holder.mbtnDeleteItemOldGold.setOnClickListener(v -> listener.onItemCLick(position, itemOldGoldList.get(position)));
    }

    @Override
    public int getItemCount() {
        return itemOldGoldList.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        MaterialTextView mtvItemNameOldGold, mtvItemRateOldGold, mtvItemGrossWeightOldGold, mtvItemAmountOldGold;
        MaterialButton mbtnDeleteItemOldGold;

        public Item(@NonNull View itemView) {
            super(itemView);
            mtvItemNameOldGold = itemView.findViewById(R.id.mtvItemNameOldGold);
            mtvItemRateOldGold = itemView.findViewById(R.id.mtvItemRateOldGold);
            mtvItemGrossWeightOldGold = itemView.findViewById(R.id.mtvItemGrossWeightOldGold);
            mtvItemAmountOldGold = itemView.findViewById(R.id.mtvItemAmountOldGold);
            mbtnDeleteItemOldGold = itemView.findViewById(R.id.mbtnDeleteItemOldGold);
        }
    }
}
