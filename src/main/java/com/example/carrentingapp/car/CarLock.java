package com.example.carrentingapp.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CarLock {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JsonIgnore
    private BaseCar car;
    @Enumerated(EnumType.STRING)
    private CarReason reason;
    private LocalDate lockedTo;
    private Boolean isActive;

    public CarLock(BaseCar car, CarReason reason, LocalDate lockedTo, Boolean isActive) {
        this.car = car;
        this.reason = reason;
        this.lockedTo = lockedTo;
        this.isActive = isActive;
    }

    public enum CarReason {
        REGULAR_SERVICE,
        FAILURE,
        TUNING,
        OTHER
    }

}