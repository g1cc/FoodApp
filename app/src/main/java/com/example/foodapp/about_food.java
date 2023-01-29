package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Domain.FoodDomain;

import java.io.IOException;
import java.util.ArrayList;

public class about_food extends AppCompatActivity {

    static ArrayList<FoodDomain> abFoodDomain = new ArrayList<>();

    private FoodDomain foodDomain;
    TextView abName, abDescription, abIng, abEnergy, abProtein, abCarbon, abFats;
    ImageView abImage;
    Button abAddBut;

    String query;
    Cursor cursor, checkCursor;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    String id;
    int idID = 0;

    public static String cartTitle, cartDescription, cartImage, cartIng;
    public static int cartPrice, cartWeight, cartEnergy;
    public static double cartFats, cartProtein, cartCarbon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_food);

        foodDomain = (FoodDomain) getIntent().getSerializableExtra("object");

        init();
        setText();

        abAddBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = foodDomain.getTitle();

                databaseHelper = new DatabaseHelper(getApplicationContext());
                try
                {
                    databaseHelper.updateDataBase();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                db = databaseHelper.open();

                query = "SELECT Код from Меню where Меню.Название = '" + name + "'";

                cursor = db.rawQuery(query, null);

                while (cursor.moveToNext())
                {
                    idID = cursor.getColumnIndex("Код");
                    id = cursor.getString(idID);

                }

                query = "SELECT * FROM Корзина where Код_Меню = " + id + " and Код_Клиенты = " + Auth.id;

                checkCursor = db.rawQuery(query, null);

                if (checkCursor.moveToNext())
                {
                    Toast.makeText(getApplicationContext(), "Уже в корзине!", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    ContentValues cv = new ContentValues();

                    cv.put("Код_Клиенты", Auth.id);
                    cv.put("Код_Меню", id);
                    cv.put("Количество", 1);

                    db.insert("Корзина", null, cv);
                    db.close();

                    Toast.makeText(getApplicationContext(), "Добавлено в корзину!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void init()
    {
        abName = findViewById(R.id.titleAF);
        abIng = findViewById(R.id.componentsAF);
        abDescription = findViewById(R.id.descriptionAF);
        abEnergy = findViewById(R.id.abFoodEnergy);
        abCarbon = findViewById(R.id.abCarbon);
        abFats = findViewById(R.id.abFats);
        abProtein = findViewById(R.id.abProtein);
        abImage = findViewById(R.id.imageAF);
        abAddBut = findViewById(R.id.abAddButton);
    }

    private void setText()
    {
        abName.setText(foodDomain.getTitle());
        abIng.setText(foodDomain.getIngredients());
        abDescription.setText(foodDomain.getDescription());
        abCarbon.setText(String.valueOf(foodDomain.getCarbon()));
        abProtein.setText(String.valueOf(foodDomain.getProtein()));
        abFats.setText(String.valueOf(foodDomain.getFats()));
        abEnergy.setText(String.valueOf(Integer.valueOf(foodDomain.getEnergy())));
        int drawableResourceId = this.getResources().getIdentifier(foodDomain.getImage(), "drawable", this.getPackageName());
        abImage.setImageResource(drawableResourceId);
        abAddBut.setText("Добавить в корзину за " + foodDomain.getPrice() + " ₽");
    }
}