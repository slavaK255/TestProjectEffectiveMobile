package com.example.bankcards.util;

import com.example.bankcards.entity.JwtAuthentication;
import com.example.bankcards.entity.Role;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(getRoles(claims));
        jwtInfoToken.setLogin(claims.getSubject());
        return jwtInfoToken;
    }

    private static Role getRoles(Claims claims) {
        final String role = claims.get("role", String.class);
        return Role.valueOf(role);
    }

}
