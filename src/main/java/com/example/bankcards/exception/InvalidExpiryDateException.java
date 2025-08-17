package com.example.bankcards.exception;

public class InvalidExpiryDateException extends RuntimeException{
    public InvalidExpiryDateException(String message) {
        super(message);
    }
}
