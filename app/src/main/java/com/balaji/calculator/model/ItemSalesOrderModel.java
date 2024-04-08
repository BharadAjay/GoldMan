package com.balaji.calculator.model;

import android.graphics.Bitmap;

public class ItemSalesOrderModel
{
    public int item_id,item_pieces;

    public String fix_rete,delivery_rete;
    public String delivery_date,item_name,item_design,gross_weight,less_weight,net_weight,selected_rate,item_rate,item_amount,item_purity,item_note;
    public String selected_varient,making_charge,making_total;

    public String other1 = "", other2 = "";

    public String total;

    public String img, tag_no;

    public String img_path;

    public Bitmap bitmap;

    public int isSelected;

    public ItemSalesOrderModel() {
    }

    public ItemSalesOrderModel(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
