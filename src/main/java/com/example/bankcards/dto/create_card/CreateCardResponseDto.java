package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class CreateCardResponseDto {
    private String cardNumber;
    private String userName;
    private LocalDate expiryDate;
    private CardStatus status;
    private BigDecimal balance;
}
