package com.example.campuseventstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class registeredsuccessfull extends AppCompatActivity {

    private static final long DELAY_TIME_MS = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeredsuccessfull);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the target activity after the delay
                Intent intent = new Intent(registeredsuccessfull.this, login_activity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back
            }
        }, DELAY_TIME_MS);
    }
}