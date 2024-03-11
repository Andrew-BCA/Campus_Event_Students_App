package com.example.campuseventstudents;

public class Participant {


    private String name;
    private String email;
    private String dept;
    private String eventName;
    private String mobile;
    private String eventDept;
    private  String roll;
    private String eventDate;


    public Participant() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Participant(String name,String email, String dept, String eventName,String eventDept,String roll,String eventDate,String mobile) {
        this.name = name;
        this.email = email;
        this.dept = dept;
        this.eventName = eventName;
        this.eventDept = eventDept;
        this.roll = roll;
        this.eventDate = eventDate;
        this.mobile = mobile;
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

    public String getEventDate(){ return eventDate; }

}
