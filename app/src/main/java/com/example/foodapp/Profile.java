package com.example.foodapp;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.FragmentProfileBinding;

import java.io.IOException;

public class Profile extends Fragment {

    private ProfileViewModel mViewModel;
    FragmentProfileBinding binding;
    TextView nameProfile, mailProfile, phoneProfile, myProfilePassword, myProfileBirthday;
    Button myOrdersButton;
    ImageView exitButton;
    ImageButton profileEditButton, vkButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String query;

    public static Profile newInstance() {
        return new Profile();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
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

        profileEditButton = binding.profileEditButton;

        phoneProfile = binding.phoneProfile;
        nameProfile = binding.nameProfile;
        mailProfile = binding.mailProfile;
        vkButton = binding.vkButton;

        vkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://vk.com/ggiicc");
            }
        });

        myOrdersButton = binding.ordersButton;
        exitButton = binding.exitButton;

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                finishAffinity(getActivity());
            }
        });

        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.MainFragmentConView, new profile_data_edit())
                        .addToBackStack(null)
                        .commit();
            }
        });

        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.MainFragmentConView, new my_orders())
                        .addToBackStack(null)
                        .commit();
            }
        });

        phoneProfile.setText(Auth.phone);
        nameProfile.setText("Привет, " + Auth.name);
        mailProfile.setText(Auth.mail);

        return view;
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}