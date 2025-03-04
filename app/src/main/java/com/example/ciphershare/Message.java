package com.example.ciphershare;

import java.util.Date;

public class Message {
    private String message;
    private String sender;
    private String receiver;

    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    // Default constructor (required by Firebase for deserialization)
    public Message() {
    }

    public Message(String message, String sender, String receiver,String dateTime) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime=dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
