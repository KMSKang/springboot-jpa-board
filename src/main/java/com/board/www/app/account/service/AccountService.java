package com.board.www.app.account.service;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.dto.AccountDto;

public interface AccountService {
    AccountDto create(AccountDto dto);
    Account findById(Long id);
//    boolean isLogin();
//    Account getAccount();
//    Long getAccountId();
}
