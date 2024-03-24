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

    @Query(value = "select l from CarLock l where l.car.id = :carId and l.status = 'CAR_LOCK_ACTIVE'")
    Optional<CarLock> findActiveLockForCar(UUID carId);

    List<CarLock> findAllByStatus(CarLock.CarLockStatus status);


}
