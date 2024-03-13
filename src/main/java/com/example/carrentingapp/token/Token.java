package com.example.carrentingapp.token;

import com.example.carrentingapp.user.BaseUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_jwt_token")
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id"
//)
public class Token {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String token;
//    private String tokenType = "BEARER";
    private boolean revoked;
    private boolean expired;
    @ManyToOne
    private BaseUser user;
}
