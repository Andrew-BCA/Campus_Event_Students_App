package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private TextView loginButton;
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
                    final String username = usernameEditText.getText().toString().trim().toUpperCase();
                    final String dept = deptEditText.getText().toString().trim().toUpperCase();
                    final String enteredPassword = passwordEditText.getText().toString();


                    databaseReference.child(dept).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Check if the password field exists
                                    String storedPassword = snapshot.child("password").getValue(String.class);

                                    // Hash the entered password
                                    String encryptedEnteredPassword = encryptPassword(enteredPassword);

                                    // Compare hashed passwords directly
                                    if (encryptedEnteredPassword.equals(storedPassword)) {
                                        // Passwords match, user authenticated

                                        // Redirect to loading
                                        Intent intent = new Intent(login_activity.this, loadin.class);
                                        startActivity(intent);
                                        finish();

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

                                       // return; // Exit the method after successful login
                                    }
                                    else {
                                        // Passwords don't match or password field doesn't exist
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


        });
    }


    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert bytes to hexadecimal representation
            StringBuilder builder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                builder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

