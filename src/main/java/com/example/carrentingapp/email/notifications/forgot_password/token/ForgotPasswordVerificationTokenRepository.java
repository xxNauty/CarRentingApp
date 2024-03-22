package com.example.carrentingapp.email.notifications.forgot_password.token;

import com.example.carrentingapp.user.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForgotPasswordVerificationTokenRepository extends JpaRepository<ForgotPasswordVerificationToken, UUID> {

    Optional<ForgotPasswordVerificationToken> findByToken(String token);

    @Query(value = "select t from ForgotPasswordVerificationToken t where t.user.id = :userId and t.status = :status")
    Optional<ForgotPasswordVerificationToken> findActiveTokenForUser(UUID userId, ForgotPasswordVerificationToken.ForgotPasswordTokenStatus status);

    @Query(value = "select t from ForgotPasswordVerificationToken t where t.user.id = :userId and t.status = :status")
    List<ForgotPasswordVerificationToken> findAllTokensForUser(UUID userId, ForgotPasswordVerificationToken.ForgotPasswordTokenStatus status);

}
