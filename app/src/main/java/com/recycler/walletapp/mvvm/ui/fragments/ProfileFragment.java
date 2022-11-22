package com.recycler.walletapp.mvvm.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.repository.models.User;
import com.recycler.walletapp.mvvm.ui.activities.MainActivity;
import com.recycler.walletapp.mvvm.ui.authentication.LoginActivity;
import com.recycler.walletapp.mvvm.ui.authentication.RegisterActivity;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;
import com.recycler.walletapp.utils.PreferencesManager;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText Name, Email, Password;
    private ProgressDialog progressDialog;
    private UserViewModel userViewModel;
    private Button UpdateButton;
    private int id;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initRef();
        GetCurrentUserId();
        PopulateFields();
        ClickListener();
    }

    private void GetCurrentUserId() {
    id = Integer.parseInt(PreferencesManager.getInstance(getContext()).getId());
    }

    private void PopulateFields() {
        userViewModel.GetCurrentUserById(String.valueOf(id))
                .observe(getViewLifecycleOwner(), CurrentUser -> {
                    if (CurrentUser != null) {
                    Email.setText(CurrentUser.getEmail());
                    Password.setText(CurrentUser.getPassword());
                    Name.setText(CurrentUser.getName());
                    }
                });


    }

    private void ClickListener() {
        UpdateButton.setOnClickListener(this);
    }

    private void initRef() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }


    private void init(View view) {
        Name = view.findViewById(R.id.nameText);
        Email = view.findViewById(R.id.EmailText);
        Password = view.findViewById(R.id.PasswordText);
        progressDialog = new ProgressDialog(getContext());
        UpdateButton = view.findViewById(R.id.UpdateButton);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.UpdateButton) {
            String name = Name.getText().toString();
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Name.setError("Field Required");
                Name.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Email.setError("Field Required");
                Email.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Password.setError("Field Required");
                Password.requestFocus();
                return;
            }


            User user = new User(name, email, password,userViewModel.GetCurrentUserIncome(id)
                    ,userViewModel.GetCurrentUserPreviousIncome(id));
            user.setId(id);
            userViewModel.UpdateUser(user);

        }
    }
}