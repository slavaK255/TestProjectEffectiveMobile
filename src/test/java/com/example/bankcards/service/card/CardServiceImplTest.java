package com.example.bankcards.service.card;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.filter.CardFilterDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.InvalidExpiryDateException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.validation.CardValidation;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardValidation cardValidation;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private CardServiceImpl cardService;

    private CreateCardDto createSampleCreateCardDto() {
        return new CreateCardDto(
                "1234567812345678",
                "Иван Иванов",
                LocalDate.now().plusYears(2)
        );
    }

    private CreateCardResponseDto createSampleCreateCardResponseDto() {
        return new CreateCardResponseDto(
                "1234567812345678",
                "Иван Иванов",
                LocalDate.now().plusYears(2),
                CardStatus.ACTIVE,
                BigDecimal.ZERO
        );
    }

    private Card createSampleCard() {
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("1234567812345678");
        card.setExpiryDate(LocalDate.now().plusYears(2));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setUser(new User());
        return card;
    }

    private User createSampleUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("user123");
        return user;
    }

    private Card createCardWithBalance(String number, BigDecimal balance) {
        Card card = new Card();
        card.setId(number.equals("1111") ? 1L : 2L);
        card.setCardNumber(number);
        card.setBalance(balance);
        card.setUser(new User());
        return card;
    }

    private ChangeStatusDto createSampleChangeStatusDto() {
        return new ChangeStatusDto(1L, CardStatus.BLOCKED);
    }

    private TransferRequestDto createSampleTransferRequest() {
        return new TransferRequestDto("1111", "2222", BigDecimal.TEN);
    }

    private CardFilterDto createSampleCardFilter() {
        CardFilterDto filter = new CardFilterDto();
        filter.setPageNumber(0);
        filter.setPageSize(10);
        filter.setCardNumber("1234");
        filter.setCardStatus(CardStatus.ACTIVE);
        filter.setExpiryDateStart(LocalDate.now());
        filter.setExpiryDateEnd(LocalDate.now().plusYears(1));
        filter.setBalance(BigDecimal.valueOf(100));
        return filter;
    }

    @Test
    void createNewCardWithValidDataShouldCreateCard() {
        CreateCardDto request = createSampleCreateCardDto();
        Card card = createSampleCard();
        User user = createSampleUser();
        CreateCardResponseDto responseDto = createSampleCreateCardResponseDto();

        when(cardValidation.isExpiryDateValid(request)).thenReturn(true);
        when(cardRepository.existsByCardNumber(request.getCardNumber())).thenReturn(false);
        when(cardMapper.toEntity(request)).thenReturn(card);
        when(cardMapper.toCreateCardDtoWrapper(request)).thenReturn(request);
        when(userService.findByUserName(request.getUserName())).thenReturn(user);
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toCreateCardResponseDto(card)).thenReturn(responseDto);

        CreateCardResponseDto result = cardService.createNewCard(request);

        assertEquals(result, responseDto);
        verify(cardRepository).save(card);
        assertEquals(card.getUser(), user);
    }

    @Test
    void createNewCardWithInvalidExpiryDateShouldThrowException() {
        CreateCardDto request = createSampleCreateCardDto();

        when(cardValidation.isExpiryDateValid(request)).thenReturn(false);
        when(cardMapper.toCreateCardDtoWrapper(request)).thenReturn(request);

        assertThatThrownBy(() -> cardService.createNewCard(request))
                .isInstanceOf(InvalidExpiryDateException.class);

        verify(cardRepository, never()).save(any());
    }

    @Test
    void createNewCardWithExistingCardNumberShouldThrowException() {
        CreateCardDto request = createSampleCreateCardDto();

        when(cardValidation.isExpiryDateValid(request)).thenReturn(true);
        when(cardRepository.existsByCardNumber(request.getCardNumber())).thenReturn(true);
        when(cardMapper.toCreateCardDtoWrapper(request)).thenReturn(request);

        assertThatThrownBy(() -> cardService.createNewCard(request))
                .isInstanceOf(CardNotFoundException.class);

        verify(cardRepository, never()).save(any());
    }

    @Test
    void changeStatusWithExistingCardShouldUpdateStatus() {
        ChangeStatusDto request = createSampleChangeStatusDto();
        Card card = createSampleCard();

        when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));

        ChangeStatusDto result = cardService.changeStatus(request);

        assertEquals(result, request);
        assertEquals(result.getCardStatus(), CardStatus.BLOCKED);
    }

    @Test
    void changeStatusWithNonExistingCardShouldThrowException() {
        ChangeStatusDto request = createSampleChangeStatusDto();

        when(cardRepository.findById(request.getCardId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.changeStatus(request))
                .isInstanceOf(CardNotFoundException.class);

        verify(cardRepository, never()).save(any());
    }

    @Test
    void getAllCardShouldReturnAllCards() {
        Card card1 = createSampleCard();
        Card card2 = createSampleCard();
        card2.setCardNumber("8765432187654321");

        List<Card> cards = Arrays.asList(card1, card2);
        CardDto cardDto1 = new CardDto();
        CardDto cardDto2 = new CardDto();

        when(cardRepository.findAll()).thenReturn(cards);
        when(cardMapper.toDto(card1)).thenReturn(cardDto1);
        when(cardMapper.toDto(card2)).thenReturn(cardDto2);

        List<CardDto> result = cardService.getAllCard();

        assertEquals(2, result.size());
        assertTrue(result.contains(cardDto1));
        assertTrue(result.contains(cardDto2));
    }

    @Test
    void transferBetweenCardWithValidDataShouldUpdateBalances() {
        TransferRequestDto request = createSampleTransferRequest();
        Authentication auth = mock(Authentication.class);
        User user = createSampleUser();
        Card source = createCardWithBalance("1111", BigDecimal.valueOf(100));
        Card target = createCardWithBalance("2222", BigDecimal.ZERO);

        user.setCards(List.of(source, target));

        when(auth.getPrincipal()).thenReturn("user123");
        when(userService.findByLogin("user123")).thenReturn(user);
        when(cardValidation.isUserOwner(request, user)).thenReturn(true);
        when(cardMapper.toTransferRequestDtoWrapper(request)).thenReturn(request);
        when(cardValidation.isSourceCardHaveEnoughMoney(source, request.getAmount())).thenReturn(true);
        when(cardMapper.toCardCurrentBalanceDto(source)).thenReturn(new CardCurrentBalanceDto(source.getCardNumber(),source.getBalance()));
        when(cardMapper.toCardCurrentBalanceDto(target)).thenReturn(new CardCurrentBalanceDto(target.getCardNumber(),target.getBalance()));

        List<CardCurrentBalanceDto> result = cardService.transferBetweenCard(request, auth);

        assertEquals(source.getBalance(), new BigDecimal("90"));
        assertEquals(target.getBalance(), new BigDecimal("10"));
    }

    @Test
    void transferBetweenCardWithUserNotOwningCardsShouldThrowException() {
        TransferRequestDto request = createSampleTransferRequest();
        Authentication auth = mock(Authentication.class);
        User user = createSampleUser();

        when(auth.getPrincipal()).thenReturn("user123");
        when(userService.findByLogin("user123")).thenReturn(user);
        when(cardMapper.toTransferRequestDtoWrapper(request)).thenReturn(request);
        when(cardValidation.isUserOwner(request, user)).thenReturn(false);

        assertThatThrownBy(() -> cardService.transferBetweenCard(request, auth))
                .isInstanceOf(SecurityException.class);

        verify(cardRepository, never()).save(any());
    }

    @Test
    void transferBetweenCardWithInsufficientFundsShouldThrowException() {
        TransferRequestDto request = createSampleTransferRequest();
        Authentication auth = mock(Authentication.class);
        User user = createSampleUser();
        Card source = createCardWithBalance("1111", BigDecimal.ONE);
        Card target = createCardWithBalance("2222", BigDecimal.ZERO);

        user.setCards(List.of(source, target));

        when(auth.getPrincipal()).thenReturn("user123");
        when(userService.findByLogin("user123")).thenReturn(user);
        when(cardMapper.toTransferRequestDtoWrapper(request)).thenReturn(request);
        when(cardValidation.isUserOwner(request, user)).thenReturn(true);
        when(cardValidation.isSourceCardHaveEnoughMoney(source, request.getAmount())).thenReturn(false);

        assertThatThrownBy(() -> cardService.transferBetweenCard(request, auth))
                .isInstanceOf(InsufficientFundsException.class);

        verify(cardRepository, never()).save(any());
    }

    @Test
    void transferBetweenCardWithOptimisticLockingShouldRetry() {
        TransferRequestDto request = createSampleTransferRequest();
        Authentication auth = mock(Authentication.class);
        User user = createSampleUser();
        Card source = createCardWithBalance("1111", BigDecimal.valueOf(100));
        Card target = createCardWithBalance("2222", BigDecimal.ZERO);

        user.setCards(List.of(source, target));

        when(auth.getPrincipal()).thenReturn("user123");
        when(userService.findByLogin("user123")).thenReturn(user);
        when(cardMapper.toTransferRequestDtoWrapper(request)).thenReturn(request);
        when(cardValidation.isUserOwner(request, user)).thenReturn(true);
        when(cardValidation.isSourceCardHaveEnoughMoney(source, request.getAmount())).thenReturn(true);
        when(cardMapper.toCardCurrentBalanceDto(source)).thenReturn(new CardCurrentBalanceDto(source.getCardNumber(),source.getBalance()));
        when(cardMapper.toCardCurrentBalanceDto(target)).thenReturn(new CardCurrentBalanceDto(target.getCardNumber(),target.getBalance()));

        List<CardCurrentBalanceDto> result = cardService.transferBetweenCard(request, auth);


        assertEquals(source.getBalance(), new BigDecimal("90"));
        assertEquals(target.getBalance(), new BigDecimal("10"));
    }

    @Test
    void getCardPageWithValidFilterShouldReturnPage() {
        CardFilterDto filter = createSampleCardFilter();
        Authentication auth = mock(Authentication.class);
        User user = createSampleUser();
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());

        Card card = createSampleCard();
        Page<Card> cardPage = new PageImpl<>(List.of(card));
        Page<CardDto> dtoPage = new PageImpl<>(List.of(new CardDto()));

        when(auth.getPrincipal()).thenReturn("user123");
        when(userService.findByLogin("user123")).thenReturn(user);
        when(cardRepository.findFilteredCards(
                filter.getCardNumber(),
                filter.getCardStatus(),
                filter.getExpiryDateStart(),
                filter.getExpiryDateEnd(),
                filter.getBalance(),
                user.getId(),
                pageable
        )).thenReturn(cardPage);
        when(cardMapper.toDtoPage(cardPage)).thenReturn(dtoPage);

        Page<CardDto> result = cardService.getCardPage(filter, auth);

        assertEquals(result, dtoPage);
    }

    @Test
    void getCardPageWithNullPageParametersShouldUseDefaults() {
        CardFilterDto filter = new CardFilterDto();
        Authentication auth = mock(Authentication.class);
        User user = createSampleUser();
        Pageable defaultPageable = PageRequest.of(0, 10);

        Page<Card> cardPage = new PageImpl<>(Collections.emptyList());
        Page<CardDto> dtoPage = new PageImpl<>(Collections.emptyList());

        when(auth.getPrincipal()).thenReturn("user123");
        when(userService.findByLogin("user123")).thenReturn(user);
        when(cardRepository.findFilteredCards(
                null, null, null, null, null, user.getId(), defaultPageable
        )).thenReturn(cardPage);
        when(cardMapper.toDtoPage(cardPage)).thenReturn(dtoPage);

        Page<CardDto> result = cardService.getCardPage(filter, auth);

        assertEquals(result, dtoPage);
    }

}