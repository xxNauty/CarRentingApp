package com.example.carrentingapp.user;

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
@Builder
@EqualsAndHashCode
@Table(name = "user_lock")
public class UserLock {

    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Reason reason;
    @Enumerated(EnumType.STRING)
    private LockType type;
    private LocalDate expirationDate;
    @JsonIgnore
    @ManyToOne
    private BaseUser user;

    @Enumerated(EnumType.STRING)
    private UserLockStatus status;

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
        this.status = UserLockStatus.USER_LOCK_ACTIVE;
    }

    public enum Reason {
        FREQUENT_DELAYED_RETURNS,
        DAMAGED_CAR,
        DESTROYED_CAR,
        OTHER;
    }

    public enum LockType {
        TEMPORARY,
        FOREVER
    }

    public enum UserLockStatus{
        USER_LOCK_ACTIVE,
        USER_LOCK_NOT_ACTIVE
    }


}
