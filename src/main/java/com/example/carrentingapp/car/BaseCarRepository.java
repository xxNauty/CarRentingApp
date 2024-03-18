package com.example.carrentingapp.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BaseCarRepository extends JpaRepository<BaseCar, UUID> {

    @Query(value = "select c from BaseCar c where c.status = :status")
    List<BaseCar> findByStatus(BaseCar.CarStatus status);

}
