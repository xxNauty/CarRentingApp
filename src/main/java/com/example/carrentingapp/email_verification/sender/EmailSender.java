package com.example.carrentingapp.email_verification.sender;

public interface EmailSender {
    void send(String to, String subject,  String email);
}
