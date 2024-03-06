package com.board.www.app.account.controller;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.dto.AccountDto;
import com.board.www.app.account.utils.AccountUtils;
import com.board.www.common.dto.MyRestDoc;
import com.board.www.common.dto.ResponseDto;
import com.board.www.common.dto.WithMockAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("/accounts/controller")
@Transactional
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // extends MyRestDoc
class AccountApiControllerTest extends MyRestDoc {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AccountUtils accountUtils;

    @Test
    @DisplayName("회원 등록")
    void create() throws Exception {
        // given
        List<AccountDto> accounts = accountUtils.givenAccounts(1);
        AccountDto dto = accounts.get(0);
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
        assertThat(accountDto.getRole()).isEqualTo(Account.Role.USER);
        assertThat(accountDto.getUsername()).isEqualTo("username1");
    }

    @Test
    @DisplayName("[ING] 로그인(form)")
    void login() throws Exception {
        // given
        create();
        AccountDto dto = AccountDto.create("username1", "password1");

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                                             .with(csrf())
                                             .param("username", dto.getUsername())
                                             .param("password", dto.getPassword())
                                             .param("secret_key", "secret"));
        MockHttpServletResponse mockHttpServletResponse = resultActions.andReturn().getResponse();

        // then
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(302);
        assertThat(mockHttpServletResponse.getCookies().length).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인(axios)")
    void login_axios() throws Exception {
        // given
        create();
        AccountDto dto = AccountDto.create("username1", "password1");
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

    @Test
    @DisplayName("로그인(axios) - 오류(아이디)")
    void login_axios_error_username() throws Exception {
        // given
        create();
        AccountDto dto = AccountDto.create("username2", "password1");
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
        assertThat(responseDto.getCode()).isEqualTo(400);
        assertThat(responseDto.getMessage()).isEqualTo("아이디를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("로그인(axios) - 오류(비밀번호)")
    void login_axios_error_password() throws Exception {
        // given
        create();
        AccountDto dto = AccountDto.create("username1", "password2");
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
        assertThat(responseDto.getCode()).isEqualTo(400);
        assertThat(responseDto.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다");
    }

    @Test
    @WithMockAccount
    @DisplayName("로그아웃")
    void logout() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/logout"));
        MockHttpServletResponse mockHttpServletResponse = resultActions.andReturn().getResponse();

        // then
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(302);
    }
}
