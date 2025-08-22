package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при ошибках работы с пользователями.
 * Может возникнуть при попытке создания пользователя с существующим логином,
 * поиске несуществующего пользователя или других операциях с пользователями.
 */
public class WorkWithUserException extends RuntimeException{
    public WorkWithUserException(String message) {
        super(message);
    }
}
