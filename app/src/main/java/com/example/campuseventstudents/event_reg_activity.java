package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class event_reg_activity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, deptEditText, mobileEditText,eventEditText,eventdeptEditText;
    private Button registerButton;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reg);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("participants");

        // Initialize UI elements
        nameEditText = findViewById(R.id.editTextName);
        deptEditText = findViewById(R.id.editTextDept);
        mobileEditText = findViewById(R.id.editTextMobile);
        emailEditText = findViewById(R.id.editTextEmail);
        eventEditText = findViewById(R.id.editTextEvent);
        eventdeptEditText = findViewById(R.id.editTextEventDept);
        registerButton = findViewById(R.id.buttonRegister);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String name = nameEditText.getText().toString().trim();
                String Roll_No = emailEditText.getText().toString().trim();
                String dept = deptEditText.getText().toString().trim();
                String eventName = eventEditText.getText().toString().trim();
                String eventDept = eventdeptEditText.getText().toString().trim();
                String mobile = mobileEditText.getText().toString().trim();

                // Create Participant object
                Participant participant = new Participant(name, Roll_No, dept, eventName, mobile, eventDept);

                // Push data to Firebase
                databaseReference.child(eventDept).child(eventName).child(Roll_No).setValue(participant);

                Toast.makeText(event_reg_activity.this, "Event Registered Successfully!!!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(event_reg_activity.this,event_activity.class);
                startActivity(i);
            }
        });


    }
}
