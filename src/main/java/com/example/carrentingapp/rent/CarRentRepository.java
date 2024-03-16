package com.example.carrentingapp.rent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarRentRepository extends JpaRepository<CarRent, UUID> {
}