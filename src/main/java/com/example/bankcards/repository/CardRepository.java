package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);

    @Query("SELECT c FROM Card c " +
            "WHERE " +
            "c.user.id = :userId " +
            "AND (:cardNumber IS NULL OR c.cardNumber LIKE %:cardNumber%) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:expiryDateStart IS NULL OR c.expiryDate >= :expiryDateStart) " +
            "AND (:expiryDateEnd IS NULL OR c.expiryDate <= :expiryDateEnd)" +
            "AND (:balance IS NULL OR c.balance >= :balance)")
    Page<Card> findFilteredCards(
            @Param("cardNumber") String cardNumber,
            @Param("status") CardStatus status,
            @Param("expiryDateStart") LocalDate expiryDateStart,
            @Param("expiryDateEnd") LocalDate expiryDateEnd,
            @Param("balance") BigDecimal balance,
            @Param("userId") Long userId,
            Pageable pageable
    );
}
