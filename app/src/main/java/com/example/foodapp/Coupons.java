package com.example.foodapp;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Adapter.CouponsAdapter;
import com.example.foodapp.Domain.CouponsDomain;
import com.example.foodapp.databinding.FragmentCouponsBinding;

import java.io.IOException;
import java.util.ArrayList;

public class Coupons extends Fragment {

    private CouponsViewModel mViewModel;
    FragmentCouponsBinding binding;

    private RecyclerView.Adapter adapter;
    private RecyclerView couponsRecView;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    String query;
    Cursor cursor;

    public static Coupons newInstance() {
        return new Coupons();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCouponsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        couponsRecView = binding.couponsRecView;

        databaseHelper = new DatabaseHelper(getContext());
        try {
            databaseHelper.updateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = databaseHelper.open();

        setCouponRecyclerView();

        return view;
    }

    private void setCouponRecyclerView()
    {
        couponsRecView = binding.couponsRecView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        couponsRecView.setLayoutManager(linearLayoutManager);

        query = "select Меню.Название as 'Купон', Меню.Цена as 'Цена', Меню.[Новая цена] as НоваяЦена, Меню.Описание\n" +
                "from Меню inner join Категории on Меню.Категория = Категории.Код\n" +
                "where Категории.Название = 'Купоны'";

        String title, oldPrice, newPrice, coupon;
        int titleID, oldPriceID, newPriceID, couponID;

        cursor = db.rawQuery(query, null);

        ArrayList<CouponsDomain> couponDomains = new ArrayList<>();

        while (cursor.moveToNext())
        {
            titleID = cursor.getColumnIndex("Купон");
            oldPriceID = cursor.getColumnIndex("Цена");
            newPriceID = cursor.getColumnIndex("НоваяЦена");
            couponID = cursor.getColumnIndex("Описание");

            title = cursor.getString(titleID);
            oldPrice = cursor.getString(oldPriceID);
            newPrice = cursor.getString(newPriceID);
            coupon = cursor.getString(couponID);

            couponDomains.add(new CouponsDomain(title, coupon, Integer.parseInt(oldPrice), Integer.parseInt(newPrice)));
        }

        adapter = new CouponsAdapter(couponDomains);
        couponsRecView.setAdapter(adapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CouponsViewModel.class);
        // TODO: Use the ViewModel
    }

}