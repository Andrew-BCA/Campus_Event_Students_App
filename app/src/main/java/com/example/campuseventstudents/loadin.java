package com.example.campuseventstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class loadin extends AppCompatActivity {

    private static final long DELAY_TIME_MS = 1500;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadin);

        // Initialize and start playing the audio
        mediaPlayer = MediaPlayer.create(this, R.raw.audio1);
        mediaPlayer.start();

        Toast.makeText(loadin.this, "Login successful!", Toast.LENGTH_SHORT).show();

        // Initialize a handler to delay starting the next activity
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
