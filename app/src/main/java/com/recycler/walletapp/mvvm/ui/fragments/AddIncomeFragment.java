package com.recycler.walletapp.mvvm.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.recycler.walletapp.R;
import com.recycler.walletapp.utils.PreferencesManager;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;


public class AddIncomeFragment extends Fragment implements View.OnClickListener {

    private EditText Income;
    private MaterialButton AddIncomeButton;
    private UserViewModel userViewModel;
    private String id;
    private int currentincome;

    public AddIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initRef();
        GetCurrentUserId();
        Observe();
        ClickListener();
    }

    private void GetCurrentUserId() {
        id = PreferencesManager.getInstance(getContext()).getId();
    }

    private void Observe() {
        currentincome = userViewModel.GetCurrentUserIncome(Integer.parseInt(id));
        Income.setText(String.valueOf(currentincome));
    }

    private void initRef() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void ClickListener() {
        AddIncomeButton.setOnClickListener(this);
    }

    private void init(View view) {
        AddIncomeButton = view.findViewById(R.id.AddIncomeButton);
        Income = view.findViewById(R.id.IncomeText);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.AddIncomeButton) {
            String income = Income.getText().toString();
            if (TextUtils.isEmpty(income)) {
                Income.setError("Field Required");
                Income.requestFocus();
                return;
            }
            userViewModel.InsertCurrentUserIncome(Integer.parseInt(income), Integer.parseInt(id));
            Toast.makeText(getContext(), "Income Added", Toast.LENGTH_SHORT).show();
        }
    }
}