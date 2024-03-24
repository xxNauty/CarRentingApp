package com.example.carrentingapp.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("select t from Token t where t.user.id = :id and t.status = :status")
    List<Token> findAllValidTokenByUser(UUID id, Token.JwtTokenStatus status);

    Optional<Token> findByToken(String token);
}
