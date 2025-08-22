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
/**
 * Контроллер для управления банковскими картами.
 * Предоставляет API для создания карт, изменения их статуса, просмотра и перевода средств между картами.
 */
@Tag(name = "Card management", description = "Operations with cards")
public interface CardController {

    /**
     * Создает новую банковскую карту для указанного пользователя.
     * Требует прав администратора.
     *
     * @param createCardDto DTO с данными для создания карты
     * @return DTO с информацией о созданной карте
     */
    @Operation(
            summary = "Create new card",
            description = "Creates a new bank card for the specified user"
    )
    CreateCardResponseDto createCard(CreateCardDto createCardDto);
    /**
     * Обрабатывает PATCH запрос для изменения статуса карты.
     * Требует валидных данных и аутентификации с ролью ADMIN.
     *
     * @param changeStatusDto DTO с данными для изменения статуса
     * @return DTO с обновленным статусом карты
     */
    @Operation(
            summary = "Change card status",
            description = "Updates the status of an existing card (ACTIVE, BLOCKED, EXPIRED, DELETED)"
    )
    ChangeStatusDto changeStatus(ChangeStatusDto changeStatusDto);

    /**
     * Обрабатывает GET запрос для получения всех карт в системе.
     * Требует аутентификации с ролью ADMIN.
     *
     * @return список DTO всех карт
     */
    @Operation(
            summary = "Get all cards",
            description = "Retrieves a list of all cards in the system (admin only)"
    )
    List<CardDto> getAllCard();

    /**
     * Обрабатывает POST запрос для перевода средств между картами.
     * Требует валидных данных и аутентификации с ролью USER или ADMIN.
     *
     * @param transferRequestDto DTO с данными для перевода
     * @param authentication объект аутентификации текущего пользователя
     * @return список DTO с обновленными балансами карт
     */
    @Operation(
            summary = "Transfer between cards",
            description = "Transfers money between two cards belonging to the authenticated user"
    )
    List<CardCurrentBalanceDto> transferBetweenCards(
            TransferRequestDto transferRequestDto,
            Authentication authentication);
    /**
     * Обрабатывает GET запрос для получения страницы с отфильтрованными картами.
     * Требует аутентификации с ролью USER.
     *
     * @param cardFilterDto DTO с параметрами фильтрации и пагинации
     * @param authentication объект аутентификации текущего пользователя
     * @return страница с отфильтрованными картами
     */
    @Operation(
            summary = "Get filtered cards page",
            description = "Retrieves a paginated and filtered list of cards"
    )
    Page<CardDto> getCardPage(CardFilterDto cardFilterDto, Authentication authentication);
}
