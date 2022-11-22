package com.recycler.walletapp.mvvm.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.ui.activities.MainActivity;
import com.recycler.walletapp.utils.PreferencesManager;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Email, Password;
    private Button LoginButton;
    private TextView LoginToRegisterLink;
    private UserViewModel userViewModel;
    private static final String TAG = "LoginActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferencesManager.getInstance(getApplicationContext()).IsLoggedIn()) {
            Log.d("ISLOGIN ", String.valueOf(PreferencesManager.getInstance(getApplicationContext()).IsLoggedIn()));
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initRef();
        ClickListener();
    }

    private void initRef() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void ClickListener() {
        LoginButton.setOnClickListener(this);
        LoginToRegisterLink.setOnClickListener(this);
    }

    private void init() {
        Email = findViewById(R.id.EmailText);
        LoginButton = findViewById(R.id.SigninButton);
        Password = findViewById(R.id.PasswordText);
        LoginToRegisterLink = findViewById(R.id.LoginToRegisterLink);
        LoginToRegisterLink.setText(Html.fromHtml("Don't have Account?" + "<b> Register </b>"));
        progressDialog = new ProgressDialog(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.SigninButton) {
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Email.setError("Field Required");
                Email.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Password.setError("Field Required");
                Password.requestFocus();
                return;
            }
            userViewModel.LoginUser(email, password).observe(this, user -> {
                if (user == null) {
                    Toast.makeText(this, "No User Exists", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Logging In  Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    PreferencesManager.getInstance(LoginActivity.this).UserLogin(String.valueOf(user.getId()),
                            user.getEmail(), user.getPassword());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            });

        }
        if (id == R.id.LoginToRegisterLink) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

        }
    }
}