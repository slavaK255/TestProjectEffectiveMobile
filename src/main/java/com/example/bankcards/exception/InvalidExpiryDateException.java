package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое когда указана невалидная дата expiration карты.
 * Дата expiration должна быть в будущем и не превышать 8 лет от текущей даты.
 */
public class InvalidExpiryDateException extends RuntimeException{
    public InvalidExpiryDateException(String message) {
        super(message);
    }
}
