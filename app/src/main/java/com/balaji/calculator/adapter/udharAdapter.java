package com.balaji.calculator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balaji.calculator.R;
import com.balaji.calculator.model.udharModel;

import java.util.ArrayList;
import java.util.List;

public class udharAdapter extends RecyclerView.Adapter<udharAdapter.ViewHolder>{
    private List<udharModel> dataList;
    private dialogClickListener<udharModel> listener;


    public udharAdapter(ArrayList<udharModel> data, dialogClickListener<udharModel> listener) {
        this.dataList = data;
        this.listener = listener;
    }

    public udharAdapter(ArrayList<udharModel> data) {
        this.dataList = data;
    }
    public void updatelist(ArrayList<udharModel> temp){
        this.dataList = temp;
        notifyDataSetChanged();
    }
    public void updateData(List<udharModel> data) {
        this.dataList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        udharModel data = dataList.get(holder.getAdapterPosition()); // Use getAdapterPosition()
        holder.textView.setText(data.getEdtITNameUdhar());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int clickedPosition = holder.getAdapterPosition(); // Use getAdapterPosition()
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        listener.onItemCLick(clickedPosition, data);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public interface dialogClickListener<T> {
        void onItemCLick(int position, T item);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtName);
        }
    }
}
