package com.example.bankcards.controller.card;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.change_status.ChangeStatusDto;
import com.example.bankcards.dto.create_card.CreateCardDto;
import com.example.bankcards.dto.create_card.CreateCardResponseDto;
import com.example.bankcards.dto.filter.CardFilterDto;
import com.example.bankcards.dto.transfer.CardCurrentBalanceDto;
import com.example.bankcards.dto.transfer.TransferRequestDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.card.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CardControllerImplTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Authentication auth = new TestingAuthenticationToken("user", "password", "USER");

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardControllerImpl cardController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createCardValidRequestReturnsCreated() throws Exception {
        CreateCardDto request = new CreateCardDto("1234567890123456", "John Doe", LocalDate.now().plusYears(2));
        CreateCardResponseDto response = new CreateCardResponseDto(
                "1234567890123456", "John Doe", LocalDate.now().plusYears(2),
                CardStatus.ACTIVE, BigDecimal.ZERO
        );

        when(cardService.createNewCard(any(CreateCardDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/card/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"))
                .andExpect(jsonPath("$.userName").value("John Doe"));
    }

    @Test
    void changeStatusValidRequestReturnsUpdatedStatus() throws Exception {
        ChangeStatusDto request = new ChangeStatusDto(1L, CardStatus.BLOCKED);
        ChangeStatusDto response = new ChangeStatusDto(1L, CardStatus.BLOCKED);

        when(cardService.changeStatus(any(ChangeStatusDto.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/card/status/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardId").value(1))
                .andExpect(jsonPath("$.cardStatus").value("BLOCKED"));
    }

    @Test
    void getAllCardReturnsCardList() throws Exception {
        CardDto card = new CardDto("1234567890123456", 1L, LocalDate.now().plusYears(1), CardStatus.ACTIVE);
        List<CardDto> response = Collections.singletonList(card);

        when(cardService.getAllCard()).thenReturn(response);

        mockMvc.perform(get("/api/v1/card/all/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"))
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void transferBetweenCardsValidRequestReturnsBalances() throws Exception {
        TransferRequestDto request = new TransferRequestDto(
                "1111222233334444",
                "5555666677778888",
                new BigDecimal("100.50")
        );

        CardCurrentBalanceDto source = new CardCurrentBalanceDto("1111222233334444", new BigDecimal("900.50"));
        CardCurrentBalanceDto target = new CardCurrentBalanceDto("5555666677778888", new BigDecimal("1100.50"));
        List<CardCurrentBalanceDto> response = List.of(source, target);

        when(cardService.transferBetweenCard(any(TransferRequestDto.class), any(Authentication.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/card/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1111222233334444"))
                .andExpect(jsonPath("$[0].balance").value(900.50))
                .andExpect(jsonPath("$[1].cardNumber").value("5555666677778888"))
                .andExpect(jsonPath("$[1].balance").value(1100.50));
    }

    @Test
    void getCardPageValidRequestReturnsPage() throws Exception {
        CardFilterDto filter = new CardFilterDto();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        CardDto card = new CardDto("1234567890123456", 1L, LocalDate.now().plusYears(1), CardStatus.ACTIVE);
        Page<CardDto> response = new PageImpl<>(Collections.singletonList(card), PageRequest.of(0, 10), 1);

        when(cardService.getCardPage(any(CardFilterDto.class), any(Authentication.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/card/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter))
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cardNumber").value("1234567890123456"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createCardInvalidRequestReturnsBadRequest() throws Exception {
        CreateCardDto invalidRequest = new CreateCardDto("123", "", null);

        mockMvc.perform(get("/api/v1/card/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void transferBetweenCardsInvalidAmountReturnsBadRequest() throws Exception {
        TransferRequestDto invalidRequest = new TransferRequestDto(
                "1111222233334444",
                "5555666677778888",
                new BigDecimal("-10.00")
        );

        mockMvc.perform(post("/api/v1/card/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .principal(auth))
                .andExpect(status().isBadRequest());
    }
}