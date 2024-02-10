package com.denmiagkov.meter.infrastructure.in.login_service;

import com.denmiagkov.meter.application.dto.UserLoginDto;
import com.denmiagkov.meter.application.service.UserService;
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


public class JwtProvider {

    private static final String VALUE_OF_JWT_ACCESS_SECRET_KEY = "authentication.valueOfJwtAccessSecretKey";
    private static final String VALUE_OF_JWT_REFRESH_SECRET_KEY = "authentication.valueOfJwtRefreshSecretKey";
    private final SecretKey JWT_ACCESS_SECRET_KEY;
    private final SecretKey JWT_REFRESH_SECRET_KEY;
    private final UserService userService;


    public JwtProvider(UserService service) {
        this.userService = service;
        this.JWT_ACCESS_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(PropertiesUtil.get(VALUE_OF_JWT_ACCESS_SECRET_KEY)));
        this.JWT_REFRESH_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(PropertiesUtil.get(VALUE_OF_JWT_REFRESH_SECRET_KEY)));
    }

    public String generateAccessToken(UserLoginDto loginDto) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(loginDto.getLogin())
                .setExpiration(accessExpiration)
                .claim("userId", loginDto.getId())
                .claim("role", loginDto.getRole())
                .signWith(JWT_ACCESS_SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(UserLoginDto loginDto) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(loginDto.getLogin())
                .setExpiration(refreshExpiration)
                .claim("userId", loginDto.getId())
                .claim("role", loginDto.getRole())
                .signWith(JWT_REFRESH_SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, JWT_ACCESS_SECRET_KEY);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, JWT_REFRESH_SECRET_KEY);
    }

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

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_ACCESS_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String login = claims.getSubject();
        return login;
    }

    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_ACCESS_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        int userId = claims.get("userId", Integer.class);
        return userId;
    }

    public UserRole getUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_ACCESS_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String userRole = claims.get("role", String.class);
        return UserRole.valueOf(userRole);
    }
}
