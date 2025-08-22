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
import com.example.bankcards.exception.InvalidExpiryDateException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import jakarta.persistence.OptimisticLockException;

import java.util.List;

/**
 * Сервис для управления банковскими картами.
 * Обеспечивает создание карт, изменение их статуса, получение списка карт,
 * перевод средств между картами и фильтрацию карт с пагинацией.
 * Все операции защищены проверками валидности и прав доступа.
 */
public interface CardService {

    /**
     * Создает новую банковскую карту для указанного пользователя.
     * Проверяет валидность даты expiration и уникальность номера карты.
     *
     * @param createCardDto CreateCardDto с данными для создания карты
     * @return CreateCardResponseDto с информацией о созданной карте
     * @throws InvalidExpiryDateException если указана невалидная дата expiration
     * @throws CardNotFoundException если карта с таким номером уже существует
     */
    CreateCardResponseDto createNewCard(CreateCardDto createCardDto);

    /**
     * Изменяет статус существующей карты.
     *
     * @param changeStatusDto ChangeStatusDto с данными для изменения статуса
     * @return ChangeStatusDto с обновленным статусом карты
     * @throws CardNotFoundException если карта с указанным ID не найдена
     */
    ChangeStatusDto changeStatus(ChangeStatusDto changeStatusDto);

    /**
     * Возвращает список всех карт в системе.
     *
     * @return список всех карт
     */
    List<CardDto> getAllCard();

    /**
     * Выполняет перевод денежных средств между картами, принадлежащими аутентифицированному пользователю.
     * Использует оптимистическую блокировку с повторными попытками при возникновении конфликтов.
     *
     * @param transferRequestDto TransferRequestDto с данными для перевода
     * @param authentication объект аутентификации текущего пользователя
     * @return список CardCurrentBalanceDto с обновленными балансами карт
     * @throws SecurityException если карты не принадлежат аутентифицированному пользователю
     * @throws CardNotFoundException если одна из карт не найдена
     * @throws InsufficientFundsException если на карте отправителя недостаточно средств
     * @throws OptimisticLockException при конфликте параллельных изменений (обрабатывается с повторными попытками)
     */
    List<CardCurrentBalanceDto> transferBetweenCard(TransferRequestDto transferRequestDto, Authentication authentication);

    /**
     * Возвращает страницу с отфильтрованными картами для аутентифицированного пользователя.
     *
     * @param cardFilterDto CardFilterDto с параметрами фильтрации и пагинации
     * @param authentication объект аутентификации текущего пользователя
     * @return Page CardDto страница с отфильтрованными картами
     */
    Page<CardDto> getCardPage(CardFilterDto cardFilterDto, Authentication authentication);
}
