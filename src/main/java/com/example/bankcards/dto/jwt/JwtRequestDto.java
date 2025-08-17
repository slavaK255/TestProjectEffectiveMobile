package com.example.bankcards.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String login;
    private String password;
}
