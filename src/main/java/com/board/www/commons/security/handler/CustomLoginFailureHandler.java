package com.board.www.commons.security.handler;

import com.board.www.commons.utils.CommonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final CommonUtils utils;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    String redirectUrl = "";
    HttpSession session = request.getSession();

    String username = (String) session.getAttribute("username");

    // 아이디 또는 비밀번호 불일치
    if (exception instanceof InternalAuthenticationServiceException || exception instanceof BadCredentialsException) {
      redirectUrl = "/login?error=true&exception=" + "BadCredentialsException" + "&username=" + username;
    }
    // 비정상적인 접근
    else if (exception instanceof InsufficientAuthenticationException) {
      redirectUrl = "/login?error=true&exception=" + "InsufficientAuthenticationException" + "&username=" + username;
    }

    setDefaultFailureUrl(utils.isEmpty(redirectUrl) ? "/" : redirectUrl);
    super.onAuthenticationFailure(request, response, exception);
  }
}
