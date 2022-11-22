package com.recycler.walletapp.mvvm.repository.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.recycler.walletapp.mvvm.repository.models.Expense;
import com.recycler.walletapp.mvvm.repository.models.User;


@Database(entities = {User.class, Expense.class}, version = 9)

public abstract class SimpleDatabase extends RoomDatabase {
    private static SimpleDatabase instance;

    public abstract UserDao userDao();
    public abstract ExpenseDao expenseDao();

    public static synchronized SimpleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SimpleDatabase.class, "SimpleDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;

    }
/*

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };
*/

}
