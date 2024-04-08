package com.balaji.calculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.PhysicalAuditItem;
import com.balaji.calculator.R;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalAuditAdapter extends RecyclerView.Adapter<PhysicalAuditAdapter.ItemViewHolder> {

    Context context;
    List<PhysicalAuditItem> physicalAuditItems;

    public PhysicalAuditAdapter(Context context, List<PhysicalAuditItem> physicalAuditItems) {
        this.context = context;
        this.physicalAuditItems = physicalAuditItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_physical_audit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.itemNameView.setText(physicalAuditItems.get(position).getName());
        holder.btnPhysicalAuditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edtItemPieceView.getText().toString().isEmpty()) {
                    holder.edtItemPieceView.setError("Please enter valid quantity");
                } else {
                    holder.edtItemPieceView.setError(null);
                    holder.tvPieceTotal.setText(String.valueOf(Integer.parseInt(holder.tvPieceTotal.getText().toString()) + Integer.parseInt(holder.edtItemPieceView.getText().toString())));
                }
                physicalAuditItems.set(position, new PhysicalAuditItem(holder.itemNameView.getText().toString(), Integer.parseInt(holder.tvPieceTotal.getText().toString())));
                holder.edtItemPieceView.setText("");
            }
        });
        holder.btnQuantityClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvPieceTotal.setText("0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return physicalAuditItems.size();
    }

    public String getAllItems() {
        Map<String, Integer> items = new HashMap<>();

        for (PhysicalAuditItem item : physicalAuditItems) {
            items.put(item.getName(), item.getPiece());
        }

        return items.toString();
    }

    public String getUpdatedItems() {
        Map<String, Integer> updatedItems = new HashMap<>();

        for (PhysicalAuditItem item : physicalAuditItems) {
            if (item.getPiece() > 0)
                updatedItems.put(item.getName(), item.getPiece());
        }

        return updatedItems.toString();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, tvPieceTotal;
        EditText edtItemPieceView;
        MaterialButton btnPhysicalAuditAdd;
        ImageButton btnQuantityClear;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemName);
            edtItemPieceView = itemView.findViewById(R.id.itemPiece);
            btnPhysicalAuditAdd = itemView.findViewById(R.id.btnPhysicalAuditAdd);
            btnQuantityClear = itemView.findViewById(R.id.btnQuantityClear);
            tvPieceTotal = itemView.findViewById(R.id.tvPieceTotal);
        }
    }
}
