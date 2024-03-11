package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login_activity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, deptEditText;
    private View loginButton;
    private TextView register;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsername);
        deptEditText = findViewById(R.id.editTextDept);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        register = findViewById(R.id.register_he);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login_activity.this, register_activity.class);
                startActivity(i);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        final String username = usernameEditText.getText().toString();
        final String dept = deptEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        databaseReference.child(dept).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String storedPassword = snapshot.child("password").getValue(String.class);

                    // Decrypt stored password using SHA-256
                    if (storedPassword.equals(hashPassword(password))) {
                        // Passwords match, user authenticated

                        // Redirect to loading
                        Intent intent = new Intent(login_activity.this, loadin.class);
                        startActivity(intent);

                        // After password check, assuming "rollNo" is the field in your database
                        String rollNo = snapshot.child("roll").getValue(String.class);

                        // Save the roll number to SharedPreferences
                        getSharedPreferences("user_info", MODE_PRIVATE)
                                .edit()
                                .putString("roll", rollNo)
                                .apply();

                        String Dept = snapshot.child("dept").getValue(String.class);

                        // Save the dept number to SharedPreferences
                        getSharedPreferences("user_dept", MODE_PRIVATE)
                                .edit()
                                .putString("dept", Dept)
                                .apply();

                    } else {
                        // Passwords don't match
                        Toast.makeText(login_activity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login_activity.this, login_failed.class);
                        startActivity(intent);
                    }
                } else {
                    // User not found
                    Toast.makeText(login_activity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login_activity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
