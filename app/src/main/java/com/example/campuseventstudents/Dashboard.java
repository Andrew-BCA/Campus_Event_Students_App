package com.example.campuseventstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    Button viewevents,viewnotify,viewpart;

    TextView roll_no ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        viewevents = findViewById(R.id.viewevent);
        viewnotify = findViewById(R.id.viewnotify);
        viewpart = findViewById(R.id.viewpart);

        viewevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,event_activity.class);
                startActivity(i);
            }
        });

        viewnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, View_Notify.class);
                startActivity(i);
            }
        });

        viewpart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, View_History.class);
                startActivity(i);
            }
        });


        roll_no = findViewById(R.id.textView);

        // Retrieve the roll number from SharedPreferences
        String rollNo = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("roll", "default_value_if_not_found");

        // Retrieve the roll number from SharedPreferences
        String Dept = getSharedPreferences("user_dept", MODE_PRIVATE)
                .getString("dept", "default_value_if_not_found");

        roll_no.setText("Welcome: "+rollNo);

    }
}