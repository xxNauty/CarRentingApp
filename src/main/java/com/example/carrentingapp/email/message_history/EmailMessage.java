package com.example.carrentingapp.email.message_history;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Length;
import org.springframework.stereotype.Component;

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

    @Column(length = 1000)
    private String body;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private EmailMessageStatus status;

    public EmailMessage(
            String emailFrom,
            String emailTo,
            String subject,
            String body
    ) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.subject = subject;
        this.body = body;
    }

    public enum EmailMessageStatus{
        EMAIL_SENT,
        EMAIL_ERROR
    }

    public enum EmailMessageType{
        NOTIFICATION,
        CONTACT_FORM
    }
}
