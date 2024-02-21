package com.denmiagkov.meter.infrastructure.in.filter;

import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр, реализующий аутентификацию пользователя
 */
@Component
public class UserFilterWrapper {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String TOKEN_ATTRIBUTE_NAME = "token";
    public static final Logger LOG = LoggerFactory.getLogger(UserFilterWrapper.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static AuthService authService;

    @Autowired
    private UserFilterWrapper(AuthService service) {
        authService = service;
    }


    @WebFilter("/api/v1/user/*")
    public static class UserFilter implements Filter {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            try {
                String header = ((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION_HEADER_NAME);
                String token = authService.verifyUser(header);
                servletRequest.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
                filterChain.doFilter(servletRequest, response);
            } catch (Exception e) {
                handleException(response, e);
            }
        }
    }

    private static void handleException(HttpServletResponse response, Exception e) throws IOException {
        LOG.error("EXCEPTION OCCURRED: ", e);
        String errorMessage = MAPPER.writeValueAsString(e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(errorMessage);
    }
}
