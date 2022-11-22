package com.recycler.walletapp.mvvm.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.repository.models.User;
import com.recycler.walletapp.mvvm.ui.activities.MainActivity;
import com.recycler.walletapp.utils.PreferencesManager;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Name, Email, Password;
    private ProgressDialog progressDialog;
    private UserViewModel userViewModel;
    private Button RegisterButton;
    private TextView RegisterToLoginLink;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initRef();
        ClickListener();
    }

    private void ClickListener() {
        RegisterButton.setOnClickListener(this);
        RegisterToLoginLink.setOnClickListener(this);
    }

    private void initRef() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }


    private void init() {
        Name = findViewById(R.id.nameText);
        Email = findViewById(R.id.EmailText);
        Password = findViewById(R.id.PasswordText);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        RegisterButton = findViewById(R.id.RegisterButton);
        RegisterToLoginLink = findViewById(R.id.RegisterToLoginLink);
        RegisterToLoginLink.setText(Html.fromHtml("Already have Account?" + "<b> Login </b>"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.RegisterToLoginLink) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
        if (v.getId() == R.id.RegisterButton) {
            String name = Name.getText().toString();
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Name.setError("Field Required");
                Name.requestFocus();
                return;
            }
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

            userViewModel.CheckEmailExists(email).observe(this, value -> {
                if (value) {
                    Toast.makeText(this, "Email already created", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.setMessage("Registering user Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    User user = new User(name, email, password,0,0);

                    userViewModel.RegisterUser(user);

                    userViewModel.GetCurrentUser(email).observe(this, currentuser -> {
                        if (currentuser != null) {

                            //store in shared preferences
                            PreferencesManager.getInstance(RegisterActivity.this).UserRegister(String.valueOf(currentuser.getId()),
                                    currentuser.getEmail(),currentuser.getPassword());
                            Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    });

                progressDialog.dismiss();

                }
            });

          /*  new Thread(() -> {
                boolean value = userViewModel.CheckEmailExists(email);

                    runOnUiThread(() -> {
                        if(value) {
                            Toast.makeText(this, email + "\n already created", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.setMessage("Registering user Please Wait...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            User user = new User(name,email,password);

                            userViewModel.RegisterUser(user);


                             Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    });

            }).start();

            new Thread(()->{
                runOnUiThread(()->{
                    User currentUser = userViewModel.GetCurrentUser(email);
                    Log.d(TAG, "onClick: CURRENT USER " + currentUser.getEmail());
                    Log.d(TAG, "onClick: CURRENT USER " + currentUser.getName());
                    Log.d(TAG, "onClick: CURRENT USER " + currentUser.getPassword());
                    Log.d(TAG, "onClick: CURRENT USER " + currentUser.getId());

                });
            }).start();*/

        }
    }
}