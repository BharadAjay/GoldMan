package com.balaji.calculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.Item> {

    Context context;
    List<Group> groups;
    OnRVItemClickListener<Group> listener;

    public GroupAdapter(Context context, List<Group> groups, OnRVItemClickListener<Group> listener) {
        this.context = context;
        this.groups = groups;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.title.setText(groups.get(position).getProductgroupName());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemCLick(position, groups.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        AppCompatTextView title;

        public Item(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
        }
    }
}
