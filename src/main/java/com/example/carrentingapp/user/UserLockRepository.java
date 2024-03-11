package com.example.carrentingapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserLockRepository extends JpaRepository<UserLock, UUID> {

    @Query(value = "select l from UserLock l where l.user.id = :userID and l.isActive = true")
    Optional<UserLock> findAllActiveLockForUser(UUID userID);

}
