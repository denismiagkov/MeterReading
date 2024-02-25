package com.denmiagkov.meter.infrastructure.in.login_service;

import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.exceptions.AuthenticationFailedException;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.HasNoAdminStatusException;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис аутентификации пользователя
 */
@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    /**
     * Метод проверки логина и пароля пользователя. В случае прохождения проверки генерируется токен,
     * в противном случае выбрасывается исключение.
     *
     * @param jwtRequest
     */
    public JwtResponse login(JwtRequest jwtRequest) {
        try {
            UserLoginDto loginDto = userService.getPasswordByLogin(jwtRequest.getLogin());
            if (loginDto.getPassword().equals(jwtRequest.getPassword())) {
                String accessToken = jwtProvider.generateAccessToken(loginDto);
                String refreshToken = jwtProvider.generateRefreshToken(loginDto);
                return new JwtResponse(accessToken, refreshToken);
            } else {
                throw new AuthenticationFailedException();
            }
        } catch (RuntimeException e) {
            throw new AuthenticationFailedException();
        }
    }

    /**
     * Метод получения нового access токена в случае истечения его срока действия
     *
     * @param refreshToken
     * @return JwtResponse Новая пара accsess-refresh токенов
     */
    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            UserLoginDto loginDto = userService.getPasswordByLogin(login);
            String accessToken = jwtProvider.generateAccessToken(loginDto);
            return new JwtResponse(accessToken, null);
        }
        return new JwtResponse(null, null);
    }

    /**
     * Метод вызывает метод валидации refresh токена
     *
     * @param refreshToken
     * @return boolean возвращает true в случае успешной проверки
     */
    public boolean validateRefreshToken(String refreshToken) {
        return jwtProvider.validateRefreshToken(refreshToken);
    }

    /**
     * Метод извлечения id пользователя из токена
     *
     * @param token
     * @return int id пользователя
     */
    public int getUserIdFromToken(String token) {
        return jwtProvider.getUserIdFromToken(token);
    }

    public void verifyAdmin(String header) {
        String token = getTokenFromHeader(header);
        jwtProvider.validateAccessToken(token);
        isAdmin(token);
    }

    public String verifyUser(String header) {
        String token = getTokenFromHeader(header);
        jwtProvider.validateAccessToken(token);
        return token;
    }

    /**
     * Метод извлечения токена из HttpServletRequest
     *
     * @param request HttpServletRequest
     * @return String токен
     */
    public String getTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new AuthenticationFailedException();
    }

    /**
     * Метод проверки статуса админа у пользователя
     *
     * @param token
     * @return boolean результат проверки
     * @throws HasNoAdminStatusException в случае отсутствия статуса админа
     */
    public void isAdmin(String token) {
        UserRole role = jwtProvider.getUserRoleFromToken(token);
        if (!role.equals(UserRole.ADMIN)) {
            throw new HasNoAdminStatusException();
        }
    }
}

