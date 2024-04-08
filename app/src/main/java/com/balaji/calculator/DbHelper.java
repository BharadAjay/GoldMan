package com.balaji.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.balaji.calculator.model.ItemModel;
import com.balaji.calculator.model.ItemSalesOrderModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE_ITEM_MASTER = "Create Table item_master(item_id integer PRIMARY KEY AUTOINCREMENT,cust_name text,bill_date text, carat_22 text, " +
            "carat_18 text, item_name text,net_weight text,selected_carat text,item_price text,making_charge text,selected_varient text,making_total text,other1 text," +
            "other2 text,total text,silver text,img text,tag_no text,gross_weight text,less_weight text,ghat_weight text,img_path text,isSelected integer,itemPcs integer," +
            "touch integer)";

    private String CRATE_TABLE_ITEM_SALES_ORDER_MASTER="Create Table item_sales_order_master(item_id integer PRIMARY KEY AUTOINCREMENT,delivery_date text,tag_no text,item_name text,item_design text,item_pieces text,gross_weight text,less_weight text,net_weight text,selected_rate text,item_rate text,item_amount text,item_purity text,selected_varient text,making_charge text,making_total text,"+
            "other1 text,other2 text,item_note text, img text,img_path text,isSelected integer,total text)";
    private String DELETE_TABLE_ITEM_SALES_ORDER_MASTER="drop table if exists item_sales_order_master";
    private String DELETE_TABLE_ITEM_MASTER = "drop table if exists item_master";
    private static String DATABASE_NAME = "gcalc.db";

    public DbHelper(Context context) {
        super(context, "gcalc.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM_MASTER);
        sqLiteDatabase.execSQL(CRATE_TABLE_ITEM_SALES_ORDER_MASTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DELETE_TABLE_ITEM_MASTER);
        db.execSQL(CREATE_TABLE_ITEM_MASTER);
        db.execSQL(DELETE_TABLE_ITEM_SALES_ORDER_MASTER);
        db.execSQL(CRATE_TABLE_ITEM_SALES_ORDER_MASTER);
    }

    public long insertRecord(ItemModel itemModel) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("cust_name", itemModel.cust_name);
        val.put("bill_date", itemModel.bill_date);
        val.put("carat_22", itemModel.carat_22);
        val.put("carat_18", itemModel.carat_18);
        val.put("item_name", itemModel.item_name);
        val.put("net_weight", itemModel.net_weight);
        val.put("selected_carat", itemModel.selected_carat);
        val.put("item_price", itemModel.item_price);
        val.put("making_charge", itemModel.making_charge);
        val.put("selected_varient", itemModel.selected_varient);
        val.put("making_total", itemModel.making_total);
        val.put("other1", itemModel.other1);
        val.put("other2", itemModel.other2);
        val.put("total", itemModel.total);
        val.put("silver", itemModel.silver);
        val.put("img", itemModel.img);
        val.put("tag_no", itemModel.tag_no);
        val.put("gross_weight", itemModel.gross_weight);
        val.put("less_weight", itemModel.less_weight);
        val.put("ghat_weight", itemModel.ghat_weight);
        val.put("img_path", itemModel.img_path);
        val.put("isSelected", 0);
        val.put("itemPcs", itemModel.itemPcs);
        val.put("touch", itemModel.Touch);

        return db.insert("item_master", null, val);
    }

    public long insertRecordSalesOrder(ItemSalesOrderModel itemSalesOrderModel) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("delivery_date", itemSalesOrderModel.delivery_date);
        val.put("item_name", itemSalesOrderModel.item_name);
        val.put("item_design",itemSalesOrderModel.item_design);
        val.put("gross_weight",itemSalesOrderModel.gross_weight);
        val.put("less_weight",itemSalesOrderModel.less_weight);
        val.put("net_weight", itemSalesOrderModel.net_weight);
        val.put("selected_rate",itemSalesOrderModel.selected_rate);
        val.put("item_rate",itemSalesOrderModel.item_rate);
        val.put("item_amount",itemSalesOrderModel.item_amount);
        val.put("item_purity",itemSalesOrderModel.item_purity);
        val.put("making_charge", itemSalesOrderModel.making_charge);
        val.put("selected_varient", itemSalesOrderModel.selected_varient);
        val.put("making_total", itemSalesOrderModel.making_total);
        val.put("other1", itemSalesOrderModel.other1);
        val.put("other2", itemSalesOrderModel.other2);
        val.put("item_note",itemSalesOrderModel.item_note);
        val.put("total", itemSalesOrderModel.total);
        val.put("img", itemSalesOrderModel.img);
        val.put("tag_no", itemSalesOrderModel.tag_no);
        val.put("img_path", itemSalesOrderModel.img_path);
        val.put("isSelected", 0);
        val.put("item_pieces", itemSalesOrderModel.item_pieces);


        return db.insert("item_sales_order_master", null, val);
    }

    public List<ItemModel> getItems() {
        ArrayList<ItemModel> list = new ArrayList<ItemModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM item_master", null);

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            ItemModel model = new ItemModel();
            model.item_id = c.getInt(0);
            model.cust_name = c.getString(1);
            model.bill_date = c.getString(2);
            model.carat_22 = c.getString(3);
            model.carat_18 = c.getString(4);
            model.item_name = c.getString(5);
            model.gross_weight = c.getString(6);
            model.less_weight = c.getString(6);
            model.net_weight = c.getString(6);
            model.ghat_weight = c.getString(6);
            model.selected_carat = c.getString(7);
            model.item_price = c.getString(8);
            model.making_charge = c.getString(9);
            model.selected_varient = c.getString(10);
            model.making_total = c.getString(11);
            model.other1 = c.getString(12);
            model.other2 = c.getString(13);
            model.total = c.getString(14);
            model.silver = c.getString(15);
            model.img = c.getString(16);
            model.tag_no = c.getString(17);
            model.gross_weight = c.getString(18);
            model.less_weight = c.getString(19);
            model.ghat_weight = c.getString(20);
            model.img_path = c.getString(21);
            model.isSelected = c.getInt(22);
            model.itemPcs = c.getInt(23);
            model.Touch = c.getInt(24);

            list.add(model);
        }

        c.close();
        db.close();
        return list;
    }

    public List<ItemSalesOrderModel> getItemsSalesOrder() {
        ArrayList<ItemSalesOrderModel> list = new ArrayList<ItemSalesOrderModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM item_sales_order_master", null);

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            ItemSalesOrderModel model = new ItemSalesOrderModel();
            model.item_id = c.getInt(0);
            model.delivery_date= c.getString(1);
            model.tag_no = c.getString(2);
            model.item_name = c.getString(3);
            model.item_design = c.getString(4);
            model.item_pieces = c.getInt(5);
            model.gross_weight = c.getString(6);
            model.less_weight = c.getString(7);
            model.net_weight = c.getString(8);
            model.selected_rate = c.getString(9);
            model.item_rate = c.getString(10);
            model.item_amount = c.getString(11);
            model.item_purity = c.getString(12);
            model.selected_varient = c.getString(13);
            model.making_charge = c.getString(14);
            model.making_total = c.getString(15);
            model.other1 = c.getString(16);
            model.other2 = c.getString(17);
            model.item_note=c.getString(18);
            model.img = c.getString(19);
            model.img_path = c.getString(20);
            model.isSelected = c.getInt(21);
            model.total=c.getString(22);


            list.add(model);
        }

        c.close();
        db.close();
        return list;
    }
    public int deleteItem(int item_id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] val = {item_id + ""};
        return db.delete("item_master", "item_id=?", val);
    }

    public int deleteItemSalesOrder(int item_id){
        SQLiteDatabase db=getWritableDatabase();
        String[] val={item_id+""};
        return  db.delete("item_sales_order_master","item_id=?",val);
    }

    public int deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("item_master", null, null);
    }

    public int deleteAllSalesOrder(){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete("item_sales_order_master",null,null);
    }

    public static boolean deleteDb() {
        File data = Environment.getDataDirectory();
        String currentDBPath = "/data/com.balaji.calculator1/databases/" + DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        Log.d("DBHelper", "deleteDb: Database deleted successfully!");

        return SQLiteDatabase.deleteDatabase(currentDB);
    }

    public void updateRecord(ItemModel itemModel, int item_id) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("cust_name", itemModel.cust_name);
        val.put("bill_date", itemModel.bill_date);
        val.put("carat_22", itemModel.carat_22);
        val.put("carat_18", itemModel.carat_18);
        val.put("item_name", itemModel.item_name);
        val.put("net_weight", itemModel.net_weight);
        val.put("selected_carat", itemModel.selected_carat);
        val.put("item_price", itemModel.item_price);
        val.put("making_charge", itemModel.making_charge);
        val.put("selected_varient", itemModel.selected_varient);
        val.put("making_total", itemModel.making_total);
        val.put("other1", itemModel.other1);
        val.put("other2", itemModel.other2);
        val.put("total", itemModel.total);
        val.put("silver", itemModel.silver);
        val.put("img", itemModel.img);
        val.put("tag_no", itemModel.tag_no);
        val.put("gross_weight", itemModel.gross_weight);
        val.put("less_weight", itemModel.less_weight);
        val.put("ghat_weight", itemModel.ghat_weight);
        val.put("isSelected", itemModel.isSelected);
        val.put("itemPcs", itemModel.itemPcs);
        val.put("touch", itemModel.Touch);

        db.update("item_master", val, "item_id=?", new String[]{item_id + ""});
        db.close();
    }

    public void updateRecordSalesOrder(ItemSalesOrderModel itemSalesOrderModel, int item_id) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("delivery_date", itemSalesOrderModel.delivery_date);
        val.put("item_name", itemSalesOrderModel.item_name);
        val.put("item_design",itemSalesOrderModel.item_design);
        val.put("gross_weight",itemSalesOrderModel.gross_weight);
        val.put("less_weight",itemSalesOrderModel.less_weight);
        val.put("net_weight", itemSalesOrderModel.net_weight);
        val.put("selected_rate",itemSalesOrderModel.selected_rate);
        val.put("item_rate",itemSalesOrderModel.item_rate);
        val.put("item_amount",itemSalesOrderModel.item_amount);
        val.put("item_purity",itemSalesOrderModel.item_purity);
        val.put("making_charge", itemSalesOrderModel.making_charge);
        val.put("selected_varient", itemSalesOrderModel.selected_varient);
        val.put("making_total", itemSalesOrderModel.making_total);
        val.put("other1", itemSalesOrderModel.other1);
        val.put("other2", itemSalesOrderModel.other2);
        val.put("total", itemSalesOrderModel.total);
        val.put("item_note",itemSalesOrderModel.item_note);
        val.put("img", itemSalesOrderModel.img);
        val.put("tag_no", itemSalesOrderModel.tag_no);
        val.put("img_path", itemSalesOrderModel.img_path);
        val.put("isSelected", 0);
        val.put("item_pieces", itemSalesOrderModel.item_pieces);

        db.update("item_sales_order_master", val, "item_id=?", new String[]{item_id + ""});
        db.close();
    }
}
