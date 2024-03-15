package com.example.carrentingapp.car;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car_base")
public class BaseCar {

    @Id
    @GeneratedValue
    private UUID id;

    private String brand;

    private String model;

    private Integer yearOfProduction;

    private Float mileage;

    private Boolean isRented;

    private Float power;

    private Float torque;

    private Float engineSize;

    private Float averageFuelConsumption;

    private Float minRankOfUser;

    private Float pricePerDay;

    private Boolean hasActiveSale;

    private Boolean isAvailable;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER) //todo: FetchType Eager vs Lazy
    private List<CarLock> locks;

    public CarLock getActiveLock(){
        for(CarLock lock : locks){
            if (lock.getIsActive()){
                return lock;
            }
        }
        return null;
    }

}
