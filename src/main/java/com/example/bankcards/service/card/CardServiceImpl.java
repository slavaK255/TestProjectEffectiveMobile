package com.example.bankcards.service.card;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.filter.CardFilterDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.InvalidExpiryDateException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.validation.CardValidation;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardValidation cardValidation;
    private final CardMapper cardMapper;
    private final UserService userService;

    @Override
    @Transactional
    public CreateCardResponseDto createNewCard(CreateCardDto createCardDto) {
        CreateCardDto wrapper = cardMapper.toCreateCardDtoWrapper(createCardDto);
        log.info("Create new card : " + wrapper.toString());

        if (!cardValidation.isExpiryDateValid(createCardDto)) {
            throw new InvalidExpiryDateException("Дата создания карты должна");
        }
        if (cardRepository.existsByCardNumber(createCardDto.getCardNumber())) {
            throw new CardNotFoundException("такая карта уже существует");
        }

        Card card = cardMapper.toEntity(createCardDto);
        User user = userService.findByUserName(createCardDto.getUserName());
        card.setUser(user);

        return cardMapper.toCreateCardResponseDto(cardRepository.save(card));
    }

    @Override
    @Transactional
    public ChangeStatusDto changeStatus(ChangeStatusDto changeStatusDto) {
        log.info("Update card info : " + changeStatusDto.toString());

        Card card = cardRepository.findById(changeStatusDto.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Такой Карты не существует"));
        card.setStatus(changeStatusDto.getCardStatus());

        return changeStatusDto;
    }

    @Override
    @Transactional
    public List<CardDto> getAllCard() {
        log.info("Getting all card from db");

        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map(cardMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @Retryable(
            value = OptimisticLockException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 2))
    public List<CardCurrentBalanceDto> transferBetweenCard(TransferRequestDto transferRequestDto, Authentication authentication) {
        TransferRequestDto wrapper = cardMapper.toTransferRequestDtoWrapper(transferRequestDto);
        log.info("make money transfer between two card : " + wrapper.toString());

        User user = userService.findByLogin(authentication.getPrincipal().toString());

        if(!cardValidation.isUserOwner(transferRequestDto, user)){
            throw new SecurityException("Карты не принадлежат данному пользователю");
        }

        Card sourceCard = getCardByNumber(user, transferRequestDto.getSourceCard());
        Card targetCard = getCardByNumber(user, transferRequestDto.getTargetCard());

        BigDecimal amount = transferRequestDto.getAmount();

        if(!cardValidation.isSourceCardHaveEnoughMoney(sourceCard, amount)){
            throw new InsufficientFundsException("Не достаточно денег на балансе");
        }

        sourceCard.setBalance(sourceCard.getBalance().subtract(amount));
        targetCard.setBalance(targetCard.getBalance().add(amount));

        return List.of(
                cardMapper.toCardCurrentBalanceDto(sourceCard),
                cardMapper.toCardCurrentBalanceDto(targetCard)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getCardPage(CardFilterDto cardFilterDto, Authentication authentication) {
        User user = userService.findByLogin(authentication.getPrincipal().toString());

        Pageable pageable = PageRequest.of(
                cardFilterDto.getPageNumber() == null ? 0 : cardFilterDto.getPageNumber(),
                cardFilterDto.getPageSize() == null ? 10 : cardFilterDto.getPageSize());

        Page<Card> cardPage = cardRepository.findFilteredCards(
                cardFilterDto.getCardNumber(),
                cardFilterDto.getCardStatus(),
                cardFilterDto.getExpiryDateStart(),
                cardFilterDto.getExpiryDateEnd(),
                cardFilterDto.getBalance(),
                user.getId(),
                pageable
        );


        return cardMapper.toDtoPage(cardPage);
    }

    private Card getCardByNumber(User user, String cardNumber) {
        return user.getCards().stream()
                .filter(card -> card.getCardNumber().equals(cardNumber))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException("У пользователя не существует такой карты"));
    }
}
