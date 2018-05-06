package com.example.bharat.joespizzeria.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.bharat.joespizzeria.models.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bharat on 1/30/18.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME= "JPDB.db";
    private static final int DB_VER=1;
    private int counterCarts;

    public Database(Context context) {

        super(context, DB_NAME,null, DB_VER);
    }

    public List<Order> getCarts(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect ={"ID","ProductName", "ProductId", "Quantity", "Price", "Discount" ,"Size","Crust","Image","Pstatus"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);

        Cursor c =qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();

        if (c.moveToFirst())
        {
            do {
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProductId")),
                       c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Size")),
                        c.getString(c.getColumnIndex("Crust")),
                        c.getString(c.getColumnIndex("Image")),
                        c.getString(c.getColumnIndex("Pstatus"))

                ));


            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query= String .format("INSERT INTO OrderDetail(ProductId, ProductName, Quantity, Price, Discount,Size,Crust,Image,Pstatus) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s'); ",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getSize(),
                order.getCrust(),
                order.getImage(),
                order.getPstatus());
        db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query= String .format("DELETE FROM OrderDetail");

        db.execSQL(query);
    }

    public int getCounterCarts() {
        int count =0;
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor =  db.rawQuery(query,null);

        if (cursor.moveToFirst())
        {
            do{
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }

        return count;
    }


    public void setCounterCarts(int counterCarts) {
        this.counterCarts = counterCarts;
    }

    public void updateCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("UPDATE OrderDetail SET Quantity= %s WHERE ID = %d",order.getQuantity(),order.getID());
        db.execSQL(query);
    }

    public void removeItem(int order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ID = %d",order);
        db.execSQL(query);
    }


}
