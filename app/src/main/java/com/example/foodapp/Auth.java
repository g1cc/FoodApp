package com.example.foodapp;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapp.databinding.FragmentAuthBinding;

import java.io.IOException;

public class Auth extends Fragment {

    private AuthViewModel mViewModel;
    FragmentAuthBinding binding;
    static EditText authPhoneET, authPassET;
    Button authLogInButton, authRegButton;
    String login, password;

    public static String id, name, mail, phone, passwordStr, birthday;
    int idID, nameID, mailID, phoneID, passwordStrID, birthdayID;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    public static Auth newInstance() {
        return new Auth();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        authPhoneET = binding.authPhone;
        authPassET = binding.authPassword;
        authLogInButton = binding.authLogInButton;
        authRegButton = binding.authRegButton;

        databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.create_db();
        try
        {
            databaseHelper.updateDataBase();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db = databaseHelper.open();

        authRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.MainFragmentConView, new Registration())
                        .addToBackStack(null)
                        .commit();
            }
        });

        authLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = authPhoneET.getText().toString();
                password = authPassET.getText().toString();

                if (login.length() == 16 || password.length() >= 6 || password.length() <= 22) {

                    String query = "SELECT Код, Имя, Пароль, [Дата рождения] as 'Дата рождения', [Номер телефона] as 'Номер телефона', [Электронный адрес] as 'Электронный адрес'\n" +
                            "FROM Клиенты WHERE [Номер телефона] = '" + login + "' and Пароль = '" + password + "'";

                    cursor = db.rawQuery(query, null);

                    if (cursor.moveToNext()) {

                        idID = cursor.getColumnIndex("Код");
                        nameID = cursor.getColumnIndex("Имя");
                        mailID = cursor.getColumnIndex("Электронный адрес");
                        phoneID = cursor.getColumnIndex("Номер телефона");
                        birthdayID = cursor.getColumnIndex("Дата рождения");
                        passwordStrID = cursor.getColumnIndex("Пароль");

                        id = cursor.getString(idID);
                        name = cursor.getString(nameID);
                        mail = cursor.getString(mailID);
                        phone = cursor.getString(phoneID);
                        birthday = cursor.getString(birthdayID);
                        passwordStr = cursor.getString(passwordStrID);

                        db.close();

                        FragmentManager fragmentManager = getFragmentManager();
                        assert fragmentManager != null;
                        fragmentManager.beginTransaction()
                                .replace(R.id.MainFragmentConView, new NavigationFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        authPhoneET.addTextChangedListener(new MaskWatcher("## ### ###-##-##"));

        authPhoneET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (authPhoneET.length() == 0)
                        authPhoneET.setText("+7");
                }
            }
        });

        return view;
    }

    public static class MaskWatcher implements TextWatcher {
        private boolean isRunning = false;
        private boolean isDeleting = false;
        private final String mask;

        public MaskWatcher(String mask) {
            this.mask = mask;
        }

        public MaskWatcher buildCpf() {
            return new MaskWatcher("###.###.###-##");
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            isDeleting = count > after;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (isRunning || isDeleting) {
                return;
            }
            isRunning = true;

            if (authPhoneET.length() > 1) {
                int editableLength = editable.length();
                if (editableLength < mask.length()) {
                    if (mask.charAt(editableLength) != '#') {
                        editable.append(mask.charAt(editableLength));
                    } else if (mask.charAt(editableLength - 1) != '#') {
                        editable.insert(editableLength - 1, mask, editableLength - 1, editableLength);
                    }
                }
            }

            isRunning = false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        // TODO: Use the ViewModel
    }

}