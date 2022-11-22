package com.recycler.walletapp.mvvm.repository.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.recycler.walletapp.mvvm.repository.database.ExpenseDao;
import com.recycler.walletapp.mvvm.repository.database.UserDao;
import com.recycler.walletapp.mvvm.repository.models.Expense;
import com.recycler.walletapp.mvvm.repository.models.User;

public class UserRepository extends BaseRepository {

    private UserDao userDao;


    public UserRepository(Application application) {
        super(application);
        userDao = simpleDatabase.userDao();
    }

    public void RegisterUser(User user) {
        userDao.RegisterUser(user);
    }

    public LiveData<User> LoginUser(String email, String password) {
        return userDao.LoginUser(email, password);
    }

    public LiveData<User> GetCurrentUser(String email) {
        return userDao.GetCurrentUser(email);
    }

    public Integer GetCurrentUserIncome(int id) {
        return userDao.GetCurrentUserIncome(id);
    }

    public LiveData<Integer> GetCurrentUserLiveIncome(int id) {
        return userDao.GetCurrentUserLiveIncome(id);
    }

    public Integer GetCurrentUserPreviousIncome(int id) {
        return userDao.GetCurrentUserPreviousIncome(id);
    }

    public void InsertCurrentUserIncome(int income, int id) {
        new InsertUserIncomeAsyncTask(userDao, income, id).execute();
    }

    public void UpdateUser(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void InsertCurrentUserPreviousIncome(int income, int id) {
        new InsertUserPreviousIncomeAsyncTask(userDao, income, id).execute();
    }


    public LiveData<User> GetCurrentUserById(String id) {
        return userDao.GetCurrentUserId(id);
    }

    public LiveData<Boolean> CheckEmailExists(String email) {
        return userDao.CheckEmailExists(email);
    }


    private static class InsertUserIncomeAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;
        private int income, id;

        public InsertUserIncomeAsyncTask(UserDao userDao, int income, int id) {
            this.userDao = userDao;
            this.income = income;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.InsertUserIncome(income, id);
            return null;
        }
    }


    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;

        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.UpdateUser(users[0]);
            return null;
        }
    }


    private static class InsertUserPreviousIncomeAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;
        private int income, id;

        public InsertUserPreviousIncomeAsyncTask(UserDao userDao, int income, int id) {
            this.userDao = userDao;
            this.income = income;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.InsertUserPreviousIncome(income, id);
            return null;
        }
    }
}
