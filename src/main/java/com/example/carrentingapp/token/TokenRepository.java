package com.example.carrentingapp.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = "select t from Token t inner join UserBase u on t.user.id = u.id where u.id = :id and t.status = :status ")
    List<Token> findAllValidTokenByUser(UUID id, Token.JwtTokenStatus status);

    Optional<Token> findByToken(String token);
}
