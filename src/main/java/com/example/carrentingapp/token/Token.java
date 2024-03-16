package com.example.carrentingapp.token;

import com.example.carrentingapp.user.BaseUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    private boolean revoked;

    private boolean expired;

    @JsonIgnore
    @ManyToOne
    private BaseUser user;

    public Token(String token, BaseUser user) {
        this.token = token;
        this.revoked = false;
        this.expired = false;
        this.user = user;
    }
}
