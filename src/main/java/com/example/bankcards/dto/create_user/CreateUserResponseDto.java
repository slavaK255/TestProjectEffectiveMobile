package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateUserResponseDto {
    private String login;
    private String userName;
    private Role role;
}
