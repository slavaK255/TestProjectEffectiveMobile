package com.example.bankcards.controller.card;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.filter.CardFilterDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

@Tag(name = "Card management", description = "Operations with cards")
public interface CardController {

    @Operation(
            summary = "Create new card",
            description = "Creates a new bank card for the specified user"
    )
    CreateCardResponseDto createCard(CreateCardDto createCardDto);

    @Operation(
            summary = "Change card status",
            description = "Updates the status of an existing card (ACTIVE, BLOCKED, EXPIRED, DELETED)"
    )
    ChangeStatusDto changeStatus(ChangeStatusDto changeStatusDto);

    @Operation(
            summary = "Get all cards",
            description = "Retrieves a list of all cards in the system (admin only)"
    )
    List<CardDto> getAllCard();

    @Operation(
            summary = "Transfer between cards",
            description = "Transfers money between two cards belonging to the authenticated user"
    )
    List<CardCurrentBalanceDto> transferBetweenCards(
            TransferRequestDto transferRequestDto,
            Authentication authentication);

    @Operation(
            summary = "Get filtered cards page",
            description = "Retrieves a paginated and filtered list of cards"
    )
    Page<CardDto> getCardPage(CardFilterDto cardFilterDto, Authentication authentication);
}
