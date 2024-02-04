package com.example.campuseventstudents;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

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

      DatabaseReference  participants = FirebaseDatabase.getInstance().getReference("participants");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

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
                           // Intent i = new Intent(event_activity.this,event_reg_activity.class);
                           // startActivity(i);
                            // Retrieve the roll number from SharedPreferences
                            String roll = getSharedPreferences("user_info", MODE_PRIVATE)
                                    .getString("roll", "default_value_if_not_found");

                            // Retrieve the roll number from SharedPreferences
                            String dept = getSharedPreferences("user_dept", MODE_PRIVATE)
                                    .getString("dept", "default_value_if_not_found");

                            usersRef.child(dept).child(roll).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // User exists, retrieve additional information
                                        User user = dataSnapshot.getValue(User.class);

                                        // Retrieve event information from eventsRef
                                        eventsRef.child(data.getEvent()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot eventSnapshot) {
                                                if (eventSnapshot.exists()) {
                                                    Events event = eventSnapshot.getValue(Events.class);

                                                    // Now you have the complete user and event information, you can use it as needed
                                                    String username = user.getUsername();
                                                    String email = user.getEmail();
                                                    String mobile = user.getMobile();
                                                    String userDept = user.getDept();
                                                    String eventDate = event.getDate();
                                                    String eventDept = event.getDept();

                                                    // Perform your event registration logic using the retrieved information
                                                    // For example, you can store the event registration information in a new node in the database

                                                    // Create Participant object
                                                    Participant participant = new Participant(username, email, userDept, data.getEvent(), mobile, eventDept, roll, eventDate);

                                                    // Push data to Firebase
                                                    participants.child(dept).child(data.getEvent()).child(roll).setValue(participant);

                                                    // Finish registration or navigate to the next step
                                                    Toast.makeText(event_activity.this, "Event Registered successfully", Toast.LENGTH_SHORT).show();

                                                    finish();
                                                } else {
                                                    // Handle the case where the event does not exist
                                                    Toast.makeText(event_activity.this, "Event not found. Please check event name and organizing department.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle errors
                                                Toast.makeText(event_activity.this, "Error retrieving event information", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        // User does not exist
                                        Toast.makeText(event_activity.this, "User not found. Please check roll number and department.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle errors
                                    Toast.makeText(event_activity.this, "Error checking user existence", Toast.LENGTH_SHORT).show();
                                }
                            });

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
