package com.example.carrentingapp.email.notifications.forgot_password.token;

import com.example.carrentingapp.user.BaseUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "forgot_password_verification_token")
public class ForgotPasswordVerificationToken {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime usedAt;

    @ManyToOne
    private BaseUser user;

    public ForgotPasswordVerificationToken(
            String token,
            LocalDateTime expiredAt,
            BaseUser user
    ) {
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
        this.user = user;
    }
}
