package com.balaji.calculator.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.listeners.OnRVItemClickListener;
import com.balaji.calculator.model.ItemModel;
import com.balaji.calculator.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Item> {

    Context context;
    List<ItemModel> groups;
    OnRVItemClickListener<ItemModel> listener;
    String gst;

    public CartAdapter(Context context, List<ItemModel> groups, OnRVItemClickListener<ItemModel> listener, String gst) {
        this.context = context;
        this.groups = groups;
        this.listener = listener;
        this.gst = gst;
    }


    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.ivDelete.setOnClickListener(v -> listener.onItemCLick(position, groups.get(position)));
//        holder.tvCounter.setText(position + 1 + "");
        holder.tvItemName.setText(groups.get(position).item_name);
        holder.tvTagNo.setText(groups.get(position).tag_no);
        holder.tvItemPriceTaxable.setText("\u20B9" + String.valueOf(groups.get(position).total));
        holder.tvItemPrice.setText("\u20B9" + String.valueOf(Double.parseDouble(groups.get(position).total) + (Double.parseDouble(groups.get(position).total) * (Double.parseDouble(gst) / 100))));
        holder.tvItemNetWeight.setText(groups.get(position).net_weight + "g");

//        Uri imageUri = Uri.parse(groups.get(position).img_path);
//        holder.ivProductImage.setImageURI(imageUri);

//        Picasso.get().load("https://picsum.photos/1900/1200").fit().placeholder(R.drawable.ic_plceholder).into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        MaterialTextView tvTagNo, tvItemName, tvItemPriceTaxable, tvItemPrice, tvItemNetWeight;
        MaterialButton ivDelete;
        ShapeableImageView ivProductImage;

        public Item(@NonNull View itemView) {
            super(itemView);
            tvTagNo = itemView.findViewById(R.id.tvTagNo);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPriceTaxable = itemView.findViewById(R.id.tvItemPriceTaxable);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemNetWeight = itemView.findViewById(R.id.tvItemNetWeight);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
