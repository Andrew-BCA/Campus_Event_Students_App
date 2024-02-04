package com.example.campuseventstudents;

public class Message {
    private String name;
    private String content;
    private String subject;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String name, String content, String subject) {
        this.name = name;
        this.content = content;
        this.subject = subject;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
