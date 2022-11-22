package com.recycler.walletapp.mvvm.repository.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.recycler.walletapp.mvvm.repository.database.ExpenseDao;
import com.recycler.walletapp.mvvm.repository.models.Expense;

import java.util.List;

public class ExpenseRepository extends BaseRepository {
    private final ExpenseDao expenseDao;


    public ExpenseRepository(Application application) {
        super(application);
        expenseDao = simpleDatabase.expenseDao();
    }

    public void InsertNewExpense(Expense expense) {
        new InsertExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void UpdateExpense(Expense expense) {
        new UpdateExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void DeleteExpense(Expense expense) {
        new DeleteExpenseAsyncTask(expenseDao).execute(expense);
    }

    public LiveData<List<Expense>> GetAllExpenses(int id) {
        return expenseDao.GetExpenses(id);
    }

    private static class InsertExpenseAsyncTask extends AsyncTask<Expense,Void,Void>{

        private ExpenseDao expenseDao;

        public InsertExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.InsertExpense(expenses[0]);
            return null;
        }
    }

    private static class UpdateExpenseAsyncTask extends AsyncTask<Expense,Void,Void>{

        private ExpenseDao expenseDao;

        public UpdateExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.UpdateExpense(expenses[0]);
            return null;
        }
    }

    private class DeleteExpenseAsyncTask extends AsyncTask<Expense,Void,Void>{

        private ExpenseDao expenseDao;

        public DeleteExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.DeleteExpense(expenses[0]);
            return null;
        }
    }

}
