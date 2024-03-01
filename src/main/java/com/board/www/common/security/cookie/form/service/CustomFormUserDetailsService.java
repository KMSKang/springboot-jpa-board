package com.board.www.common.security.cookie.form.service;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.repository.AccountRepository;
import com.board.www.common.security.cookie.common.dto.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor // -> config 때문에 사용 못함
public class CustomFormUserDetailsService implements UserDetailsService {
  @Autowired private AccountRepository accountRepository;
//  private final AccountRepository accountRepository; // -> config 때문에 사용 못함

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountRepository.findByUsername(username);
    if (account == null) {
      throw new InternalAuthenticationServiceException("아이디를 찾을 수 없습니다");
    }

    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(account.getRole().getName()));

    return new AccountContext(account, roles);
  }
}
