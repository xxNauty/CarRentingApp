package com.example.carrentingapp.car;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_base")
public class BaseCar {

    @Id
    @GeneratedValue
    private UUID id;

    private String brand;

    private String model;

    private Integer yearOfProduction;

    private Float mileage;

    private Float power;

    private Float torque;

    private Float engineSize;

    private Float averageFuelConsumption;

    private Float minRankOfUser;

    private Float pricePerDay;

    private Boolean hasActiveSale;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private List<CarLock> locks;

    //todo: mechanizm pilnujący by w danym momencie była tylko jedna aktywna blokada
    public CarLock getActiveLock(){
        for(CarLock lock : locks){
            if (lock.getStatus().equals(CarLock.CarLockStatus.CAR_LOCK_ACTIVE)){
                return lock;
            }
        }
        return null;
    }

    public enum CarStatus{
        CAR_READY,
        CAR_LOCKED,
        CAR_RENTED
    }

}
