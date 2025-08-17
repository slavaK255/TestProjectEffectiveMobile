package com.example.bankcards.dto.create_card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardDto {
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    private LocalDate expiryDate;
}
