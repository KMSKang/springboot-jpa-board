package com.board.www.app.accounts.controller;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.dto.AccountDto;
import com.board.www.app.accounts.repository.AccountRepository;
import com.board.www.app.accounts.utils.AccountUtils;
import com.board.www.commons.dto.MyRestDoc;
import com.board.www.commons.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("local")
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@AutoConfigureMockMvc
@DisplayName("/accounts/controller")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // extends MyRestDoc
class AccountControllerTest extends MyRestDoc {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AccountUtils accountUtils;

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
    void insert() throws Exception {
        // given
        List<AccountDto> accounts = accountUtils.givenAccounts(1);
        AccountDto dto = accounts.get(0);
        final String password = passwordEncoder.encode(dto.getPassword());

        // when
        String dtoStringify = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mockMvc.perform(post("/api/accounts").with(csrf()).content(dtoStringify).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);
        AccountDto accountDto = objectMapper.convertValue(responseDto.getData(), AccountDto.class);
        System.out.println(responseBody);
        System.out.println(responseDto);
        System.out.println(accountDto);

        // then
        assertThat(responseDto.getCode()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("OK");
        assertThat(accountDto.getId()).isEqualTo(1L);
        assertThat(accountDto.getRole()).isEqualTo(Account.Role.USER);
        assertThat(accountDto.getUsername()).isEqualTo("username1");
        assertThat(passwordEncoder.matches(accountDto.getPassword(), password));
    }
}
