package com.example.bankcards.dto.block_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlockCardDto {
    @NotNull
    private Long card_id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 4096)
    private String reason;

    private boolean isExecuted;
}
