package com.example.campuseventstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_History extends AppCompatActivity {

    TextView roll_no, displayTextView ;
    private DatabaseReference dbref;
    private DataSnapshot dataSnapshot; // Add this variable to store the initial data
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        roll_no = findViewById(R.id.textView);
        displayTextView = findViewById(R.id.displayTextView);

        // Retrieve the roll number from SharedPreferences
        String rollNo = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("roll", "default_value_if_not_found");

        // Retrieve the dept from SharedPreferences
        String Dept = getSharedPreferences("user_dept", MODE_PRIVATE)
                .getString("dept", "default_value_if_not_found");



        roll_no.setText(rollNo);

        dbref = FirebaseDatabase.getInstance().getReference("participanthistory");

        // ...

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // List to store the data for all dates
                    List<String> dataForAllDates = new ArrayList<>();

                    for (DataSnapshot deptsnapshot : dataSnapshot.getChildren()) {
                        if (deptsnapshot.getKey().equals(Dept)) {
                            for (DataSnapshot eventsnapshot : deptsnapshot.getChildren()) {
                                for (DataSnapshot rollsnapshot : eventsnapshot.getChildren()) {
                                    if (rollsnapshot.getKey().equals(rollNo)) {

                                        String evname = rollsnapshot.child("eventName").getValue(String.class);
                                        String evdept = rollsnapshot.child("eventDept").getValue(String.class);
                                        String evdate = rollsnapshot.child("eventDate").getValue(String.class);


                                        // Format the data for display
                                        String displayData = "Event Name: " + evname + "\n"
                                                +"Event Date: " + evdate + "\n"
                                                + "Event Organized Dept: " + evdept + "\n\n";

                                        dataForAllDates.add(displayData);
                                    }
                                }
                            }
                        }
                    }

                    if (!dataForAllDates.isEmpty()) {
                        // Display all the data collected
                        StringBuilder allData = new StringBuilder();
                        for (String data : dataForAllDates) {
                            allData.append(data);
                        }
                        displayTextView.setText(allData.toString());
                    } else {
                        // Handle the case where no data exists for the selected employee
                        displayTextView.setText("Poi ethacham eventla participate pannu da");
                    }
                } else {
                    // Handle the case where no data exists
                    displayTextView.setText("No data available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                displayTextView.setText("Error retrieving data: " + databaseError.getMessage());
            }
        });

// ...

    }
}