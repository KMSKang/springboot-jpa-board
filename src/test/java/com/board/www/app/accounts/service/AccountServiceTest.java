package com.board.www.app.accounts.service;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.dto.AccountDto;
import com.board.www.app.accounts.repository.AccountRepository;
import com.board.www.app.accounts.utils.AccountUtils;
import com.board.www.app.boards.utils.BoardUtils;
import com.board.www.commons.dto.MyRestDoc;
import com.board.www.commons.exception.dto.Exception500;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DisplayName("/accounts/service")
@SpringBootTest
class AccountServiceTest extends MyRestDoc {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AccountUtils accountUtils;
    @Autowired private AccountService service;

    @AfterEach
    public void afterEach() {
        String[] tableNames = {"board", "account"};
        for (String tableName : tableNames) {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    @Test
    @DisplayName("회원 등록")
    void insert() {
        // given
        List<AccountDto> accounts = accountUtils.givenAccounts(1);
        AccountDto dto = accounts.get(0);
        String password = passwordEncoder.encode(dto.getPassword());

        // when
        AccountDto accountDto = service.insert(dto);

        // then
        assertThat(accountDto.getId()).isEqualTo(1L);
        assertThat(accountDto.getRole()).isEqualTo(Account.Role.USER);
        assertThat(accountDto.getUsername()).isEqualTo("username1");
        assertThat(passwordEncoder.matches(accountDto.getPassword(), password));
    }

    @Test
    @DisplayName("회원 등록 중복 오류")
    void insert_error_duplicate() {
        // given
        List<AccountDto> accounts = accountUtils.givenAccounts(10);
        AccountDto dto = new AccountDto();
        dto.setUsername("username1");
        dto.setPassword("password1");

        // when
        accounts.forEach(row -> service.insert(row));
        Exception500 e = assertThrows(Exception500.class, () -> service.insert(dto));
        assertThat(e.getMessage()).isEqualTo("이미 가입된 계정이 있습니다");

        // then
    }

    @Test
    @DisplayName("로그인")
    void login() {
        //
    }

    @Test
    @DisplayName("로그인 아이디 오류")
    void login_error_username() {
        //
    }

    @Test
    @DisplayName("로그인 비밀번호 오류")
    void login_error_password() {
        //
    }

    @Test
    @DisplayName("로그인 인증 오류") // 자격 증명 확인
    void login_error_authentication() {
        //
    }

    @Test
    @DisplayName("로그인 인가 오류") // 권한 허가, 거부
    void login_error_authorization() {
        //
    }
}
