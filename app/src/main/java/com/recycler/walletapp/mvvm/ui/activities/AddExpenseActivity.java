package com.recycler.walletapp.mvvm.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView DateView, TimeView;
    private EditText Category, Price;
    private MaterialButton AddButton;
    private ExpenseViewModel expenseViewModel;
    private UserViewModel userViewModel;
    private Calendar myCalendar;
    private String format, id;
    private static final String TAG = "AddExpenseActivity";
    private int currentincome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        init();
        initRef();
        GetCurrentUserId();
        ClickListener();
    }

    private void GetCurrentUserId() {
        id = PreferencesManager.getInstance(AddExpenseActivity.this).getId();
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

            Expense expense = new Expense(Integer.parseInt(id), date, time, category, price);

            currentincome = userViewModel.GetCurrentUserIncome(Integer.parseInt(id));
            if (price > currentincome) {
                Log.d(TAG, "onClick: INCOME " + currentincome);
                Toast.makeText(this, "Your Income is low. Please add some income", Toast.LENGTH_SHORT).show();
            } else {

                expenseViewModel.InsertNewExpense(expense);
                userViewModel.InsertCurrentUserIncome(currentincome - price, Integer.parseInt(id));
                userViewModel.InsertCurrentUserPreviousIncome(currentincome, Integer.parseInt(id));

                Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDateField() {
        String myFormat = "dd/MM/yyyy";
        String dateFormat = "dd";
        String currentFormat = "dd";
        String monthFormat = "MM";
        String currentmonthFormat = "MM";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, Locale.US);
        SimpleDateFormat sdfmonthfomat = new SimpleDateFormat(monthFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(currentFormat, Locale.US);
        SimpleDateFormat sdfcurrentmonthformat = new SimpleDateFormat(currentmonthFormat, Locale.US);

        int currentdate = Integer.parseInt(sdf2.format(Calendar.getInstance().getTime()));
        int calenderdate = Integer.parseInt(sdf1.format(myCalendar.getTime()));
        int calendermonth = Integer.parseInt(sdfmonthfomat.format(myCalendar.getTime()));
        int calendercurrentmonth = Integer.parseInt(sdfcurrentmonthformat.format(Calendar.getInstance().getTime()));


        Log.d(TAG, calendercurrentmonth + "   " + calendermonth);
        if (calenderdate < currentdate && calendermonth == calendercurrentmonth) {
            Toast.makeText(this, "Please Enter Current date or newer", Toast.LENGTH_SHORT).show();
            DateView.setText("");

        } else {
            DateView.setText(sdf.format(myCalendar.getTime()));
        }
    }
}