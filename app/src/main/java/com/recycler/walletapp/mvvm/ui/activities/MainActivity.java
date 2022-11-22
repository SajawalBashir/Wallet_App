package com.recycler.walletapp.mvvm.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.ui.authentication.LoginActivity;
import com.recycler.walletapp.utils.PreferencesManager;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView UserName, UserEmail;
    private ProgressDialog dialog;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initRef();
        setSupportActionBar(toolbar);
        initHeaderView();
        CreateAppBarConfiguration();
        SetDrawer();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }

    private void initRef() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void initHeaderView() {
        UserEmail = navigationView.getHeaderView(0).findViewById(R.id.UserEmail);
        UserName = navigationView.getHeaderView(0).findViewById(R.id.UserName);
        dialog.setMessage("Fetching data...");
        dialog.setCancelable(false);
        dialog.show();
        userViewModel.GetCurrentUserById(PreferencesManager.getInstance(MainActivity.this).getId())
                .observe(this, CurrentUser -> {
                    if (CurrentUser != null) {
                    UserEmail.setText(CurrentUser.getEmail());
                    UserName.setText(CurrentUser.getName());
                    }
                });
        dialog.dismiss();
    }

    private void SetDrawer() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void CreateAppBarConfiguration() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_expenses,R.id.nav_income)
                .setDrawerLayout(drawer)
                .build();
    }


    private void init() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        dialog = new ProgressDialog(MainActivity.this);
    }

    private void setToolBar() {
        setToolBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout){
            PreferencesManager.getInstance(MainActivity.this).Logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}