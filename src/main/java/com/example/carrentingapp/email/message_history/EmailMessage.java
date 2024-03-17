package com.example.carrentingapp.email.message_history;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "email_message")
public class EmailMessage {

    @Id
    @GeneratedValue
    private UUID id;

    private String emailFrom;

    private String emailTo;

    private String subject;

    private String body;

    private LocalDateTime sentAt;

    private boolean sent;

    public EmailMessage(
            String emailFrom,
            String emailTo,
            String subject,
            String body,
            LocalDateTime sentAt,
            boolean sent
    ) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
        this.sent = sent;
    }
}
