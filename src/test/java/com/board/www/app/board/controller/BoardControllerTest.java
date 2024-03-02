package com.board.www.app.board.controller;

import com.board.www.app.account.repository.AccountRepository;
import com.board.www.app.account.utils.AccountUtils;
import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.service.BoardService;
import com.board.www.app.board.utils.BoardUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@DisplayName("/boards/controller")
@Transactional
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // extends MyRestDoc
public class BoardControllerTest extends MyRestDoc {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BoardUtils boardUtils;
    @Autowired private AccountUtils accountUtils;

    @Autowired private BoardService service;
    @Autowired private AccountRepository accountRepository;

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트")
    void index() throws Exception {
        // given
        accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = boardUtils.givenBoards(11);
        boards.forEach(dto -> service.insert(dto));

        BoardDto.KeywordType keywordType = null;
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/boards")
                                             .param("keywordType", keywordType == null ? "" : keywordType.toString())
                                             .param("keyword", keyword)
                                             .param("pageable", pageable.toString()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);
        System.out.println(responseBody);
        System.out.println(responseDto);

        // then
        assertThat(responseDto.getCode()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("OK");
    }

    @Test
//    @WithMockAccount
    @DisplayName("게시판 리스트 - 오류(인증)") // 자격 증명 확인
    void index_error_authentication() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/boards"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);

        // then
        assertThat(responseDto.getCode()).isEqualTo(401);
        assertThat(responseDto.getMessage()).isEqualTo("Unauthorized Exception");
    }

    /**
     * 보류
     */
//    @Test
//    @WithMockAccount
//    @DisplayName("[ING] 게시판 리스트 - 오류(인가)") // 권한 허가, 거부
//    void index_error_authorization() throws Exception {
//        // given
//        boolean isComplete = false;
//        assertThat(isComplete).isEqualTo(true);
//
//        // when
//
//        // then
//    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 등록")
    void insert() throws Exception {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = boardUtils.givenBoards(1);
        BoardDto dto = boards.get(0);
        String dtoStringify = objectMapper.writeValueAsString(dto);
        MockMultipartFile jsonFile = new MockMultipartFile("dto", "", "application/json", dtoStringify.getBytes());

        // when
        ResultActions resultActions = mockMvc.perform(multipart("/api/boards")
                                             .file(jsonFile)
                                             .with(csrf()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);
        BoardDto boardDto = objectMapper.convertValue(responseDto.getData(), BoardDto.class);
        System.out.println(responseBody);
        System.out.println(responseDto);
        System.out.println(boardDto);

        // then
        assertThat(responseDto.getCode()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("OK");
        assertThat(boardDto.getTitle()).isEqualTo("title1");
        assertThat(boardDto.getContent()).isEqualTo("content1");
        assertThat(boardDto.getCreatedBy()).isEqualTo(username);
    }
}
