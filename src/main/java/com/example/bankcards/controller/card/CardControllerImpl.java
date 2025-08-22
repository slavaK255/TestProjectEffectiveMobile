package com.example.bankcards.controller.card;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.filter.CardFilterDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import com.example.bankcards.service.card.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/card")
public class CardControllerImpl implements CardController {

    private final CardService cardService;

    @PostMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CreateCardResponseDto createCard(@Valid @RequestBody CreateCardDto createCardDto) {
        return cardService.createNewCard(createCardDto);
    }

    @PatchMapping("status/change")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ChangeStatusDto changeStatus(@Valid @RequestBody ChangeStatusDto changeStatusDto) {
        return cardService.changeStatus(changeStatusDto);
    }

    @GetMapping("all/get")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CardDto> getAllCard() {
        return cardService.getAllCard();
    }

    @PostMapping("transfer")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public List<CardCurrentBalanceDto> transferBetweenCards(
            @Valid @RequestBody TransferRequestDto transferRequestDto,
            Authentication authentication) {
        return cardService.transferBetweenCard(transferRequestDto,authentication);
    }

    @GetMapping("page")
    @PreAuthorize("hasAuthority('USER')")
    public Page<CardDto> getCardPage(@RequestBody CardFilterDto cardFilterDto, Authentication authentication){
        return cardService.getCardPage(cardFilterDto, authentication);
    }

}
