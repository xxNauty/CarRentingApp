package com.example.carrentingapp.email.notifications.forgot_password.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordVerificationTokenRepository extends JpaRepository<ForgotPasswordVerificationToken, UUID> {

    Optional<ForgotPasswordVerificationToken> findByToken(String token);

}
