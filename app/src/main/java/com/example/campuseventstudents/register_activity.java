package com.example.campuseventstudents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class register_activity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, MobileEditText, RollnoEditText, DeptEditText;
    private Button registerButton;

    // Firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

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
            // Hash the password using SHA-256
            String hashedPassword = hashPassword(password);

            // Create User object
            User user = new User(Rollno, username, Dept, email, mobile, hashedPassword);

            // Add user to the database
            databaseReference.child(Dept).child(Rollno).setValue(user);

            // Create a unique user ID using email
            String userId = Rollno;

            firebaseAuth.createUserWithEmailAndPassword(email, hashedPassword)
                    .addOnCompleteListener(register_activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(register_activity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(register_activity.this, loading_reg.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(register_activity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Display an error message if any field is empty
            // You can customize this part based on your requirements
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }
    }

    private String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hashed bytes
            byte[] hashedBytes = md.digest();

            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
    }
}
