package com.example.carrentingapp.email.notifications.confirm_email.token;

import com.example.carrentingapp.user.UserBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {

    Optional<ConfirmationToken> findByToken(String token);

    Optional<ConfirmationToken> findByUser(UserBase user);

    @Query(value = "select t from ConfirmationToken t where t.user.id = :userId and t.status = 'CONFIRMATION_TOKEN_SENT'")
    List<ConfirmationToken> findAllUnusedForUser(UUID userId);
}
