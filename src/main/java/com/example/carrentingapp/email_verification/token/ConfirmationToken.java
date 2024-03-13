package com.example.carrentingapp.email_verification.token;

import com.example.carrentingapp.user.BaseUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_email_confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue
    private UUID id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    private BaseUser user;

    public ConfirmationToken(
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiredAt,
            BaseUser user
    ) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.user = user;
    }
}
