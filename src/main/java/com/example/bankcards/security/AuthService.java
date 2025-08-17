package com.example.bankcards.service;

import com.example.bankcards.dto.jwt.JwtRequestDto;
import com.example.bankcards.dto.jwt.JwtResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserService userService;
    //TODO
    public JwtResponseDto login(@NonNull JwtRequestDto authRequest) {
        final User user = userService.findByLogin(authRequest.getLogin());
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            return new JwtResponseDto(accessToken, refreshToken);
        } else {
            throw new IllegalArgumentException("Неправильный пароль");
        }
    }
    //TODO
//    public JwtResponseDto getAccessToken(@NonNull String refreshToken) {
//        if (jwtProvider.validateRefreshToken(refreshToken)) {
//            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
//            final String login = claims.getSubject();
//            final String saveRefreshToken = refreshStorage.get(login);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
//                final User user = userService.findByLogin(login);
//                final String accessToken = jwtProvider.generateAccessToken(user);
//                return new JwtResponseDto(accessToken, null);
//            }
//        }
//        return new JwtResponseDto(null, null);
//    }
//
//    public JwtResponseDto refresh(@NonNull String refreshToken) {
//        if (jwtProvider.validateRefreshToken(refreshToken)) {
//            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
//            final String login = claims.getSubject();
//            final String saveRefreshToken = refreshStorage.get(login);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
//                final User user = userService.getByLogin(login)
//                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
//                final String accessToken = jwtProvider.generateAccessToken(user);
//                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
//                refreshStorage.put(user.getLogin(), newRefreshToken);
//                return new JwtResponseDto(accessToken, newRefreshToken);
//            }
//        }
//        throw new AuthException("Невалидный JWT токен");
//    }
}
