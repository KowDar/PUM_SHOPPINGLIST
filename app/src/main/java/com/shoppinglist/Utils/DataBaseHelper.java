package com.shoppinglist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.shoppinglist.Model.ShoppingListModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper
{
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "SHOPPINGLIST_DATABASE";
    private static final String TABLE_NAME= "SHOPPINGLIST_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "PROCUCT";
    private static final String COL_3 = "STATUS";
    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , PROCUCT TEXT , STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void insertProduct(ShoppingListModel model)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getProduct());
        values.put(COL_3 , 0);
        db.insert(TABLE_NAME , null , values);
    }

    public void updateProduct(int id, String product)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , product);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void deleteProduct(int id)
    {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }

    public List<ShoppingListModel> getAllProducts()
    {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ShoppingListModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME , null , null , null , null , null , null );
            if(cursor != null)
            {
                if(cursor.moveToFirst()){
                    do{
                        ShoppingListModel product = new ShoppingListModel();
                        product.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        product.setProduct(cursor.getString(cursor.getColumnIndex(COL_2)));
                        product.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(product);


                    }while(cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

}
