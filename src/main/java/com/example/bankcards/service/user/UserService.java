package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.User;

public interface UserService {

    User findByLogin(String login);

    User findByUserName(String userName);

    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);
}
