package com.example.ecommerce_hvpp.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UserMessage {
    private String Text;
    private String Sender;
    private Date Date;
    private String DateString;


    public UserMessage(String text, String sender) {
        Text = text;
        Sender = sender;
    }

    public UserMessage(String text, String sender, java.util.Date date) {
        Text = text;
        Sender = sender;
        Date = date;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getDateString() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date.setTimeZone(TimeZone.getDefault());
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    public void setDateString(String dateString) {
        DateString = dateString;
    }
}
