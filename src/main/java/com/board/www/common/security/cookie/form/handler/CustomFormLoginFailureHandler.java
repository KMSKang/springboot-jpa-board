package com.board.www.common.security.cookie.form.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFormLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    String redirectUrl = "/";
    String message = exception.getMessage();

    // 아이디 오류
    if (exception instanceof InternalAuthenticationServiceException) {
      redirectUrl = "/login?exception=InternalAuthenticationServiceException";
    }

    // 비밀번호 오류
    if (exception instanceof BadCredentialsException) {
      redirectUrl = "/login?exception=BadCredentialsException";
    }

    // 비정상적인 접근
    if (exception instanceof InsufficientAuthenticationException) {
      redirectUrl = "/login?error=true&exception=InsufficientAuthenticationException&username=" + message;
    }

    setDefaultFailureUrl(redirectUrl);

    super.onAuthenticationFailure(request, response, exception);
  }
}
