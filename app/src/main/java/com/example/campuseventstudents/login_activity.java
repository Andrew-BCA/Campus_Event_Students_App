package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText,deptEditText;
    private Button loginButton;
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String dept = deptEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                databaseReference.child(dept).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String storedPassword = snapshot.child("password").getValue(String.class);

                            if (password.equals(storedPassword)) {
                                // Passwords match, user authenticated
                                Toast.makeText(login_activity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                Log.d("LoginActivity", "Redirecting to EventActivity");

                                // Redirect to EventActivity
                                Intent intent = new Intent(login_activity.this, event_activity.class);
                                startActivity(intent);
                            }
                            else {
                                // Passwords don't match
                                Toast.makeText(login_activity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
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
}
