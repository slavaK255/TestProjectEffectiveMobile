package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.WorkWithUserException;

/**
 * Сервис для управления пользователями.
 * Обеспечивает поиск, создание, обновление и удаление пользователей.
 * Все операции выполняются в транзакционном контексте.
 */
public interface UserService {

    /**
     * Находит пользователя по логину.
     *
     * @param login логин пользователя для поиска
     * @return User найденный пользователь
     * @throws WorkWithUserException если пользователь с указанным логином не найден
     */
    User findByLogin(String login);

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param userName имя пользователя для поиска
     * @return User найденный пользователь
     * @throws WorkWithUserException если пользователь с указанным именем не найден
     */
    User findByUserName(String userName);

    /**
     * Создает нового пользователя в системе.
     * Проверяет уникальность логина перед созданием.
     *
     * @param createUserRequestDto CreateUserRequestDto с данными для создания пользователя
     * @return CreateUserResponseDto с информацией о созданном пользователе
     * @throws WorkWithUserException если пользователь с таким логином уже существует
     */
    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    /**
     * Обновляет информацию о существующем пользователе.
     * Обновляет только те поля, которые не равны null.
     *
     * @param userDto UserDto с обновляемыми данными пользователя
     * @return UserDto с обновленной информацией о пользователе
     * @throws WorkWithUserException если пользователь с указанным ID не найден
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя для удаления
     */
    void deleteUser(Long userId);

    /**
     * Находит пользователя по идентификатору и возвращает в формате DTO.
     *
     * @param userId идентификатор пользователя
     * @return UserDto с информацией о пользователе
     * @throws WorkWithUserException если пользователь с указанным ID не найден
     */
    UserDto getUserById(Long userId);
}
