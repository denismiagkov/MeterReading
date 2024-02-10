package com.denmiagkov.meter.infrastructure.in.login_service;

import com.denmiagkov.meter.application.dto.UserLoginDto;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.validator.exception.NoAccessRightsException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;

public class AuthService {

    private final UserService service;
    private final JwtProvider jwtProvider;

    public AuthService(UserService service, JwtProvider provider) {
        this.service = service;
        this.jwtProvider = provider;
    }

    public JwtResponse login(JwtRequest jwtRequest) {
        try {
            UserLoginDto loginDto = service.getPasswordByLogin(jwtRequest.getLogin());
            if (loginDto.getPassword().equals(jwtRequest.getPassword())) {
                String accessToken = jwtProvider.generateAccessToken(loginDto);
                String refreshToken = jwtProvider.generateRefreshToken(loginDto);
                return new JwtResponse(accessToken, refreshToken);
            } else {
                throw new AuthException();
            }
        } catch (RuntimeException e) {
            throw new AuthException();
        }
    }

    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            UserLoginDto loginDto = service.getPasswordByLogin(login);
            ;
            String accessToken = jwtProvider.generateAccessToken(loginDto);
            return new JwtResponse(accessToken, null);
        }
        return new JwtResponse(null, null);
    }

    public boolean validateAccessToken(String token) throws RuntimeException {
        return jwtProvider.validateAccessToken(token);
    }

    public int getUserIdFromToken(String token) {
        return jwtProvider.getUserIdFromToken(token);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return jwtProvider.validateRefreshToken(refreshToken);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public boolean isAdmin(String token) {
        UserRole role = jwtProvider.getUserRoleFromToken(token);
        if (role.equals(UserRole.ADMIN)) {
            return true;
        } else {
            throw new NoAccessRightsException();
        }
    }

    public String getTokenFromHeader(String header) {
        return header.substring(7);
    }

    public String getLoginFromToken(String token) {
        return jwtProvider.getLoginFromToken(token);
    }

}
