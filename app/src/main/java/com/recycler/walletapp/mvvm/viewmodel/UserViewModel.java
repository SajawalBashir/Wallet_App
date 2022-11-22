package com.recycler.walletapp.mvvm.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.recycler.walletapp.mvvm.repository.models.User;
import com.recycler.walletapp.mvvm.repository.repo.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;


    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }



    public void RegisterUser(User user) {
        repository.RegisterUser(user);
    }

    public LiveData<User> GetCurrentUser(String email) {
        return repository.GetCurrentUser(email);
    }

    public Integer GetCurrentUserIncome(int id) {
        return repository.GetCurrentUserIncome(id);
    }

    public LiveData<Integer> GetCurrentUserLiveIncome(int id) {
        return repository.GetCurrentUserLiveIncome(id);
    }

    public Integer GetCurrentUserPreviousIncome(int id) {
        return repository.GetCurrentUserPreviousIncome(id);
    }


    public void InsertCurrentUserIncome(int income, int id) {
        repository.InsertCurrentUserIncome(income,id);
    }

    public void UpdateUser(User user) {
        repository.UpdateUser(user);
    }

    public void InsertCurrentUserPreviousIncome(int income, int id) {
        repository.InsertCurrentUserPreviousIncome(income,id);
    }


    public LiveData<User> GetCurrentUserById(String id) {
        return repository.GetCurrentUserById(id);
    }

    public LiveData<User> LoginUser(String email,String password) {
        return repository.LoginUser(email,password);
    }

    public LiveData<Boolean> CheckEmailExists(String email) {
        return repository.CheckEmailExists(email);
    }



}
