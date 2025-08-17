package com.example.bankcards.dto.create_user;

import com.example.bankcards.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {
    @NotNull
    @NotBlank
    @Size(max = 64)
    private String login;
    @NotNull
    @NotBlank
    @Size(max = 128)
    private String userName;
    @NotNull
    @NotBlank
    @Size(max = 128)
    private String password;
    @NotNull
    private Role role;
}
