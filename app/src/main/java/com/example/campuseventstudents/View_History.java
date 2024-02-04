package com.example.campuseventstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class View_History extends AppCompatActivity {

    TextView roll_no ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        roll_no = findViewById(R.id.textView);

        // Retrieve the roll number from SharedPreferences
        String rollNo = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("roll", "default_value_if_not_found");

        // Retrieve the dept number from SharedPreferences
        String Dept = getSharedPreferences("user_dept", MODE_PRIVATE)
                .getString("dept", "default_value_if_not_found");

        roll_no.setText(rollNo);



    }
}