package com.example.carrentingapp.email.notifications.confirm_email.token;

import com.example.carrentingapp.user.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {

    Optional<ConfirmationToken> findByToken(String token);

    Optional<ConfirmationToken> findByUser(BaseUser user);
}
