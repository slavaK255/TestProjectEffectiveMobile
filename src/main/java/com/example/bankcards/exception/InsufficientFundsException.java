package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое когда на карте недостаточно средств для выполнения операции.
 * Например, при попытке перевода суммы, превышающей текущий баланс карты.
 */
public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
