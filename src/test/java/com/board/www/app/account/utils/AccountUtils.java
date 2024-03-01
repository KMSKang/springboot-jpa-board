package com.board.www.app.account.utils;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.dto.AccountDto;
import com.board.www.common.security.cookie.common.dto.AccountContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class AccountUtils {
    @Autowired private Validator validator;

    public Account givenAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((AccountContext) authentication.getPrincipal()).getAccount();
    }

    public List<AccountDto> givenAccounts(int size) {
        List<AccountDto> result = new ArrayList<>();
        for (int i=1; i<size+1; i++) {
            AccountDto dto = new AccountDto();
            dto.setRole(Account.Role.USER);
            dto.setUsername("username" + i);
            dto.setPassword("password" + i);
            assertThat(errorSize(dto)).isEqualTo(0); // VALIDATE
            result.add(dto);
        }
        return result;
    }

    public int errorSize(AccountDto dto) {
        Set<ConstraintViolation<AccountDto>> validate = validator.validate(dto);
        Iterator<ConstraintViolation<AccountDto>> iterator = validate.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<AccountDto> next = iterator.next();
            System.out.println(next.getMessage());
        }
        return validate.size();
    }
}
