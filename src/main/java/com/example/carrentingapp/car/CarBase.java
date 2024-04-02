package com.example.carrentingapp.car;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_base")
public class CarBase {

    @Id
    @GeneratedValue
    private UUID id;

    private String brand;

    private String model;

    private Integer yearOfProduction;

    @Column(precision = 2)
    private Float mileage;

    @Column(precision = 1)
    private Float power;

    @Column(precision = 1)
    private Float torque;

    @Column(precision = 2)
    private Float engineSize;

    @Column(precision = 1)
    private Float averageFuelConsumption;

    @Column(precision = 1)
    private Float minRankOfUser;

    @Column(precision = 2)
    private Float pricePerDay;

    private Boolean hasActiveSale;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private List<CarLock> locks;

    private LocalDate unavailableTo;

    //todo: mechanizm pilnujący by w danym momencie była tylko jedna aktywna blokada
    public CarLock getActiveLock(){
        for(CarLock lock : locks){
            if (lock.getStatus().equals(CarLock.CarLockStatus.CAR_LOCK_ACTIVE)){
                return lock;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BaseCar{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", yearOfProduction=" + yearOfProduction +
                ", mileage=" + mileage +
                ", power=" + power +
                ", torque=" + torque +
                ", engineSize=" + engineSize +
                ", averageFuelConsumption=" + averageFuelConsumption +
                ", minRankOfUser=" + minRankOfUser +
                ", pricePerDay=" + pricePerDay +
                ", hasActiveSale=" + hasActiveSale +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarBase car = (CarBase) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum CarStatus{
        CAR_READY,
        CAR_LOCKED,
        CAR_RENTED
    }

}
