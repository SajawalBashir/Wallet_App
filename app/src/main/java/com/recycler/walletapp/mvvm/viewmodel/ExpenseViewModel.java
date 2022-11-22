package com.recycler.walletapp.mvvm.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.recycler.walletapp.mvvm.repository.models.Expense;
import com.recycler.walletapp.mvvm.repository.repo.ExpenseRepository;

import java.util.List;


public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;

    public ExpenseViewModel(Application application) {
        super(application);
        repository = new ExpenseRepository(application);

    }

    public void InsertNewExpense(Expense expense) {
        repository.InsertNewExpense(expense) ;
    }

    public void UpdateExpense(Expense expense) {
        repository.UpdateExpense(expense);
    }

    public void DeleteExpense(Expense expense) {
        repository.DeleteExpense(expense);
    }

    public LiveData<List<Expense>> GetExpenses(int id) {
        return repository.GetAllExpenses(id);
    }

}
