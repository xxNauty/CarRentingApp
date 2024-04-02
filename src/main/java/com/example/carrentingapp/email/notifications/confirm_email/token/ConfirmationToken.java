package com.example.carrentingapp.email.notifications.confirm_email.token;

import com.example.carrentingapp.user.UserBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "email_confirm_verification_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    @Enumerated(EnumType.STRING)
    private ConfirmationTokenStatus status;

    @ManyToOne
    private UserBase user;

    public ConfirmationToken(
            String token,
            LocalDateTime expiredAt,
            UserBase user
    ) {
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
        this.user = user;
        this.status = ConfirmationTokenStatus.CONFIRMATION_TOKEN_SENT;
    }

    public enum ConfirmationTokenStatus {
        CONFIRMATION_TOKEN_SENT,
        CONFIRMATION_TOKEN_CONFIRMED,
        CONFIRMATION_TOKEN_EXPIRED
    }
}
