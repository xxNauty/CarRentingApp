package com.example.carrentingapp.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<BaseUser, UUID> {
}
