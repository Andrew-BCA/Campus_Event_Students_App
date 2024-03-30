package com.example.campuseventstudents;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class register_activity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, MobileEditText, RollnoEditText, DeptEditText;
    private TextView registerButton;
    private DatabaseReference databaseReference;

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
        final String username = usernameEditText.getText().toString().trim().toUpperCase();
        final String Rollno = RollnoEditText.getText().toString().trim().toUpperCase();
        final String mobile = MobileEditText.getText().toString().trim();
        final String Dept = DeptEditText.getText().toString().trim().toUpperCase();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        // Check if fields are not empty
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !Rollno.isEmpty() && !mobile.isEmpty() && !Dept.isEmpty()) {
            // Hash the password using SHA-256
            final String hashedPassword = hashPassword(password);

            // Check if user with the provided Rollno already exists
            databaseReference.child(Dept).child(Rollno).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User with the provided Rollno already exists
                        Toast.makeText(register_activity.this, "User with Roll number " + Rollno + " already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // User with the provided Rollno does not exist, proceed with registration
                        // You can also check for existing users with the provided email similarly if needed

                        // Create User object
                        User user = new User(Rollno, username, Dept, email, mobile, hashedPassword);

                        // Add user to the database
                        databaseReference.child(Dept).child(Rollno).setValue(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sendEmail();
                                            Toast.makeText(register_activity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                            Intent i = new Intent(register_activity.this, loading_reg.class);
                                            shownotification(register_activity.this,"Student","Registration Successful",i);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(register_activity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    Toast.makeText(register_activity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Display an error message if any field is empty
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }
    }

    //Password hashing
    private String hashPassword(String password) {
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

    //notification
    private void shownotification(Context context, String title, String message, Intent intent)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId=1;
        String channelId = "Channel1";
        String channelName = "My Channel"; // Fixed typo

        int important = NotificationManager.IMPORTANCE_HIGH;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName,important
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message);

        PendingIntent intent1 = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_MUTABLE);
        mBuilder.setContentIntent(intent1);
        notificationManager.notify(notificationId,mBuilder.build());
    }

    private void sendEmail() {
        final String email = emailEditText.getText().toString().trim();
        final String recipientEmail = email;
        final String Rollno = RollnoEditText.getText().toString().trim();

        // Replace with your Gmail
        final String senderEmail = "andrewpatrick011@gmail.com";
        final String senderPassword = "pvdf cmzm akqm bmrh";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Registration Successfull");
            message.setText("Dear,"+Rollno +"\n Your Registration process for RegistriX is successfully completed....");

            // Perform email sending in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // Toast.makeText(getApplicationContext(), "Email sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
        }
    }
}
