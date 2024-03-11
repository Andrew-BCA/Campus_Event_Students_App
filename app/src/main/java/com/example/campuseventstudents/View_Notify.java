package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class View_Notify extends AppCompatActivity {

    private DatabaseReference messagesRef;
    private FirebaseStorage storage;
    private LinearLayout linearLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notify);

        linearLayout = findViewById(R.id.linearLayout);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();

        // Read data from Firebase
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayMessages(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void displayMessages(DataSnapshot dataSnapshot) {
        linearLayout.removeAllViews(); // Clear existing views

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                Message message = messageSnapshot.getValue(Message.class);
                if (message != null) {
                    addMessageView(message);
                }
            }
        }
    }

    private void addMessageView(Message message) {
        // Create a LinearLayout to hold each notification
        LinearLayout eventLayout = new LinearLayout(this);
        eventLayout.setOrientation(LinearLayout.VERTICAL);

        // Create TextViews dynamically for each message
        TextView nameTextView = new TextView(this);
        nameTextView.setText("Name: " + message.getName());

        TextView subjectTextView = new TextView(this);
        subjectTextView.setText("Subject: " + message.getSubject());

        TextView contentTextView = new TextView(this);
        contentTextView.setText("Content: " + message.getContent());

        // Create a download button for each message
        Button downloadButton = new Button(this);
        downloadButton.setText("Download File");
        downloadButton.setOnClickListener(view -> downloadBrochure(message.getSubject()));

        // Add TextViews and download button to the eventLayout
        eventLayout.addView(nameTextView);
        eventLayout.addView(subjectTextView);
        eventLayout.addView(contentTextView);
        eventLayout.addView(downloadButton);

        // Add eventLayout to the main LinearLayout
        linearLayout.addView(eventLayout);
    }

    private void downloadBrochure(String eventName) {
        // Implement the code to download the brochure from Firebase Cloud Storage
        // You can use the StorageReference to get the download URL and open it using an Intent
        // Example:
        StorageReference brochureRef = FirebaseStorage.getInstance().getReference().child("files/" + eventName + ".pdf");
        brochureRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
            startActivity(browserIntent);
        }).addOnFailureListener(exception -> {
            //     // Handle any errors that may occur
        });
    }
}
