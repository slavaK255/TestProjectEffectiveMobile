package com.example.bankcards.service.card;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.filter.CardFilterDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    CreateCardResponseDto createNewCard(CreateCardDto createCardDto);
    ChangeStatusDto changeStatus(ChangeStatusDto changeStatusDto);
    List<CardDto> getAllCard();
    List<CardCurrentBalanceDto> transferBetweenCard(TransferRequestDto transferRequestDto, Authentication authentication);
    Page<CardDto> getCardPage(CardFilterDto cardFilterDto, Authentication authentication);
}
