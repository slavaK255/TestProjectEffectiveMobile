package com.example.bankcards.controller.block_request;

import com.example.bankcards.dto.block_request.BlockCardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Реализация REST контроллера для управления запросами на блокировку карт.
 * Обрабатывает HTTP-запросы связанные с операциями блокировки карт.
 */
@Tag(name = "Block request management", description = "Operations with block card request")
public interface BlockRequestController {

    /**
     * Обрабатывает POST запрос для создания нового запроса на блокировку карты.
     * Требует валидных данных и аутентификации с ролью USER.
     *
     * @param blockCardDto DTO с данными для блокировки карты
     * @return созданный запрос на блокировку в формате DTO
     */
    @Operation(
            summary = "Create block card request",
            description = "Create a new block card request and return created request details"
    )
    BlockCardDto createBlockCardRequest(BlockCardDto blockCardDto);

    /**
     * Обрабатывает GET запрос для получения запроса на блокировку по ID.
     * Требует аутентификации с ролью ADMIN.
     *
     * @param requestId идентификатор запроса на блокировку
     * @return DTO с данными запроса на блокировку
     */
    @Operation(
            summary = "Get block request by ID",
            description = "Retrieve block card request details by its ID"
    )
    BlockCardDto getById(Long requestId);

    /**
     * Обрабатывает GET запрос для получения всех запросов на блокировку.
     * Требует аутентификации с ролью ADMIN.
     *
     * @return список DTO всех запросов на блокировку
     */
    @Operation(
            summary = "Get all block requests",
            description = "Retrieve a list of all block card requests"
    )
    List<BlockCardDto> getAll();
}
