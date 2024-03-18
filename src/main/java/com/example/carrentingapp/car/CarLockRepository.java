package com.example.carrentingapp.car;

import com.example.carrentingapp.user.UserLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarLockRepository extends JpaRepository<CarLock, UUID> {

    @Query(value = "select l from CarLock l where l.car.id = :carId and l.status = :status")
    Optional<CarLock> findAllActiveLocksForCar(UUID carId, CarLock.CarLockStatus status);

    List<CarLock> findAllByStatus(CarLock.CarLockStatus status);


}
