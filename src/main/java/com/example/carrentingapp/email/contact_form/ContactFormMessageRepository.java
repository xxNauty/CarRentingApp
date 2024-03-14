package com.example.carrentingapp.email.contact_form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactFormMessageRepository extends JpaRepository<ContactFormMessage, UUID> {
}
