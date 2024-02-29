package com.board.www.app.accounts.controller;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.dto.AccountDto;
import com.board.www.app.accounts.repository.AccountRepository;
import com.board.www.app.accounts.utils.AccountUtils;
import com.board.www.commons.dto.MyRestDoc;
import com.board.www.commons.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("/accounts/controller")
@Transactional
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // extends MyRestDoc
class AccountControllerTest extends MyRestDoc {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AccountUtils accountUtils;

    @Autowired private AccountRepository repository;

    @Test
    @DisplayName("회원 등록")
    void insert() throws Exception {
        // given
        List<AccountDto> accounts = accountUtils.givenAccounts(1);
        AccountDto dto = accounts.get(0);
        final String password = passwordEncoder.encode(dto.getPassword());
        String dtoStringify = objectMapper.writeValueAsString(dto);

        // when
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

    @Test
    @DisplayName("로그인 - form")
    void login() throws Exception {
        insert();

        // given
        AccountDto dto = new AccountDto();
        dto.setUsername("username1");
        dto.setPassword("password1");

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                                             .with(csrf())
                                             .param("username", dto.getUsername())
                                             .param("password", dto.getUsername()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);
        System.out.println(responseBody);
        System.out.println(responseDto);

        // then
        assertThat(responseDto.getCode()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("OK");
    }

    @Test
    @DisplayName("로그인 - ajax")
    void login_ajax() throws Exception {
        insert();

        // given
        AccountDto dto = new AccountDto();
        dto.setUsername("username1");
        dto.setPassword("password1");
        String dtoStringify = objectMapper.writeValueAsString(dto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/login")
                                             .with(csrf())
                                             .content(dtoStringify)
                                             .contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);
        System.out.println(responseBody);
        System.out.println(responseDto);

        // then
        assertThat(responseDto.getCode()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("OK");
    }
}
