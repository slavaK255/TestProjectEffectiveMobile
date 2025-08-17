package com.example.bankcards.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
public class CreateCardDto {
    @Size(min = 16, max = 16)
    private String cardNumber;
    @NotNull
    @NotBlank
    private String userName;

    private LocalDate expiryDate;
}
