package com.example.campuseventstudents;

public class Participant {


    private String name;
    private String email;
    private String dept;
    private String eventName;
    private String mobile;
    private String eventDept;
    private  String roll;


    public Participant() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Participant(String name,String email, String dept, String eventName,String mobile,String eventDept,String roll) {
        this.name = name;
        this.email = email;
        this.dept = dept;
        this.eventName = eventName;
        this.eventDept = eventDept;
        this.mobile = mobile;
        this.roll = roll;
    }

    public String getName() { return name; }

    public String getEmail() {
        return email;
    }

    public String getDept() {
        return dept;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDept() {
        return eventDept;
    }

    public String getMobile(){ return mobile; }

    public  String getRoll(){ return roll; }

}
