package com.example.carrentingapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserBaseRepository extends JpaRepository<UserBase, UUID> {

    Optional<UserBase> findByEmail(String email);

    Optional<UserBase> findByEmailAndId(String email, UUID id);
}
