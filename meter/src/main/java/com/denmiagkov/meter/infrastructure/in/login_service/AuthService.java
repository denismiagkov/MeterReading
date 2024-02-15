package com.denmiagkov.meter.infrastructure.in.login_service;

import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.validator.exception.NoAccessRightsException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Сервис аутентификации пользователя
 */
public class AuthService {
    public static final AuthService INSTANCE = new AuthService();
    private final UserService service;
    private final JwtProvider jwtProvider;

    private AuthService() {
        this.service = UserServiceImpl.INSTANCE;
        this.jwtProvider = JwtProvider.INSTANCE;
    }

    /**
     * Метод проверки логина и пароля пользователя. В случае прохождения проверки генерируется токен,
     * в противном случае выбрасывается исключение.
     *
     * @param jwtRequest
     */
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
            UserLoginDto loginDto = service.getPasswordByLogin(login);
            String accessToken = jwtProvider.generateAccessToken(loginDto);
            return new JwtResponse(accessToken, null);
        }
        return new JwtResponse(null, null);
    }

    /**
     * Метод вызывает метод валидации access токена
     *
     * @param token
     * @return boolean возвращает true в случае успешной проверки
     */
    public boolean validateAccessToken(String token) throws RuntimeException {
        return jwtProvider.validateAccessToken(token);
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
     * Метод извлечения токена из HttpServletRequest
     *
     * @param request HttpServletRequest
     * @return String токен
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
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


    /**
     * Метод проверки статуса админа у пользователя
     *
     * @param token
     * @return boolean результат проверки
     * @throws NoAccessRightsException в случае отсутствия статуса админа
     */
    public boolean isAdmin(String token) {
        UserRole role = jwtProvider.getUserRoleFromToken(token);
        if (role.equals(UserRole.ADMIN)) {
            return true;
        } else {
            throw new NoAccessRightsException();
        }
    }
}

