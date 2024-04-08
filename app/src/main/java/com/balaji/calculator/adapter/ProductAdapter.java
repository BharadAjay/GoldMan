package com.balaji.calculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ProductItem;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Item> {

    Context context;
    List<ProductItem> productItems;
    OnRVItemClickListener<ProductItem> listener;

    public ProductAdapter(Context context, List<ProductItem> groups, OnRVItemClickListener<ProductItem> listener) {
        this.context = context;
        this.productItems = groups;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.title.setText(productItems.get(position).getProductName());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemCLick(position, productItems.get(position));
        });
    }

    public void updateList(List<ProductItem> productItems) {
        this.productItems = productItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

        public class Item extends RecyclerView.ViewHolder {
            AppCompatTextView title;

            public Item(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvTitle);
            }
        }
}
