package com.example.carrentingapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLockRepository extends JpaRepository<UserLock, UUID> {

    @Query(value = "select l from UserLock l where l.user.id = :id and l.status = :status")
    Optional<UserLock> findAllByStatusAndUser(UUID id, UserLock.UserLockStatus status);

    List<UserLock> findAllByStatus(UserLock.UserLockStatus status);

}
