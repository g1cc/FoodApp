package com.example.foodapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Auth;
import com.example.foodapp.DatabaseHelper;
import com.example.foodapp.Domain.CartDomain;
import com.example.foodapp.iface.ChangeNumberItemsListener;

import java.io.IOException;
import java.util.ArrayList;

public class CartManager
{
    private Context context;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String query;
    RecyclerView.Adapter adapter;
    ContentValues cv = new ContentValues();

    String foodName;
    String foodPrice;
    String foodIng;
    String foodImage;
    String foodQTY;
    String foodID;

    int foodNameID;
    int foodPriceID;
    int foodIngID;
    int foodImageID;
    int foodQTYID;
    int foodidID;

    public CartManager(Context context) {
        this.context = context;
    }

    public void minusNumberFood(ArrayList<CartDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        initDB(context.getApplicationContext());
        if (listFood.get(position).getFoodQTY() == 1)
        {
            db.delete("Корзина", "Код_Меню = " + listFood.get(position).getID() + " and Код_Клиенты = " + Auth.id, null);
            listFood.remove(position);
        }

        else
        {
            cv.put("Количество", String.valueOf(listFood.get(position).getFoodQTY() - 1));
            db.update("Корзина", cv, "Код_Меню = " + listFood.get(position).getID() + " and Код_Клиенты = " + Auth.id, null);
            listFood.get(position).setFoodQTY(listFood.get(position).getFoodQTY() - 1);
        }

        changeNumberItemsListener.changed();
    }

    public void plusNumberFood(ArrayList<CartDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        initDB(context.getApplicationContext());
        cv.put("Количество", String.valueOf(listFood.get(position).getFoodQTY() + 1));
        db.update("Корзина", cv, "Код_Меню = " + listFood.get(position).getID() + " and Код_Клиенты = " + Auth.id, null);
        listFood.get(position).setFoodQTY(listFood.get(position).getFoodQTY() + 1);
        changeNumberItemsListener.changed();
    }

    public void initDB(Context context)
    {
        databaseHelper = new DatabaseHelper(context);
        try
        {
            databaseHelper.updateDataBase();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db = databaseHelper.open();
    }

    private void listFill()
    {
        foodNameID = cursor.getColumnIndex("Блюдо");
        foodPriceID = cursor.getColumnIndex("Цена");
        foodImageID = cursor.getColumnIndex("Картинка");
        foodIngID = cursor.getColumnIndex("Состав");
        foodQTYID = cursor.getColumnIndex("Количество");
        foodidID = cursor.getColumnIndex("Код");

        foodName = cursor.getString(foodNameID);
        foodIng = cursor.getString(foodIngID);
        foodPrice = cursor.getString(foodPriceID);
        foodImage = cursor.getString(foodImageID);
        foodQTY = cursor.getString(foodQTYID);
        foodID = cursor.getString(foodidID);
    }
}
