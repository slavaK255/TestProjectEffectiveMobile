package com.example.bankcards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность, представляющая банковскую карту.
 * Содержит информацию о номере карты, владельце, сроке действия, статусе и балансе.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "user")
public class Card {
    /**
     * Уникальный идентификатор карты.
     * Генерируется автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Номер банковской карты.
     * Не может быть null.
     * Длина должна быть ровно 16
     */
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    /**
     * Пользователь-владелец карты.
     * Связь Many-to-One с сущностью User.
     * Не может быть null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Дата окончания срока действия карты.
     * Формат: ГГГГ-ММ-ДД.
     * Не может быть null.
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    /**
     * Текущий статус карты.
     * Возможные значения: ACTIVE, BLOCKED, EXPIRED, DELETED.
     * По умолчанию: ACTIVE.
     */
    @Enumerated(EnumType.STRING)
    private CardStatus status = CardStatus.ACTIVE;

    /**
     * Текущий баланс карты.
     * По умолчанию: 0.
     */
    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Версия записи для оптимистичной блокировки.
     */
    @Version
    private Long version;
}
