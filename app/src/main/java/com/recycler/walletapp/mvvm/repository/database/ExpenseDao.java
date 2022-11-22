package com.recycler.walletapp.mvvm.repository.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.recycler.walletapp.mvvm.repository.models.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void InsertExpense(Expense expense);

    @Query("SELECT * FROM Expense WHERE userid =:userid")
    LiveData<List<Expense>> GetExpenses(int userid);

    @Update
    void UpdateExpense(Expense expense);

    @Delete
    void DeleteExpense(Expense expense);


}
