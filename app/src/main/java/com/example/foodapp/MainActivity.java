package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.foodapp.databinding.FragmentAuthBinding;
import com.example.foodapp.databinding.FragmentRegistrationBinding;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    FragmentAuthBinding authBinding;
    FragmentRegistrationBinding regBinding;
    TextView authTV, regTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authBinding = FragmentAuthBinding.inflate(getLayoutInflater());
        regBinding = FragmentRegistrationBinding.inflate(getLayoutInflater());

        frameLayout = findViewById(R.id.MainFragmentConView);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.MainFragmentConView, new Auth())
                .commit();
    }
}