package com.example.carrentingapp.car;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<BaseCar, UUID> {

    List<BaseCar> findByIsAvailable(Boolean isAvailable);

}
