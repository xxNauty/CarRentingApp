package com.example.carrentingapp.email.sender;

import com.example.carrentingapp.email.message_history.EmailMessage;

public interface EmailSender {
    void send(String to, String from, String subject,  String email, EmailMessage.EmailMessageType type);
}
