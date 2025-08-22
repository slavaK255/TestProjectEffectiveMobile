package com.example.bankcards.controller.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Контроллер для управления пользователями.
 * Предоставляет API для создания, обновления, удаления и получения информации о пользователях.
 * Все операции требуют прав администратора.
 */
@Tag(name = "User management", description = "Operations with user")
public interface UserController {

    /**
     * Создает нового пользователя в системе.
     *
     * @param createUserRequestDto CreateUserRequestDto с данными для создания пользователя
     * @return CreateUserResponseDto с информацией о созданном пользователе
     */
    @Operation(
            summary = "Create new user",
            description = "create new user then return create user response Dto"
    )
    CreateUserResponseDto createNewUser(CreateUserRequestDto createUserRequestDto);

    /**
     * Обновляет информацию о существующем пользователе.
     *
     * @param userDto UserDto с обновляемыми данными пользователя
     * @return UserDto с обновленной информацией о пользователе
     */
    @Operation(
            summary = "Update user",
            description = "Update existing user information"
    )
    UserDto updateUser(UserDto userDto);

    /**
     * Удаляет пользователя по указанному идентификатору.
     *
     * @param userId идентификатор пользователя для удаления
     * @return String сообщение о результате удаления
     */
    @Operation(
            summary = "Delete user",
            description = "Delete user by specified user ID"
    )
    String deleteUser(Long userId);

    /**
     * Получает информацию о пользователе по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return UserDto с информацией о пользователе
     */
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve user information by user ID"
    )
    UserDto getUserById(Long userId);
}
