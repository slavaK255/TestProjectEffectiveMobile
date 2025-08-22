package com.example.bankcards.mapper;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserRequestDto createUserRequestDto);

    CreateUserResponseDto toCreateUserResponseDto(User user);

    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);
}
