package com.balaji.calculator.model;

import android.graphics.Bitmap;

public class ItemModel {
    public int item_id;
    public String customer_name, cust_name, bill_date, carat_22, carat_18, silver;
    public String item_name, gross_weight, less_weight, net_weight, ghat_weight, selected_carat, item_price;
    public String making_charge, selected_varient, making_total;
    public String other1 = "", other2 = "";
    public String total;
    public double Touch;
    public String img, tag_no;
    public String img_path;
    public Bitmap bitmap;
    public int isSelected,itemPcs;

    public ItemModel() {
    }

    public ItemModel(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
