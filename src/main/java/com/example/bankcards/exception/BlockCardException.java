package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при ошибках, связанных с запросами на блокировку карт.
 * Например, когда запрос на блокировку не найден или произошла ошибка при его обработке.
 */
public class BlockCardException extends RuntimeException{
    public BlockCardException(String message) {
        super(message);
    }
}
