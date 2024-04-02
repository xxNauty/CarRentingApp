package com.example.carrentingapp.token;

import com.example.carrentingapp.user.UserBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_jwt_token")
public class Token {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private JwtTokenStatus status;

    @JsonIgnore
    @ManyToOne
    private UserBase user;

    public Token(String token, UserBase user) {
        this.token = token;
        this.status = JwtTokenStatus.JWT_TOKEN_ACTIVE;
        this.user = user;
    }

    public enum JwtTokenStatus {
        JWT_TOKEN_ACTIVE,
        JWT_TOKEN_EXPIRED
    }
}
