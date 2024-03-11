package com.example.campuseventstudents;


public class Events {


    private String event;
    private String date;
    private String regDate;
    private String dept;
    private String addinfo;

    public Events() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Events(String event,String dept, String Date, String regDate,String addinfo) {
        this.event = event;
        this.date = Date;
        this.regDate = regDate;
        this.dept = dept;
        this.addinfo = addinfo;
    }

    public String getEvent() {
        return event;
    }

    public String getDate() {
        return date;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getDept() {
        return dept;
    }
    public String getAddinfo(){ return addinfo; }

}
