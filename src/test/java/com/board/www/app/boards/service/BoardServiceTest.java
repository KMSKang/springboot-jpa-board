package com.board.www.app.boards.service;

import com.board.www.app.accounts.domain.Account;
import com.board.www.app.accounts.dto.AccountDto;
import com.board.www.app.accounts.repository.AccountRepository;
import com.board.www.app.boards.domain.Board;
import com.board.www.app.boards.dto.BoardDto;
import com.board.www.app.boards.repository.BoardDslRepository;
import com.board.www.app.boards.repository.BoardRepository;
import com.board.www.app.boards.utils.BoardUtils;
import com.board.www.app.common.utils.CommonTestUtils;
import com.board.www.commons.dto.MyRestDoc;
import com.board.www.commons.dto.WithMockAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DisplayName("/boards/service")
@SpringBootTest
class BoardServiceTest extends MyRestDoc {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private CommonTestUtils commonUtils;
    @Autowired private BoardUtils boardUtils;
    @Autowired private BoardService service;
    @Autowired private BoardRepository repository;
    @Autowired private BoardDslRepository dslRepository;
    @Autowired private AccountRepository accountRepository;

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
    @WithMockAccount
    @DisplayName("게시판 리스트")
    void index() {
        // given
        String username = commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(11);

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(null, null, pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(10); // 현재 페이지 개수
        assertThat(content.getId()).isEqualTo(11L);
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - 2페이지")
    void index_page2() {
        // given
        String username = commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(11);

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<BoardDto> pageable = dslRepository.findAll(null, null, pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(1); // 현재 페이지 개수
        assertThat(content.getId()).isEqualTo(1L);
        assertThat(content.getTitle()).isEqualTo("title1");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: NULL / 내용: 1)")
    void index_search_keywordType_null_keyword_1() {
        // given
        String username = commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(11);

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(null, "1", pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(3); // 전체 개수
        assertThat(contents.size()).isEqualTo(3); // 현재 페이지 개수
        assertThat(content.getId()).isEqualTo(11L);
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: 제목 / 내용: 1)")
    void index_search_keywordType_TITLE_keyword_1() {
        // given
        String username = commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(11);

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(BoardDto.KeywordType.TITLE, "1", pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(3); // 전체 개수
        assertThat(contents.size()).isEqualTo(3); // 현재 페이지 개수
        assertThat(content.getId()).isEqualTo(11L);
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: 등록자명 / 내용: kang)")
    void index_search_keywordType_CREATEDBY_keyword() {
        // given
        commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(11);

        // when
        boards.forEach(dto -> service.insert(dto));
        givenBoardsOthers();
        assertThat(repository.findAll().size()).isEqualTo(13);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(BoardDto.KeywordType.CREATEDBY, "kang", pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(10); // 현재 페이지 개수
        assertThat(content.getId()).isEqualTo(11L);
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo("KangMinSung");
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 등록")
    void insert()  {
        // given
        String username = commonUtils.givenAccount();
        List<BoardDto> boards = boardUtils.givenBoards(1);
        BoardDto dto = boards.get(0);

        // when
        BoardDto boardDto = service.insert(dto);

        // then
        assertThat(boardDto.getId()).isEqualTo(1L);
        assertThat(boardDto.getTitle()).isEqualTo("title1");
        assertThat(boardDto.getContent()).isEqualTo("content1");
        assertThat(boardDto.getCreatedBy()).isEqualTo(username);
    }

    public void givenBoardsOthers() {
        AccountDto accountDto1 = new AccountDto();
        accountDto1.setUsername("BaekKiSeon");
        accountDto1.setPassword("BaekKiSeon1234");
        Account account1 = accountRepository.save(accountDto1.toEntity());

        AccountDto accountDto2 = new AccountDto();
        accountDto2.setUsername("KimYoungHan");
        accountDto2.setPassword("KimYoungHan1234");
        Account account2 = accountRepository.save(accountDto2.toEntity());

        BoardDto boardDto1 = new BoardDto();
        boardDto1.setTitle("titleBaek");
        boardDto1.setContent("contentBaek");
        Board board1 = boardDto1.toEntity();
        board1.setAccount(account1);
        repository.save(board1);

        BoardDto boardDto2 = new BoardDto();
        boardDto2.setTitle("titleKim");
        boardDto2.setContent("KimYoungHan1234");
        Board board2 = boardDto2.toEntity();
        board2.setAccount(account2);
        repository.save(board2);
    }
}
