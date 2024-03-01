package com.board.www.common.security.cookie.form.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFormAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        redirectStrategy.sendRedirect(request, response, "/denied?exception=UNAUTHORIZED"); // 인가 (로그인 X)
    }
}
