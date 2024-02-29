package com.board.www.commons.security.cookie.service;

import com.board.www.app.accounts.domain.Account;
import com.board.www.commons.exception.dto.Exception500;
import com.board.www.commons.security.cookie.dto.AccountContext;
import com.board.www.app.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountRepository.findByUsername(username);
    if (account == null) {
      throw new Exception500("아이디를 찾을 수 없습니다");
    }
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(account.getRole().getName()));
    return new AccountContext(account, roles);
  }
}
