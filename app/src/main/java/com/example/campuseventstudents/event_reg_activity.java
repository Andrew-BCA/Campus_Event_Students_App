package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class event_reg_activity extends AppCompatActivity {

    private EditText rollEditText, deptEditText;
    private Button registerButton;
    private Spinner eventNameSpinner, organizingDeptSpinner;

    private DatabaseReference usersRef, eventsRef, participants;

    private List<String> eventNames;
    private List<String> organizingDepts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reg);

        // Initialize Firebase Database references
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        participants = FirebaseDatabase.getInstance().getReference("participants");

      //  rollEditText = findViewById(R.id.editTextRoll);
     //   deptEditText = findViewById(R.id.editTextDept);
        registerButton = findViewById(R.id.buttonRegister);
        eventNameSpinner = findViewById(R.id.spinnerEventName);
        organizingDeptSpinner = findViewById(R.id.spinnerOrganizingDept);

        // Initialize the list of event names
        eventNames = new ArrayList<>();
        organizingDepts = new ArrayList<>();

        // Set up the adapter for the event name spinner
        ArrayAdapter<String> eventNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, eventNames);
        eventNameSpinner.setAdapter(eventNameAdapter);

        // Set up the adapter for the organizing department spinner
        ArrayAdapter<String> organizingDeptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, organizingDepts);
        organizingDeptSpinner.setAdapter(organizingDeptAdapter);

        // Load event names and organizing departments from the database
        loadEventNames();
        loadOrganizingDepts();

        eventNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedEventName = eventNames.get(position);
                // You can use the selected event name as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle when nothing is selected
            }
        });

        organizingDeptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedOrganizingDept = organizingDepts.get(position);
                // You can use the selected organizing department as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle when nothing is selected
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String selectedEventName = eventNameSpinner.getSelectedItem().toString();
                String selectedOrganizingDept = organizingDeptSpinner.getSelectedItem().toString();

                // Retrieve the roll number from SharedPreferences
                String roll = getSharedPreferences("user_info", MODE_PRIVATE)
                        .getString("roll", "default_value_if_not_found");

                // Retrieve the roll number from SharedPreferences
                String dept = getSharedPreferences("user_dept", MODE_PRIVATE)
                        .getString("dept", "default_value_if_not_found");

                checkUserExistence(roll, dept, selectedEventName, selectedOrganizingDept);

            }
        });

    }

    private void loadEventNames() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String eventName = eventSnapshot.getKey();
                    eventNames.add(eventName);
                }
                // Notify the adapter that the dataset has changed
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) eventNameSpinner.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(event_reg_activity.this, "Error loading event names", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrganizingDepts() {
        // Assuming your events have a "dept" field indicating the organizing department
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String organizingDept = eventSnapshot.child("dept").getValue(String.class);
                    if (organizingDept != null && !organizingDepts.contains(organizingDept)) {
                        organizingDepts.add(organizingDept);
                    }
                }
                // Notify the adapter that the dataset has changed
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) organizingDeptSpinner.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(event_reg_activity.this, "Error loading organizing departments", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkUserExistence(final String roll, final String dept, final String selectedEventName, final String selectedOrganizingDept) {

        usersRef.child(dept).child(roll).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, retrieve additional information
                    User user = dataSnapshot.getValue(User.class);

                    // Retrieve event information from eventsRef
                    eventsRef.child(selectedEventName).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                Participant participant = new Participant(username, email, userDept, selectedEventName, mobile, eventDept, roll, eventDate);

                                // Push data to Firebase
                                participants.child(dept).child(selectedEventName).child(roll).setValue(participant);

                                // Finish registration or navigate to the next step
                                Toast.makeText(event_reg_activity.this, "Event Registered successfully", Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                // Handle the case where the event does not exist
                                Toast.makeText(event_reg_activity.this, "Event not found. Please check event name and organizing department.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle errors
                            Toast.makeText(event_reg_activity.this, "Error retrieving event information", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // User does not exist
                    Toast.makeText(event_reg_activity.this, "User not found. Please check roll number and department.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Toast.makeText(event_reg_activity.this, "Error checking user existence", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
