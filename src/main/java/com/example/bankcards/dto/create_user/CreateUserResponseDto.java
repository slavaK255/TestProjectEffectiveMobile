package com.example.bankcards.dto.create_user;

import com.example.bankcards.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponseDto {
    private String login;
    private String userName;
    private Role role;
}
