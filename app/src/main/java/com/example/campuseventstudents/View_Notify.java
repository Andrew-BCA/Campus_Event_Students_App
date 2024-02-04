package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private TextView displayTextView;
    private DatabaseReference messagesRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notify);

        displayTextView = findViewById(R.id.displayTextView);

        // Reference to LinearLayout to dynamically add TextViews
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");

        // Read data from Firebase
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing views before adding new ones
                linearLayout.removeAllViews();

                // Iterate through each message
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot subjectsnapshot : snapshot.getChildren()) {
                        Message message = subjectsnapshot.getValue(Message.class);

                        // Create a LinearLayout to hold each notification
                        LinearLayout eventLayout = new LinearLayout(View_Notify.this);
                        eventLayout.setOrientation(LinearLayout.VERTICAL);

                        // Create TextViews dynamically for each event
                        TextView nameTextView = new TextView(View_Notify.this);
                        nameTextView.setText("Name: " + message.getName());

                        TextView subjectTextView = new TextView(View_Notify.this);
                        subjectTextView.setText("Subject: " + message.getSubject());

                        TextView contentTextView = new TextView(View_Notify.this);
                        contentTextView.setText("Content: " + message.getContent());

                        // Create a download button for each event
                        Button downloadButton = new Button(View_Notify.this);
                        downloadButton.setText("Download File");
                        downloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Create an Intent to download the brochure
                                String subject = message.getSubject();
                                downloadBrochure(subject);
                            }
                        });

                        // Add TextViews and delete button to the eventLayout
                        eventLayout.addView(nameTextView);
                        eventLayout.addView(subjectTextView);
                        eventLayout.addView(contentTextView);
                        eventLayout.addView(downloadButton);

                        // Add eventLayout to the main LinearLayout
                        linearLayout.addView(eventLayout);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void downloadBrochure(String subject) {
        // Implement the code to download the brochure from Firebase Cloud Storage
        // You can use the StorageReference to get the download URL and open it using an Intent
        // Example:
        StorageReference brochureRef = FirebaseStorage.getInstance().getReference().child("File/" + subject + ".pdf");
        brochureRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
            startActivity(browserIntent);
        }).addOnFailureListener(exception -> {
            // Handle any errors that may occur
        });
    }
}
