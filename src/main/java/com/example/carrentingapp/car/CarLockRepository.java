package com.example.carrentingapp.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CarLockRepository extends JpaRepository<CarLock, UUID> {

    @Query(value = "select l from CarLock l where l.car.id = :carId and l.isActive = true")
    Optional<CarLock> findAllActiveLocksForCar(UUID carId);

}
