package com.board.www.app.boards.controller;

import com.board.www.app.boards.dto.BoardDto;
import com.board.www.app.boards.utils.BoardUtils;
import com.board.www.app.common.utils.CommonTestUtils;
import com.board.www.commons.dto.MyRestDoc;
import com.board.www.commons.dto.ResponseDto;
import com.board.www.commons.dto.WithMockAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@DisplayName("/boards/controller")
@Transactional
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // extends MyRestDoc
public class BoardControllerTest extends MyRestDoc {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CommonTestUtils commonUtils;
    @Autowired private BoardUtils boardUtils;

    @Test
//    @WithMockAccount
    @DisplayName("게시판 리스트")
    void index() {

    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 등록")
    void insert() throws Exception {
        System.out.println("테스트: "+ Thread.currentThread());

        // given
        String username = commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(1);
        BoardDto dto = boards.get(0);

        // when
        String dtoStringify = objectMapper.writeValueAsString(dto);
        MockMultipartFile jsonFile = new MockMultipartFile("dto", "", "application/json", dtoStringify.getBytes());
        ResultActions resultActions = mockMvc.perform(multipart("/api/boards").file(jsonFile).with(csrf()));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        ResponseDto responseDto = objectMapper.readValue(responseBody, ResponseDto.class);
        BoardDto boardDto = objectMapper.convertValue(responseDto.getData(), BoardDto.class);
        System.out.println(responseBody);
        System.out.println(responseDto);
        System.out.println(boardDto);

        // then
        assertThat(responseDto.getCode()).isEqualTo(200);
        assertThat(responseDto.getMessage()).isEqualTo("OK");
        assertThat(boardDto.getId()).isEqualTo(1L);
        assertThat(boardDto.getTitle()).isEqualTo("title1");
        assertThat(boardDto.getContent()).isEqualTo("content1");
        assertThat(boardDto.getCreatedBy()).isEqualTo(username);
    }
}
