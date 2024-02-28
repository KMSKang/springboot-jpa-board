package com.board.www.commons.security.cookie.provider;

import com.board.www.commons.security.cookie.dto.AccountContext;
import com.board.www.commons.security.cookie.common.FormWebAuthenticationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);
    boolean result = passwordEncoder.matches(password, accountContext.getAccount().getPassword());
    if (!result) {
      throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
    }

    FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
    String secretKey = formWebAuthenticationDetails.getSecretKey();
    if (secretKey == null || !"secret".equals(secretKey)) {
      throw new InsufficientAuthenticationException("비정상적인 접근입니다");
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(accountContext, null, accountContext.getAuthorities());

    return authenticationToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

}
