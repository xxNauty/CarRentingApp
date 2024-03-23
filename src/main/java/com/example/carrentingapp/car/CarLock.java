package com.example.carrentingapp.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "car_lock")
public class CarLock {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JsonIgnore
    private CarBase car;

    @Enumerated(EnumType.STRING)
    private CarLockReason reason;

    private LocalDate lockedTo;

    @Enumerated(EnumType.STRING)
    private CarLockStatus status;

    public CarLock(CarBase car, CarLockReason reason, LocalDate lockedTo) {
        this.car = car;
        this.reason = reason;
        this.lockedTo = lockedTo;
        this.status = CarLockStatus.CAR_LOCK_ACTIVE;
    }

    public enum CarLockReason {
        REGULAR_SERVICE,
        FAILURE,
        TUNING,
        OTHER
    }

    public enum CarLockStatus{
        CAR_LOCK_ACTIVE,
        CAR_LOCK_NOT_ACTIVE,
        CAR_LOCK_EXTENDED
    }

}