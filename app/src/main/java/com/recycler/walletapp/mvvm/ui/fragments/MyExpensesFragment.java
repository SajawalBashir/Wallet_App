package com.recycler.walletapp.mvvm.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.repository.listeners.RecyclerItemTouchHelper;
import com.recycler.walletapp.mvvm.repository.models.Expense;
import com.recycler.walletapp.mvvm.ui.activities.AddExpenseActivity;
import com.recycler.walletapp.mvvm.ui.activities.UpdateExpenseActivity;
import com.recycler.walletapp.mvvm.ui.adapters.ExpenseAdapter;
import com.recycler.walletapp.mvvm.viewmodel.ExpenseViewModel;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;
import com.recycler.walletapp.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class MyExpensesFragment extends Fragment implements View.OnClickListener {


    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ExpenseViewModel expenseViewModel;
    private UserViewModel userViewModel;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private String userid;
    private TextView TotalIncomeView;
    private static final String TAG = "MyExpensesFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initRef();
        GetCurrentUserId();
        Observe();
        ClickListener();
        RecyclerViewDeleteListener();
    }

    private void RecyclerViewDeleteListener() {
        RecyclerItemTouchHelper recyclerItemTouchHelper = new RecyclerItemTouchHelper(getContext()) {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: LIST " + expenseList.size());
                Log.d(TAG, "onSwiped: POSITION " + viewHolder.getAdapterPosition());
                int CurrentIncome = userViewModel.GetCurrentUserIncome(Integer.parseInt(userid));
                userViewModel.InsertCurrentUserIncome(CurrentIncome + expenseList.get(viewHolder.getAdapterPosition()).getPrice(), Integer.parseInt(userid));
                userViewModel.InsertCurrentUserPreviousIncome(CurrentIncome, Integer.parseInt(userid));
                expenseViewModel.DeleteExpense(expenseAdapter.GetExpenseAt(viewHolder.getAdapterPosition()));

            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(recyclerItemTouchHelper);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void GetCurrentUserId() {
        userid = PreferencesManager.getInstance(getContext()).getId();
    }


    private void Observe() {
        expenseViewModel.GetExpenses(Integer.parseInt(userid)).observe(getViewLifecycleOwner(), Expenses -> {
            if (Expenses != null) {
                expenseList = Expenses;
                PopulateRecyclerView(expenseList);
            }
        });
        userViewModel.GetCurrentUserLiveIncome(Integer.parseInt(userid)).observe(getViewLifecycleOwner(),Income->{
            if(Income != null){
                TotalIncomeView.setText("Total Income: " + Income);
            }
        });
    }

    private void PopulateRecyclerView(List<Expense> expenseList) {
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList);
        expenseAdapter.notifyDataSetChanged();
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(expenseAdapter);


        expenseAdapter.SetItemClickListener((view, position) -> {
            Intent intent = new Intent(getActivity(), UpdateExpenseActivity.class);
            intent.putExtra("expense", expenseList.get(position));
            startActivity(intent);
        });

    }

    private void initRef() {
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void init(View view) {
        floatingActionButton = view.findViewById(R.id.AddExpenseButton);
        recyclerView = view.findViewById(R.id.ExpenseRecyclerView);
        expenseList = new ArrayList<>();
        TotalIncomeView = view.findViewById(R.id.TotalIncome);
    }

    private void ClickListener() {
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.AddExpenseButton) {
            startActivity(new Intent(getActivity(), AddExpenseActivity.class));

        }
    }
}