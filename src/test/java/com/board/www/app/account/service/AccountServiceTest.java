package com.board.www.app.account.service;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.dto.AccountDto;
import com.board.www.app.account.utils.AccountUtils;
import com.board.www.common.exception.dto.Exception500;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("/accounts/service")
@Transactional
@SpringBootTest
class AccountServiceTest extends AccountUtils {
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private AccountService service;

    @Test
    @DisplayName("회원 등록")
    void create() {
        // given
        List<AccountDto> accounts = givenAccounts(1);
        AccountDto dto = accounts.get(0);
        String password = passwordEncoder.encode(dto.getPassword());

        // when
        AccountDto accountDto = service.create(dto);

        // then
        assertThat(accountDto.getRole()).isEqualTo(Account.Role.USER);
        assertThat(accountDto.getUsername()).isEqualTo("username1");
    }

    @Test
    @DisplayName("회원 등록 - 오류(중복)")
    void insert_error_duplicate() {
        // given
        List<AccountDto> accounts = givenAccounts(10);
        AccountDto dto = AccountDto.create("username1", "password1");

        // when
        accounts.forEach(row -> service.create(row));
        Exception500 e = assertThrows(Exception500.class, () -> service.create(dto));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 가입된 계정이 있습니다");
    }
}
