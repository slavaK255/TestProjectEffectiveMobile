package com.example.bankcards.controller.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "User management", description = "Operations with user")
public interface UserController {
    @Operation(
            summary = "Create new user",
            description = "create new user then return create user response Dto"
    )
    CreateUserResponseDto createNewUser(CreateUserRequestDto createUserRequestDto);

    @Operation(
            summary = "Update user",
            description = "Update existing user information"
    )
    UserDto updateUser(UserDto userDto);

    @Operation(
            summary = "Delete user",
            description = "Delete user by specified user ID"
    )
    String deleteUser(Long userId);

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve user information by user ID"
    )
    UserDto getUserById(Long userId);
}
