package com.example.campuseventstudents;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
                    titleTextView.setText("Event Name: "+data.getEvent());

                    TextView departTextView = new TextView(event_activity.this);
                    departTextView.setText("Organizing Dept: "+data.getDept());

                    TextView linkTextView = new TextView(event_activity.this);
                    linkTextView.setText("Last date for Registration: "+data.getRegDate());

                    TextView dateTextView = new TextView(event_activity.this);
                    dateTextView.setText("Event Date: "+data.getDate());

                    // Create a download button for each event
                    Button downloadButton = new Button(event_activity.this);
                    downloadButton.setText("Download Brochure");
                    downloadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an Intent to download the brochure
                            String eventName = data.getEvent();
                            downloadBrochure(eventName);
                        }
                    });



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
                    eventLayout.addView(downloadButton);
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

    private void downloadBrochure(String eventName) {
        // Implement the code to download the brochure from Firebase Cloud Storage
        // You can use the StorageReference to get the download URL and open it using an Intent
        // Example:
         StorageReference brochureRef = FirebaseStorage.getInstance().getReference().child("brochures/" + eventName + ".pdf");
         brochureRef.getDownloadUrl().addOnSuccessListener(uri -> {
             String downloadUrl = uri.toString();
             Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
             startActivity(browserIntent);
         }).addOnFailureListener(exception -> {
        //     // Handle any errors that may occur
         });
    }
}
