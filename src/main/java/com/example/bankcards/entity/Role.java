package com.example.bankcards.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление ролей пользователей в системе.
 * Реализует интерфейс GrantedAuthority для интеграции с Spring Security.
 */
public enum Role implements GrantedAuthority {

    /**
     * Администратор системы. Имеет расширенные права доступа.
     */
    ADMIN,

    /**
     * Обычный пользователь системы.
     */
    USER;

    /**
     * Возвращает название роли в виде строки для Spring Security.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
