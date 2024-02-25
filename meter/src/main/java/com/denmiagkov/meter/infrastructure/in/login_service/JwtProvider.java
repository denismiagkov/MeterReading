package com.denmiagkov.meter.infrastructure.in.login_service;

import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.service.exceptions.AuthenticationFailedException;
import com.denmiagkov.meter.config.yaml.AuthConfig;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.starter.audit.aspect.annotations.Audit;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Класс, отвечающий за создание и валидацию access и refresh токенов.
 */
@Audit
@Component
public class JwtProvider {
    /**
     * Секретный ключ для генерации и валидации access токена
     */
    private final SecretKey jwtAccessSecretKey;
    /**
     * Секретный ключ для генерации и валидации access токена
     */
    private final SecretKey jwtRefreshSecretKey;
    /**
     * Поле токена для хранения id пользователя
     */
    private static final String USER_ID = "userId";
    /**
     * Поле токена для хранения роли пользователя
     */
    public static final String USER_ROLE = "role";

    @Autowired
    public JwtProvider(AuthConfig authConfig) {
        this.jwtAccessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(authConfig.getValueOfJwtAccessSecretKey()));
        this.jwtRefreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(authConfig.getValueOfJwtRefreshSecretKey()));
    }

    /**
     * Метод генерирует access токен
     *
     * @param loginDto Входяще ДТО для аутентификации пользователя
     */
    public String generateAccessToken(UserLoginDto loginDto) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(loginDto.getLogin())
                .setExpiration(accessExpiration)
                .claim(USER_ID, loginDto.getUserId())
                .claim(USER_ROLE, loginDto.getRole())
                .signWith(jwtAccessSecretKey)
                .compact();
    }

    /**
     * Метод генерирует refresh токен
     *
     * @param loginDto Входяще ДТО для аутентификации пользователя
     */
    public String generateRefreshToken(UserLoginDto loginDto) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(loginDto.getLogin())
                .setExpiration(refreshExpiration)
                .claim(USER_ID, loginDto.getUserId())
                .claim(USER_ROLE, loginDto.getRole())
                .signWith(jwtRefreshSecretKey)
                .compact();
    }

    /**
     * Метод валидации access токена
     *
     * @param accessToken
     * @return boolean в случае успешной валидации
     */
    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecretKey);
    }

    /**
     * Метод валидации refresh токена
     *
     * @param refreshToken
     * @return boolean в случае успешной валидации
     */
    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecretKey);
    }

    /**
     * Метод валидации токена
     *
     * @param token  токен
     * @param secret секретный ключ
     * @return boolean в случае успешной валидации
     */
    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecretKey);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecretKey);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Метод получения логина пользователя из токена
     *
     * @param token токен
     * @return String логин
     */
    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtAccessSecretKey)
                .parseClaimsJws(token)
                .getBody();
        String login = claims.getSubject();
        return login;
    }

    /**
     * Метод получения id пользователя из токена
     *
     * @param token токен
     * @return int id пользователя
     */
    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtAccessSecretKey)
                .parseClaimsJws(token)
                .getBody();
        int userId = claims.get(USER_ID, Integer.class);
        return userId;
    }

    /**
     * Метод получения роли пользователя из токена
     *
     * @param token токен
     * @return UserRole роль пользоваетля
     */
    public UserRole getUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtAccessSecretKey)
                .parseClaimsJws(token)
                .getBody();
        String userRole = claims.get(USER_ROLE, String.class);
        return UserRole.valueOf(userRole);
    }
}
