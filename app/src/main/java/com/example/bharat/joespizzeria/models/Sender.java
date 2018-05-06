package com.example.bharat.joespizzeria.models;

import android.app.Notification;

/**
 * Created by bharat on 3/1/18.
 */

public class Sender {

    public String to;
    public com.example.bharat.joespizzeria.models.Notification notification;

    public Sender(String to, com.example.bharat.joespizzeria.models.Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public Sender() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public com.example.bharat.joespizzeria.models.Notification getNotification() {
        return notification;
    }

    public void setNotification(com.example.bharat.joespizzeria.models.Notification notification) {
        this.notification = notification;
    }
}
