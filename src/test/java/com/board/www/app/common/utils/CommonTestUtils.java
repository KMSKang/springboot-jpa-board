package com.board.www.app.common.utils;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.repository.AccountRepository;
import com.board.www.commons.security.dto.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommonTestUtils {
    private final AccountRepository accountRepository;

    public String givenAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account loginAccount = ((AccountContext) authentication.getPrincipal()).getAccount();
        accountRepository.save(loginAccount);
        return loginAccount.getUsername();
    }
}
