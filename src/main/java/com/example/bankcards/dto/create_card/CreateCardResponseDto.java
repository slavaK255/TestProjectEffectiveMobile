package com.example.bankcards.dto.create_card;

import com.example.bankcards.entity.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateCardResponseDto {
    private String cardNumber;
    private String userName;
    private LocalDate expiryDate;
    private CardStatus status;
    private BigDecimal balance;
}
