package com.board.www.app.accounts.service;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.dto.AccountDto;
import com.board.www.app.accounts.repository.AccountRepository;
import com.board.www.commons.exception.dto.Exception500;
import com.board.www.commons.security.dto.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountService {
  private final PasswordEncoder passwordEncoder;

  private final AccountRepository repository;

  @Transactional
  public AccountDto insert(AccountDto dto) {
    Account account = repository.findByUsername(dto.getUsername());
    if (account != null) {
      throw new Exception500("이미 가입된 계정이 있습니다");
    }

    dto.setPassword(passwordEncoder.encode(dto.getPassword()));

    return new AccountDto(repository.save(dto.toEntity()));
  }

  public Account findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new Exception500("계정을 찾을 수 없습니다"));
  }

  public boolean isLogin() {
    return !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  public Account getAccount() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long id = ((AccountContext) authentication.getPrincipal()).getAccount().getId();
    return repository.findById(id).orElseThrow(() -> new Exception500("접속 정보를 찾을 수 없습니다"));
  }
}
