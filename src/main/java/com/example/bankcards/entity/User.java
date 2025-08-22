package com.example.bankcards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Сущность, представляющая пользователя системы.
 * Содержит учетные данные и личную информацию пользователя.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_jwt")
@EqualsAndHashCode(exclude = "cards")
public class User {
    /**
     * Уникальный идентификатор пользователя.
     * Генерируется автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальный логин пользователя для входа в систему.
     * Максимальная длина - 64 символа.
     * Не может быть null.
     */
    @Column(name = "login", length = 64, nullable = false, unique = true)
    private String login;

    /**
     * Имя пользователя.
     * Максимальная длина - 128 символов.
     * Не может быть null.
     */
    @Column(name = "user_name", length = 128, nullable = false)
    private String userName;

    /**
     * Пароль пользователя.
     * Максимальная длина - 128 символов.
     * Не может быть null.
     */
    @Column(name = "password", length = 128, nullable = false)
    private String password;

    /**
     * Роль пользователя в системе.
     * Определяет уровень доступа к функционалу.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Список банковских карт, привязанных к пользователю.
     * Связь One-to-Many с сущностью Card.
     */
    @OneToMany(mappedBy = "user")
    private List<Card> cards;

    /**
     * Версия записи для оптимистичной блокировки.
     */
    @Version
    private Long version;
}