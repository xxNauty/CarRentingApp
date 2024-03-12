package com.example.carrentingapp.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserLock {

    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Reason reason;
    @Enumerated(EnumType.STRING)
    private LockType type;
//    @Column
    private LocalDate expirationDate;
    @ManyToOne
    private BaseUser user;
    private Boolean isActive;

    public UserLock(
            Reason reason,
            LockType type,
            LocalDate expirationDate,
            BaseUser user
    ) {
        this.reason = reason;
        this.type = type;
        this.expirationDate = expirationDate;
        this.user = user;
        this.isActive = true;
    }

    @RequiredArgsConstructor
    public enum Reason {
        FREQUENT_DELAYED_RETURNS("częste-opóźnienia-ze-zwrotami"),
        DAMAGED_CAR("uszkodzenie-samochodu"),
        DESTROYED_CAR("zniszczenie-samochodu"),
        OTHER("inne");

        private final String reason;
    }

    public enum LockType {
        TEMPORARY,
        FOREVER
    }


}