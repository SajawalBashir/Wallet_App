package com.recycler.walletapp.mvvm.repository.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.recycler.walletapp.mvvm.repository.models.User;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void RegisterUser(User user);

    @Query("SELECT * FROM user_table WHERE email =(:email) AND password =(:password)")
    LiveData<User> LoginUser(String email,String password);

    @Update
    void UpdateUser(User user);


    @Query("SELECT * FROM user_table WHERE email = :email")
    LiveData<User> GetCurrentUser(String email);

    @Query("SELECT income FROM user_table WHERE id = :id")
    Integer GetCurrentUserIncome(int id);

    @Query("SELECT income FROM user_table WHERE id = :id")
    LiveData<Integer> GetCurrentUserLiveIncome(int id);

    @Query("SELECT previousincome FROM user_table WHERE id = :id")
    Integer GetCurrentUserPreviousIncome(int id);


    @Query("UPDATE user_table SET income=:income WHERE id=:id")
    void InsertUserIncome(int income,int id);

    @Query("UPDATE user_table SET previousincome=:income WHERE id=:id")
    void InsertUserPreviousIncome(int income,int id);


    @Query("SELECT * FROM user_table WHERE id = :id")
    LiveData<User> GetCurrentUserId(String id);

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE email = :email)")
    LiveData<Boolean> CheckEmailExists(String email);



}
