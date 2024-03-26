package com.example.carrentingapp.email.notifications;

public interface NotificationSender {
    void sendEmail(NotificationRequest request);
}
