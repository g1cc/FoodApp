package com.example.foodapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "FoodDataBase.db";
    private static final int DB_VERSION = 1; // версия базы данных
    static final String TABLE = "Клиенты"; // название таблицы в бд
    static final String tableOrders = "Заказы";
    static final String ordersColumnSumOrder = "[Стоимость заказа]";
    static final String ordersColumnSumDelivery = "[Стоимость доставки]";
    static final String ordersColumnTotal = "Итого";
    static final String ordersColumnStatus = "Статус";
    static final String ordersColumnDateOrder = "[Дата заказа]";
    static final String ordersColumnDateComplete = "[Дата выполнения]";
    static final String ordersColumnClientID = "КодКлиента";
    static final String ordersColumnStreet = "Улица";
    static final String ordersColumnHouse = "Дом";
    static final String ordersColumnFlat = "Квартира";
    static final String tableClients = "Клиенты";
    static final String clientsID = "Код";
    static final String clientsName = "Имя";
    static final String clientsSurname = "Фамилия";
    static final String clientsPhone = "[Номер телефона]";
    static final String clientsPassword = "Пароль";
    static final String clientsBirthday = "[Дата рождения]";
    static final String clientsMail = "[Электронный адрес]";
    private Context myContext;

    public static boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)
    {
        if (newVersion > oldVersion)
        {
            mNeedUpdate = true;
        }
    }

    public void updateDataBase() throws IOException {

        if (mNeedUpdate)
        {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        try(InputStream myInput = myContext.getAssets().open(DB_NAME);
            // Открываем пустую бд
            OutputStream myOutput = new FileOutputStream(DB_PATH)) {

            // побайтово копируем данные
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    public void create_db(){
        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException
    {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }


}