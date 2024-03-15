package com.example.carrentingapp.email.notifications.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, UUID> {
}
