package com.example.campuseventstudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class forgetpass extends AppCompatActivity {

    public EditText otpInput, RollnoEditText, DeptEditText, emailEditText, passEditText;
    public TextView generateOtpButton, resetButton;
    public DatabaseReference databaseReference,otpreference;
    public int otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        otpInput = findViewById(R.id.otp);
        generateOtpButton = findViewById(R.id.buttongenerateotp);
        resetButton = findViewById(R.id.buttonreset);
        passEditText = findViewById(R.id.editTextnewpass);
        RollnoEditText = findViewById(R.id.editTextRollNo);
        DeptEditText = findViewById(R.id.editTextDept);
        emailEditText = findViewById(R.id.editTextmail);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        otpreference = FirebaseDatabase.getInstance().getReference("otp");

        generateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = generateRandomNumber();

                String rollNo = RollnoEditText.getText().toString().trim().toUpperCase();
                String dept = DeptEditText.getText().toString().trim().toUpperCase();
                String email = emailEditText.getText().toString().trim();

                otpreference.child(dept).child(rollNo).setValue(otp);
                sendOtp();
                //sendEmail(otp, email);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass = passEditText.getText().toString();
                String hashedpass = hashPassword(newpass);
                // Assuming otpInput is an EditText view
                String otpString = otpInput.getText().toString();
                int otpValue = Integer.parseInt(otpString);


                passwordchange(otpValue,hashedpass);
            }
        });
    }

    private void sendOtp() {
        String rollNo = RollnoEditText.getText().toString().trim().toUpperCase();
        String dept = DeptEditText.getText().toString().trim().toUpperCase();
        String email = emailEditText.getText().toString().trim();

        if (rollNo.isEmpty() || dept.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(dept).child(rollNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    String storedEmail = user.getEmail();
                    if (email.equals(storedEmail)) {
                        otp = generateRandomNumber();
                        otpreference.child(dept).child(rollNo).child("password").setValue(otp);
                        sendEmail(); // Send the OTP to the user's email
                        Toast.makeText(forgetpass.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(forgetpass.this, "Email does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(forgetpass.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(forgetpass.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }



   /* private void sendEmail() {
        // Replace with your Gmail
        final String senderEmail = "andrewpatrick011@gmail.com";
        final String senderPassword = "gfeo gryv irqr stoq";

        String email = emailEditText.getText().toString().trim();

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(forgetpass.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void sendEmail() {
        final String email = emailEditText.getText().toString().trim();
        final String recipientEmail = email;
        final String Rollno = RollnoEditText.getText().toString().trim().toUpperCase();

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
            message.setSubject("Password Reset OTP");
            message.setText("Dear,"+Rollno +"\n Your OTP for password reset is:"+ otp);

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
    private int generateRandomNumber() {
        Random random = new Random();
        // Generate a random number between 100000 and 999999 (inclusive)
        return random.nextInt(900000) + 100000;
    }

    private void passwordchange(int enteredOtp, String pass) {
        String rollNo = RollnoEditText.getText().toString().trim().toUpperCase();
        String dept = DeptEditText.getText().toString().trim().toUpperCase();

        if (rollNo.isEmpty() || dept.isEmpty()) {
            Toast.makeText(this, "Please fill in Roll No. and Dept", Toast.LENGTH_SHORT).show();
            return;
        }

        otpreference.child(dept).child(rollNo).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int storedOtp = snapshot.getValue(Integer.class);
                    // Log.d("OTP Debug", "Entered OTP: " + enteredOtp + ", Stored OTP: " + storedOtp); // Debugging
                    if (enteredOtp == storedOtp) {
                        // Update password in the 'users' node
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(dept).child(rollNo).child("password").setValue(pass);
                        Toast.makeText(forgetpass.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        otpreference.child(dept).child(rollNo).removeValue();
                        Intent i =new Intent(forgetpass.this,login_activity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(forgetpass.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(forgetpass.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(forgetpass.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }


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
}

