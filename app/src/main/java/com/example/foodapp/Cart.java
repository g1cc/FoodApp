package com.example.foodapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Domain.CartDomain;
import com.example.foodapp.Helper.CartManager;
import com.example.foodapp.databinding.FragmentCartBinding;
import com.example.foodapp.iface.ChangeNumberItemsListener;

import java.io.IOException;
import java.util.ArrayList;

public class Cart extends Fragment {
    private CartViewModel mViewModel;
    RecyclerView cartRecView;
    FragmentCartBinding binding;
    TextView emptyTitle, sumTitle, costTitle, deliveryTitle;

    Button completeButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String query;
    RecyclerView.Adapter adapter;

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

    public int summ, delivery, finalSumm;

    public int exportSumm, exportDelivery, exportFinalSumm;

    CartManager cartManager = new CartManager(getContext());

    TextView foodSumCart, deliverySumCart, sumCart;

    ArrayList<CartDomain> cartDomains = new ArrayList<>();

    public static Cart newInstance() {
        return new Cart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        cartRecView = binding.cartRecView;

        emptyTitle = binding.emptyTitle;
        sumTitle = binding.sumTitle;
        costTitle = binding.costTitle;
        deliveryTitle = binding.deliveryTitle;

        foodSumCart = binding.foodSumCart;
        deliverySumCart = binding.deliverySumCart;
        sumCart = binding.sumCart;
        completeButton = binding.completeButton;

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.MainFragmentConView, new order_complete())
                        .addToBackStack(null)
                        .commit();
            }
        });

        databaseHelper = new DatabaseHelper(getContext());
        try {
            databaseHelper.updateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = databaseHelper.open();


        setCartRecView();

        calculateCart();

        exportSumm = summ;
        exportDelivery = delivery;
        exportFinalSumm = finalSumm;

        return view;
    }

    public void setCartRecView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cartRecView.setLayoutManager(linearLayoutManager);

        query = "select Меню.Код as 'Код', Меню.Название as 'Блюдо', Меню.Картинка as 'Картинка', Меню.Цена as 'Цена', Меню.Состав as 'Состав', Корзина.Количество\n" +
                "from Меню inner join Корзина on Меню.Код = Корзина.Код_Меню\n" +
                "where Корзина.Код_Клиенты = " + Auth.id;

        cursor = db.rawQuery(query, null);

        if (cartDomains.isEmpty()) {
            while (cursor.moveToNext()) {
                listFill();
                cartDomains.add(new CartDomain(foodName, foodImage, foodIng, Integer.parseInt(foodPrice), Integer.parseInt(foodQTY), Integer.parseInt(foodID)));
                summ += Integer.parseInt(foodPrice) * Integer.parseInt(foodQTY);
            }
        }

        emptyChecker();

        adapter = new CartAdapter(cartDomains, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCart();
            }
        }, getContext());

        cartRecView.setAdapter(adapter);
    }

    private void calculateCart() {
        summ = Math.round(getTotalFee());

        emptyChecker();

        if (summ < 1500)
            delivery = 149;
        if (summ >= 1500)
            delivery = 79;
        if (summ >= 3000)
            delivery = 0;

        foodSumCart.setText(String.valueOf(summ) + " ₽");
        deliverySumCart.setText(String.valueOf(delivery) + " ₽");
        finalSumm = summ + delivery;

        sumCart.setText(String.valueOf(finalSumm) + " ₽");
    }

    public int getTotalFee() {
        int price = 0;
        for (int i = 0; i < cartDomains.size(); i++) {
            price = price + (cartDomains.get(i).getPrice() * cartDomains.get(i).getFoodQTY());
        }
        return price;
    }

    private void listFill() {
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

    private void emptyChecker()
    {
        if (cartDomains.isEmpty())
        {
            cartRecView.setVisibility(View.INVISIBLE);
            completeButton.setVisibility(View.INVISIBLE);
            foodSumCart.setVisibility(View.INVISIBLE);
            deliverySumCart.setVisibility(View.INVISIBLE);
            sumCart.setVisibility(View.INVISIBLE);
            sumTitle.setVisibility(View.INVISIBLE);
            costTitle.setVisibility(View.INVISIBLE);
            deliveryTitle.setVisibility(View.INVISIBLE);
            emptyTitle.setVisibility(View.VISIBLE);
        }
        else
            emptyTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        // TODO: Use the ViewModel
    }

}