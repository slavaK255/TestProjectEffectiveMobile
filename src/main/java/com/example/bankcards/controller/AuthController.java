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

/**
 * Контроллер для аутентификации и управления JWT токенами.
 * Предоставляет эндпоинты для входа в систему, получения нового access token и обновления refresh token.
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Обрабатывает запрос на аутентификацию пользователя.
     *
     * @param jwtRequestDto JwtRequestDto с учетными данными пользователя (логин и пароль)
     * @return JwtResponseDto с access и refresh токенами
     */
    @PostMapping("login")
    public JwtResponseDto tryToLogin(@RequestBody JwtRequestDto jwtRequestDto){
        return authService.login(jwtRequestDto);
    }

    /**
     * Генерирует новый access token по действительному refresh token.
     *
     * @param request RefreshJwtRequestDto с refresh токеном
     * @return JwtResponseDto с новым access токеном и refresh токеном
     */
    @PostMapping("token")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody RefreshJwtRequestDto request) {
        final JwtResponseDto token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * Генерирует новую пару access и refresh токенов по действительному refresh token.
     *
     * @param request RefreshJwtRequestDto с refresh токеном
     * @return JwtResponseDto с новой парой access и refresh токенов
     */
    @PostMapping("refresh")
    public ResponseEntity<JwtResponseDto> getNewRefreshToken(@RequestBody RefreshJwtRequestDto request){
        final JwtResponseDto token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
