package com.board.www.commons.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationHandler implements AuthenticationEntryPoint {
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    redirectStrategy.sendRedirect(request, response, "/login");
  }
}
