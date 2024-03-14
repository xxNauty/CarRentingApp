package com.example.carrentingapp.email.sender;

public interface EmailSender {
    void send(String to, String from, String subject,  String email);
}
