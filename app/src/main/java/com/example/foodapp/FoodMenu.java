package com.example.foodapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.FoodAdapter;
import com.example.foodapp.Adapter.PopularAdapter;
import com.example.foodapp.Domain.FoodDomain;
import com.example.foodapp.databinding.FragmentFoodMenuBinding;

import java.io.IOException;
import java.util.ArrayList;

public class FoodMenu extends Fragment {

    private FoodMenuViewModel mViewModel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FragmentFoodMenuBinding binding;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String query, category;

    String foodName;
    String foodPrice;
    String foodIng;
    String foodImage;
    String foodDescription;
    String foodEnergy;
    String foodProtein;
    String foodFats;
    String foodCarbon;
    String foodWeight;
    String foodID;

    int foodNameID;
    int foodPriceID;
    int foodIngID;
    int foodImageID;

    int foodDescriptionID;
    int foodEnergyID;
    int foodProteinID;
    int foodFatsID;
    int foodCarbonID;
    int foodWeightID;
    int foodidID;

    public static FoodMenu newInstance() {
        return new FoodMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

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

        setPizzaRecView();
        setPopularRecView();
        setBurgerRecView();
        setSnacksRecView();
        setDessertRecView();
        setDrinksRecView();
        setSauceRecView();

        return view;
    }

    public void setPopularRecView()
    {
        category = "Пицца";
        recyclerView = binding.popularRecView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<FoodDomain> foodDomains = new ArrayList<>();

        query = "select Меню.Код as 'Код', Меню.Название as 'Блюдо', Меню.Картинка as 'Картинка', Меню.Цена as 'Цена', Меню.Состав as 'Состав', Категории.Название as 'Категория', Меню.Белки, Меню.Жиры, Меню.Углеводы, Меню.Описание, Меню.Вес, Меню.[Энергетическая ценность]\n" +
                "from Меню inner join Категории on Меню.Категория = Категории.Код\n" +
                "where Категории.Название = '" + category + "'";

        cursor = db.rawQuery(query, null);

        while (cursor.moveToNext())
        {
            listFill();
            foodDomains.add(new FoodDomain(foodName, foodImage, foodIng, foodDescription, Integer.parseInt(foodPrice), Integer.parseInt(foodEnergy), Double.parseDouble(foodFats), Double.parseDouble(foodCarbon), Double.parseDouble(foodProtein), Integer.parseInt(foodWeight), Integer.parseInt(foodID)));
        }

        adapter = new PopularAdapter(foodDomains);
        recyclerView.setAdapter(adapter);
    }

    public void setPizzaRecView()
    {
        recyclerView = binding.pizzaRecView;
        category = "Пицца";
        recViewHelper();
    }

    public void setBurgerRecView()
    {
        recyclerView = binding.burgerRecView;
        category = "Бургеры";
        recViewHelper();
    }

    public void setSnacksRecView()
    {
        recyclerView = binding.snacksRecView;
        category = "Закуски";
        recViewHelper();
    }

    public void setDessertRecView()
    {
        recyclerView = binding.dessertRecView;
        category = "Десерты";
        recViewHelper();
    }

    public void setDrinksRecView()
    {
        recyclerView = binding.drinksRecView;
        category = "Напитки";
        recViewHelper();
    }

    public void setSauceRecView()
    {
        recyclerView = binding.sauceRecView;
        category = "Соусы";
        recViewHelper();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FoodMenuViewModel.class);
        // TODO: Use the ViewModel
    }

    private void listFill()
    {
        foodNameID = cursor.getColumnIndex("Блюдо");
        foodPriceID = cursor.getColumnIndex("Цена");
        foodImageID = cursor.getColumnIndex("Картинка");
        foodIngID = cursor.getColumnIndex("Состав");
        foodDescriptionID = cursor.getColumnIndex("Описание");
        foodProteinID = cursor.getColumnIndex("Белки");
        foodFatsID = cursor.getColumnIndex("Жиры");
        foodCarbonID = cursor.getColumnIndex("Углеводы");
        foodEnergyID = cursor.getColumnIndex("Энергетическая ценность");
        foodWeightID = cursor.getColumnIndex("Вес");
        foodidID = cursor.getColumnIndex("Код");

        foodName = cursor.getString(foodNameID);
        foodIng = cursor.getString(foodIngID);
        foodPrice = cursor.getString(foodPriceID);
        foodImage = cursor.getString(foodImageID);
        foodProtein = cursor.getString(foodProteinID);
        foodFats = cursor.getString(foodFatsID);
        foodCarbon = cursor.getString(foodCarbonID);
        foodWeight = cursor.getString(foodWeightID);
        foodDescription = cursor.getString(foodDescriptionID);
        foodEnergy = cursor.getString(foodEnergyID);
        foodID = cursor.getString(foodidID);
    }

    private void recViewHelper()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodDomains = new ArrayList<>();

        recyclerView.setNestedScrollingEnabled(false);

        query = "select Меню.Код as 'Код', Меню.Название as 'Блюдо', Меню.Картинка as 'Картинка', Меню.Цена as 'Цена', Меню.Состав as 'Состав', Категории.Название as 'Категория', Меню.Белки, Меню.Жиры, Меню.Углеводы, Меню.Описание, Меню.Вес, Меню.[Энергетическая ценность]\n" +
                "from Меню inner join Категории on Меню.Категория = Категории.Код\n" +
                "where Категории.Название = '" + category + "'";

        cursor = db.rawQuery(query, null);

        while (cursor.moveToNext())
        {
            listFill();
            foodDomains.add(new FoodDomain(foodName, foodImage, foodIng, foodDescription, Integer.parseInt(foodPrice), Integer.parseInt(foodEnergy), Double.parseDouble(foodFats), Double.parseDouble(foodCarbon), Double.parseDouble(foodProtein), Integer.parseInt(foodWeight), Integer.parseInt(foodID)));
        }

        adapter = new FoodAdapter(foodDomains);
        recyclerView.setAdapter(adapter);
    }

}