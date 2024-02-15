package com.denmiagkov.meter.infrastructure.in.login_service;

import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.utils.PropertiesUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Класс, отвечающий за создание и валидацию access и refresh токенов.
 */
public class JwtProvider {

    public static final JwtProvider INSTANCE = new JwtProvider();
    /**
     * Ссылка на значение секретного ключа для создания и валидации access токена в формате BASE64
     */
    private static final String VALUE_OF_JWT_ACCESS_SECRET_KEY = "authentication.valueOfJwtAccessSecretKey";
    /**
     * Ссылка на значение секретного ключа для создания и валидации refresh токена в формате BASE64
     */
    private static final String VALUE_OF_JWT_REFRESH_SECRET_KEY = "authentication.valueOfJwtRefreshSecretKey";
    /**
     * Секретный ключ для генерации и валидации access токена
     */
    private final SecretKey JWT_ACCESS_SECRET_KEY;
    /**
     * Секретный ключ для генерации и валидации access токена
     */
    private final SecretKey JWT_REFRESH_SECRET_KEY;
    /**
     * Поле токена для хранения id пользователя
     */
    private static final String USER_ID = "userId";
    /**
     * Поле токена для хранения роли пользователя
     */
    public static final String USER_ROLE = "role";

    public JwtProvider() {
        this.JWT_ACCESS_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(PropertiesUtil.get(VALUE_OF_JWT_ACCESS_SECRET_KEY)));
        this.JWT_REFRESH_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(PropertiesUtil.get(VALUE_OF_JWT_REFRESH_SECRET_KEY)));
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
                .signWith(JWT_ACCESS_SECRET_KEY)
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
                .signWith(JWT_REFRESH_SECRET_KEY)
                .compact();
    }

    /**
     * Метод валидации access токена
     *
     * @param accessToken
     * @return boolean в случае успешной валидации
     */
    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, JWT_ACCESS_SECRET_KEY);
    }

    /**
     * Метод валидации refresh токена
     *
     * @param refreshToken
     * @return boolean в случае успешной валидации
     */
    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, JWT_REFRESH_SECRET_KEY);
    }

    /**
     * Метод валидации токена
     *
     * @param token  токен
     * @param secret секретный ключ
     * @return boolean в случае успешной валидации
     */
    private boolean validateToken(String token, Key secret) {
        Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, JWT_ACCESS_SECRET_KEY);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, JWT_REFRESH_SECRET_KEY);
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
                .setSigningKey(JWT_ACCESS_SECRET_KEY)
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
                .setSigningKey(JWT_ACCESS_SECRET_KEY)
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
                .setSigningKey(JWT_ACCESS_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String userRole = claims.get(USER_ROLE, String.class);
        return UserRole.valueOf(userRole);
    }
}
