package com.example.bankcards.controller;

import com.example.bankcards.dto.jwt.JwtRequestDto;
import com.example.bankcards.dto.jwt.JwtResponseDto;
import com.example.bankcards.dto.jwt.RefreshJwtRequestDto;
import com.example.bankcards.security.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public JwtResponseDto tryToLogin(@RequestBody JwtRequestDto jwtRequestDto){
        return authService.login(jwtRequestDto);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody RefreshJwtRequestDto request) {
        final JwtResponseDto token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponseDto> getNewRefreshToken(@RequestBody RefreshJwtRequestDto request){
        final JwtResponseDto token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
