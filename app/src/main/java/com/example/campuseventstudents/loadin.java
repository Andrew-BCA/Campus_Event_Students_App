package com.example.campuseventstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class loadin extends AppCompatActivity {
    private static final long DELAY_TIME_MS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadin);

        // Initialize a handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the target activity after the delay
                Intent intent = new Intent(loadin.this, Dashboard.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back
            }
        }, DELAY_TIME_MS);
    }
}
