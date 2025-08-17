package com.example.bankcards.controller;

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
}
