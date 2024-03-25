package com.example.campuseventstudents;

public class User {

    private String roll;
    private String Dept;
    private String mobile;
    private String username;
    private String email;
    private String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String roll, String username, String Dept, String email, String mobile ,String password) {
        this.roll = roll;
        this.username = username;
        this.Dept = Dept;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public String getRoll() {
        return roll;
    }

    public String getUsername() {
        return username;
    }

    public String getDept() {
        return Dept;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }
}
