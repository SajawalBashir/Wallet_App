package com.recycler.walletapp.mvvm.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.repository.models.Expense;
import com.recycler.walletapp.utils.PreferencesManager;
import com.recycler.walletapp.mvvm.viewmodel.ExpenseViewModel;
import com.recycler.walletapp.mvvm.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateExpenseActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView DateView, TimeView;
    private EditText Category, Price;
    private MaterialButton AddButton;
    private ExpenseViewModel expenseViewModel;
    private UserViewModel userViewModel;
    private Calendar myCalendar;
    private String format, id;
    private Intent intent;
    private Expense expense;
    private static final String TAG = "UpdateExpenseActivity";
    private int income, PreviousPrice, previousincome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);
        init();
        initRef();
        GetIntentData();
        PopulateFields();
        GetCurrentUserId();
        ClickListener();
    }

    private void PopulateFields() {
        PreviousPrice = expense.getPrice();
        DateView.setText(expense.getDate());
        TimeView.setText(expense.getTime());
        Category.setText(expense.getCategory());
        Price.setText(String.valueOf(expense.getPrice()));
    }

    private void GetIntentData() {
        intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "Null Intent", Toast.LENGTH_SHORT).show();
        }
        expense = (Expense) intent.getSerializableExtra("expense");
    }

    private void GetCurrentUserId() {
        id = PreferencesManager.getInstance(UpdateExpenseActivity.this).getId();
    }

    private void initRef() {
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void ClickListener() {
        AddButton.setOnClickListener(this);
        DateView.setOnClickListener(this);
        TimeView.setOnClickListener(this);
    }

    private void init() {
        DateView = findViewById(R.id.DateView);
        TimeView = findViewById(R.id.TimeView);
        Category = findViewById(R.id.ExpenseCategory);
        Price = findViewById(R.id.Expenseprice);
        AddButton = findViewById(R.id.AddButton);
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.DateView) {
            final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDateField();
            };
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }

        if (v.getId() == R.id.TimeView) {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {

                if (selectedHour == 0) {

                    selectedHour += 12;

                    format = "AM";
                } else if (selectedHour == 12) {

                    format = "PM";

                } else if (selectedHour > 12) {

                    selectedHour -= 12;

                    format = "PM";

                } else {

                    format = "AM";
                }
                TimeView.setText(selectedHour + ":" + selectedMinute + ":" + format);
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
        if (v.getId() == R.id.AddButton) {
            String category = Category.getText().toString();
            int price = Integer.parseInt(Price.getText().toString());
            String date = DateView.getText().toString();
            String time = TimeView.getText().toString();

            if (TextUtils.isEmpty(category)) {
                Category.setError("Field Required");
                Category.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(String.valueOf(price))) {
                Price.setError("Field Required");
                Price.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(date)) {
                DateView.setError("Field Required");
                DateView.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(time)) {
                TimeView.setError("Field Required");
                TimeView.requestFocus();
                return;
            }


            income = userViewModel.GetCurrentUserIncome(Integer.parseInt(id));
            previousincome = userViewModel.GetCurrentUserPreviousIncome(Integer.parseInt(id));


            if (PreviousPrice > price) {
                //reduce expense
                int differencePrice = PreviousPrice - price;

                Expense expenses = new Expense(Integer.parseInt(id), date, time, category, price);
                expenses.setId(expense.getId());
                expenseViewModel.UpdateExpense(expenses);
                userViewModel.InsertCurrentUserIncome(income + differencePrice, Integer.parseInt(id));
                Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show();
            }
            if (PreviousPrice < price) {

                if (price > previousincome) {
                    Toast.makeText(getApplicationContext(), "Low Income. Please add some income", Toast.LENGTH_SHORT).show();
                } else if (price == previousincome) {
                    int differencePrice = previousincome - price;

                    Expense expenses = new Expense(Integer.parseInt(id), date, time, category, price);
                    expenses.setId(expense.getId());
                    expenseViewModel.UpdateExpense(expenses);
                    userViewModel.InsertCurrentUserIncome(differencePrice, Integer.parseInt(id));
                    Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show();

                } else {
                    int differencePrice = previousincome - price;

                    Expense expenses = new Expense(Integer.parseInt(id), date, time, category, price);
                    expenses.setId(expense.getId());
                    expenseViewModel.UpdateExpense(expenses);
                    userViewModel.InsertCurrentUserIncome( differencePrice, Integer.parseInt(id));
                    Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateDateField() {
        String myFormat = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DateView.setText(sdf.format(myCalendar.getTime()));

    }

}