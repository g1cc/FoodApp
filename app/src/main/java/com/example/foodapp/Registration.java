package com.example.foodapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.FragmentRegistrationBinding;

import java.io.IOException;

public class Registration extends Fragment {

    private RegistrationViewModel mViewModel;

    TextView textView;
    FragmentRegistrationBinding binding;
    EditText regPhoneET, regNameET, regPassET, regMailET;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    String phone, password, name, mail;
    Button regButton;

    public static Registration newInstance() {
        return new Registration();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        regPhoneET = binding.regPhoneET;
        regButton = binding.regButton;
        regNameET = binding.regNameET;
        regPassET = binding.regPasswordET;
        regMailET = binding.regMailET;

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

        regPhoneET.addTextChangedListener(new Registration.MaskWatcher("## ### ###-##-##"));

        regPhoneET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (regPhoneET.length() == 0)
                        regPhoneET.setText("+7");
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = regPhoneET.getText().toString();

                password = regPassET.getText().toString();
                name = regNameET.getText().toString();
                mail = regMailET.getText().toString();

                if (phone.length() == 16 && password.length() >= 6 && password.length() <= 22)
                {

                    ContentValues cv = new ContentValues();

                    cv.put("Имя", name);
                    cv.put("[Электронный адрес]", mail);
                    cv.put("Пароль", password);
                    cv.put("[Номер телефона]", phone);
                    cv.put("Фамилия", "NULL");
                    cv.put("[Дата рождения]", "NULL");

                    db.insert("Клиенты", null, cv);
                    db.close();

                    Toast.makeText(getContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    assert fragmentManager != null;
                    fragmentManager.popBackStack();
                }
                else
                {
                    Toast.makeText(getContext(), "Проверьте правильность номера телефона. Минимальная длина пароля - 6 символов, максимальная - 22 символа", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public class MaskWatcher implements TextWatcher {
        private boolean isRunning = false;
        private boolean isDeleting = false;
        private final String mask;

        public MaskWatcher(String mask) {
            this.mask = mask;
        }

        public Auth.MaskWatcher buildCpf() {
            return new Auth.MaskWatcher("###.###.###-##");
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

            if (regPhoneET.length() > 1) {
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
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        // TODO: Use the ViewModel
    }

}