package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое когда карта не найдена в системе.
 * Может возникнуть при поиске карты по номеру или идентификатору, который не существует.
 */
public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String message) {
        super(message);
    }
}
