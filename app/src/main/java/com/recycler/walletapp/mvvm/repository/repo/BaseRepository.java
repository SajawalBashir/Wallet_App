package com.recycler.walletapp.mvvm.repository.repo;

import android.app.Application;

import com.recycler.walletapp.mvvm.repository.database.SimpleDatabase;

public class BaseRepository {
    protected SimpleDatabase simpleDatabase;

    public BaseRepository(Application application) {
        simpleDatabase = SimpleDatabase.getInstance(application);
    }


}
