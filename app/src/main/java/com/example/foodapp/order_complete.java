package com.example.foodapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.Domain.CartDomain;
import com.example.foodapp.databinding.FragmentOrderCompleteBinding;

import java.io.IOException;
import java.util.ArrayList;

public class order_complete extends Fragment {

    private OrderCompleteViewModel mViewModel;
    FragmentOrderCompleteBinding binding;

    Button orderCompleButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String query;

    EditText streetEditText, houseEditText, flatEditText;

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

    Cart cart = new Cart();

    public int summ, delivery, finalSumm;

    ArrayList<CartDomain> cartDomains = new ArrayList<>();

    ContentValues cv = new ContentValues();

    public static order_complete newInstance() {
        return new order_complete();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderCompleteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        streetEditText = binding.streetEditText;
        houseEditText = binding.houseEditText;
        flatEditText = binding.flatEditText;

        databaseHelper = new DatabaseHelper(getContext());
        try
        {
            databaseHelper.updateDataBase();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db = databaseHelper.open();

        orderCompleButton = binding.orderCompleButton;

        getCartList();

        calculateCart();

        orderCompleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cvSumOrder = String.valueOf(summ);
                String cvDelivery = String.valueOf(delivery);
                String cvTotal = String.valueOf(finalSumm);
                String cvStatus = "Готовится";
                String cvDateOrder = "2022-12-26";
                String cvDateComplete = "2022-12-26";
                String cvClientID = Auth.id;
                String cvStreet = String.valueOf(streetEditText.getText());
                String cvHouse = String.valueOf(houseEditText.getText());
                String cvFlat = String.valueOf(flatEditText.getText());

                cv.put(DatabaseHelper.ordersColumnSumOrder, cvSumOrder);
                cv.put(DatabaseHelper.ordersColumnSumDelivery, cvDelivery);
                cv.put(DatabaseHelper.ordersColumnTotal, cvTotal);
                cv.put(DatabaseHelper.ordersColumnStatus, cvStatus);
                cv.put(DatabaseHelper.ordersColumnDateOrder, cvDateOrder);
                cv.put(DatabaseHelper.ordersColumnDateComplete, cvDateComplete);
                cv.put(DatabaseHelper.ordersColumnClientID, cvClientID);
                cv.put(DatabaseHelper.ordersColumnStreet, cvStreet);
                cv.put(DatabaseHelper.ordersColumnHouse, cvHouse);
                cv.put(DatabaseHelper.ordersColumnFlat, cvFlat);

                db.insert(DatabaseHelper.tableOrders, null, cv);

            }
        });

        return view;
    }

    public void getCartList()
    {
        query = "select Меню.Код as 'Код', Меню.Название as 'Блюдо', Меню.Картинка as 'Картинка', Меню.Цена as 'Цена', Меню.Состав as 'Состав', Корзина.Количество\n" +
                "from Меню inner join Корзина on Меню.Код = Корзина.Код_Меню\n" +
                "where Корзина.Код_Клиенты = " + Auth.id;

        cursor = db.rawQuery(query, null);

        while (cursor.moveToNext())
        {
            listFill();
            cartDomains.add(new CartDomain(foodName, foodImage, foodIng, Integer.parseInt(foodPrice), Integer.parseInt(foodQTY), Integer.parseInt(foodID)));
        }


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

    private void calculateCart()
    {
        summ = Math.round(getTotalFee());

        if (summ < 1500)
            delivery = 149;
        if (summ >= 1500)
            delivery = 79;
        if (summ >= 3000)
            delivery = 0;

        finalSumm = summ + delivery;
    }

    public int getTotalFee() {
        int price = 0;
        for (int i = 0; i < cartDomains.size(); i++) {
            price = price + (cartDomains.get(i).getPrice() * cartDomains.get(i).getFoodQTY());
        }
        return price;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderCompleteViewModel.class);
        // TODO: Use the ViewModel
    }

}