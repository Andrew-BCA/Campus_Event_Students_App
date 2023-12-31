package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register_activity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText,MobileEditText,RollnoEditText,DeptEditText;
    private ImageButton registerButton;

    // Firebase
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        usernameEditText = findViewById(R.id.editTextUsername);
        MobileEditText = findViewById(R.id.editTextMobile);
        RollnoEditText = findViewById(R.id.editTextRollNo);
        DeptEditText = findViewById(R.id.editTextDept);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String Rollno = RollnoEditText.getText().toString().trim();
        String mobile = MobileEditText.getText().toString().trim();
        String Dept = DeptEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Check if fields are not empty
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !Rollno.isEmpty() && !mobile.isEmpty() && !Dept.isEmpty()) {
            // Create a unique user ID using email
            String userId = Rollno;
            String dept = Dept;

            // Create User object
            User user = new User(Rollno, username, Dept, email, mobile, password);

            // Add user to the database
            databaseReference.child(dept).child(Rollno).setValue(user);

            // You can add additional logic here, like showing a success message
            Toast.makeText(this, "Registered Successful!!!!!!!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(register_activity.this, login_activity.class);
            startActivity(i);

        } else {
            // Display an error message if any field is empty
            // You can customize this part based on your requirements
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }
    }
}
