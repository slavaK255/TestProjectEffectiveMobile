package com.example.bankcards.dto.change_status;

import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusDto {
    @NotNull
    private Long cardId;
    @NotNull
    private CardStatus cardStatus;
}
