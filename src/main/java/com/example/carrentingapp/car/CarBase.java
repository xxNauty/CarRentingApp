package com.example.carrentingapp.car;

import jakarta.persistence.*;
import lombok.*;

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
        return Objects.equals(id, car.id) &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model) &&
                Objects.equals(yearOfProduction, car.yearOfProduction) &&
                Objects.equals(mileage, car.mileage) &&
                Objects.equals(power, car.power) &&
                Objects.equals(torque, car.torque) &&
                Objects.equals(engineSize, car.engineSize) &&
                Objects.equals(averageFuelConsumption, car.averageFuelConsumption) &&
                Objects.equals(minRankOfUser, car.minRankOfUser) &&
                Objects.equals(pricePerDay, car.pricePerDay) &&
                Objects.equals(hasActiveSale, car.hasActiveSale) &&
                status == car.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, yearOfProduction, mileage, power, torque, engineSize,
                averageFuelConsumption, minRankOfUser, pricePerDay, hasActiveSale, status);
    }

    public enum CarStatus{
        CAR_READY,
        CAR_LOCKED,
        CAR_RENTED
    }

}
