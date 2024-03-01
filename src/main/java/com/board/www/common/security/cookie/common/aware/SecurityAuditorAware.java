package com.board.www.common.security.cookie.common.aware;

import com.board.www.app.account.domain.Account;
import com.board.www.common.security.cookie.common.dto.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
public class SecurityAuditorAware implements AuditorAware<Account> {
//  private final AccountService accountService;

  @Override
  public Optional<Account> getCurrentAuditor() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(authentication -> ((AccountContext)authentication.getPrincipal()).getAccount());
//            .map(authentication -> accountService.getAccount());
  }
}
