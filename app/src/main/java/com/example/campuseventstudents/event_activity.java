package com.example.campuseventstudents;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressLint("MissingInflatedId")
public class event_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FirebaseApp.initializeApp(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Reference to LinearLayout to dynamically add TextViews
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear existing views before adding new ones
                linearLayout.removeAllViews();

                // Iterate through all events
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Events data = snapshot.getValue(Events.class);

                    // Create a LinearLayout to hold each event's information and delete button
                    LinearLayout eventLayout = new LinearLayout(event_activity.this);
                    eventLayout.setOrientation(LinearLayout.VERTICAL);

                    // Create TextViews dynamically for each event
                    TextView titleTextView = new TextView(event_activity.this);
                    titleTextView.setText(data.getEvent());

                    TextView departTextView = new TextView(event_activity.this);
                    departTextView.setText(data.getDept());

                    TextView linkTextView = new TextView(event_activity.this);
                    linkTextView.setText(data.getRegDate());

                    TextView dateTextView = new TextView(event_activity.this);
                    dateTextView.setText(data.getDate());



                    // Create a delete button for each event
                    Button deleteButton = new Button(event_activity.this);
                    deleteButton.setText("Register");
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(event_activity.this,event_reg_activity.class);
                            startActivity(i);
                        }
                    });

                    // Add TextViews and delete button to the eventLayout
                    eventLayout.addView(titleTextView);
                    eventLayout.addView(departTextView);
                    eventLayout.addView(dateTextView);
                    eventLayout.addView(linkTextView);
                    eventLayout.addView(deleteButton);

                    // Add eventLayout to the main LinearLayout
                    linearLayout.addView(eventLayout);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, add your code here
            }
        });
    }
}
