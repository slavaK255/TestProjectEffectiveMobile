package com.example.bankcards.service.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import com.example.bankcards.exception.BlockCardException;

import java.util.List;

/**
 * Сервис для обработки запросов на блокировку карт.
 * Обеспечивает создание, поиск и получение всех запросов на блокировку.
 * Все операции выполняются в транзакционном контексте.
 */
public interface BlockRequestService {
    /**
     * Создает новый запрос на блокировку карты на основе предоставленных данных.
     *
     * @param blockCardDto BlockCardDto с данными для создания запроса на блокировку
     * @return BlockCardDto с информацией о созданном запросе на блокировку
     */
    BlockCardDto createBlockCardRequest(BlockCardDto blockCardDto);

    /**
     * Находит запрос на блокировку карты по его идентификатору.
     *
     * @param requestId идентификатор запроса на блокировку
     * @return BlockCardDto с информацией о найденном запросе на блокировку
     * @throws BlockCardException если запрос с указанным идентификатором не найден
     */
    BlockCardDto findById(Long requestId);

    /**
     * Возвращает список всех запросов на блокировку карт.
     *
     * @return список BlockCardDto список всех запросов на блокировку
     */
    List<BlockCardDto> findAll();
}
