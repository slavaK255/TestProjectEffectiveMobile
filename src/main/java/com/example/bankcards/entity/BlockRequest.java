package com.example.bankcards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность, представляющая запрос на блокировку карты.
 * Хранит информацию о причине блокировки, статусе выполнения запроса и id карты.
 */
@Entity
@Table(name = "block_request")
@Getter
@Setter
@EqualsAndHashCode
public class BlockRequest {
    /**
     * Уникальный идентификатор запроса на блокировку.
     * Генерируется автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор карты, которую необходимо заблокировать.
     * Не может быть null.
     */
    @Column(name = "card_id", nullable = false)
    private Long card_id;

    /**
     * Причина блокировки карты.
     * Максимальная длина - 4096 символов.
     * Не может быть null.
     */
    @Column(name = "reason", nullable = false, length = 4096)
    private String reason;

    /**
     * Флаг, указывающий был ли выполнен запрос на блокировку.
     * По умолчанию false.
     */
    @Column(name = "executed")
    private boolean isExecuted = false;
}
