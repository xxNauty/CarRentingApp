package com.example.carrentingapp.contact_form;

import com.example.carrentingapp.user.BaseUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contact_form_message")
public class ContactFormMessage {

    @Id
    @GeneratedValue
    private UUID id;

    private String senderEmail;

    private String recipientEmail;

    private String subject;

    private String body;

    private LocalDateTime createdAt;

    public ContactFormMessage(
            String senderEmail,
            String recipientEmail,
            String subject,
            String body
    ) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.createdAt = LocalDateTime.now();
    }
}