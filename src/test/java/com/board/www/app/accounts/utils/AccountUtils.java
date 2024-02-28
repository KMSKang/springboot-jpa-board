package com.board.www.app.accounts.utils;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.dto.AccountDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class AccountUtils {
    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public List<AccountDto> givenAccounts(int size) {
        List<AccountDto> result = new ArrayList<>();
        for (int i=1; i<size+1; i++) {
            AccountDto dto = new AccountDto();
            dto.setRole(Account.Role.USER);
            dto.setUsername("username" + i);
            dto.setPassword("password" + i);

            // VALIDATE
            assertThat(errorSize(dto)).isEqualTo(0);

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
