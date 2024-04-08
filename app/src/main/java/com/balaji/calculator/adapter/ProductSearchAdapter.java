package com.balaji.calculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ItemModel;
import com.balaji.calculator.model.ProductItem;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.Item> {
    Context context;
    List<String> productSearchList;
    OnRVItemClickListener<String> listener;

    public ProductSearchAdapter(Context context, List<String> productSearchList, OnRVItemClickListener<String> listener) {
        this.context = context;
        this.productSearchList = productSearchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductSearchAdapter.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_search_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.mtvItemNameBill.setText(productSearchList.get(position));
        holder.itemView.setOnClickListener(v -> listener.onItemCLick(position, productSearchList.get(position)));
    }

    @Override
    public int getItemCount() {
        return productSearchList.size();
    }

    public void updateList(List<String> productItems) {
        this.productSearchList = productItems;
        notifyDataSetChanged();
    }

    public class Item extends RecyclerView.ViewHolder {
        MaterialTextView mtvItemNameBill;

        public Item(@NonNull View itemView) {
            super(itemView);
            mtvItemNameBill = itemView.findViewById(R.id.mtvItemNameBill);
        }
    }
}
