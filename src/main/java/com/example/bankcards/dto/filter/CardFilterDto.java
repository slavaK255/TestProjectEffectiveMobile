package com.example.bankcards.dto.filter;

import com.example.bankcards.entity.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardFilterDto {
    private String cardNumber;
    private LocalDate expiryDateStart;
    private LocalDate expiryDateEnd;
    private CardStatus cardStatus;
    private BigDecimal balance;
    private Integer pageNumber;
    private Integer pageSize;
}
