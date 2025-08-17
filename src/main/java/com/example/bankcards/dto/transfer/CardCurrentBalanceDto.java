package com.example.bankcards.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CardCurrentBalanceDto {
    private String cardNumber;
    private BigDecimal balance;
}
