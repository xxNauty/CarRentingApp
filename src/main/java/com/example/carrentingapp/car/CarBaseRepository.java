package com.example.carrentingapp.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarBaseRepository extends JpaRepository<CarBase, UUID> {

    @Query(value = "select c from CarBase c where c.status = :status")
    List<CarBase> findByStatus(CarBase.CarStatus status);

}
