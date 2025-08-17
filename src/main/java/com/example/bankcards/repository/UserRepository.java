package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"cards"})
    Optional<User> findByLogin(String login);

    @EntityGraph(attributePaths = {"cards"})
    Optional<User> findByUserName(String userName);

    boolean existsByLogin(String login);
}
