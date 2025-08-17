package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BlockCardRequestDto {
    @NotNull
    private Long card_id;

    @NotNull
    @NotBlank
    private String reason;
}
