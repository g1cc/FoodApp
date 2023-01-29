package com.example.foodapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.FragmentProfileDataEditBinding;

public class profile_data_edit extends Fragment {

    private ProfileDataEditViewModel mViewModel;
    FragmentProfileDataEditBinding binding;
    static EditText profileDEphone;
    EditText profileDEpass, profileDEmail, profileDEname;

    public static profile_data_edit newInstance() {
        return new profile_data_edit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileDataEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        profileDEphone = binding.profileDEphone;
        profileDEpass = binding.profileDEpass;
        profileDEmail = binding.profileDEmail;
        profileDEname = binding.profileDEname;

        profileDEphone.addTextChangedListener(new Auth.MaskWatcher("## ### ###-##-##"));

        profileDEphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (profileDEphone.length() == 0)
                        profileDEphone.setText("+7");
                }
            }
        });

        profileDEphone.setText(Auth.phone);
        profileDEpass.setText(Auth.passwordStr);
        profileDEname.setText(Auth.name);
        profileDEmail.setText(Auth.mail);

        return view;
    }

    public static class MaskWatcher implements TextWatcher {
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

            if (profileDEphone.length() > 1) {
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
        mViewModel = new ViewModelProvider(this).get(ProfileDataEditViewModel.class);
        // TODO: Use the ViewModel
    }

}