package com.denmiagkov.meter.infrastructure.in.filter;

import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserFilterWrapper {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String TOKEN_ATTRIBUTE_NAME = "token";
    private static AuthService authService;

    @Autowired
    private UserFilterWrapper(AuthService service) {
        authService = service;
    }

    @WebFilter("/api/v1/user/*")
    public static class UserFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            String header = ((HttpServletRequest) request).getHeader(AUTHORIZATION_HEADER_NAME);
            if (header != null) {
                String token = authService.verifyUser(header);
                request.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect("/api/v1/login");
            }
        }
    }
}
