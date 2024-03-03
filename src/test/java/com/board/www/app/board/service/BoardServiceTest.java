package com.board.www.app.board.service;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.dto.AccountDto;
import com.board.www.app.account.repository.AccountRepository;
import com.board.www.app.account.utils.AccountUtils;
import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.dto.BoardTestDto;
import com.board.www.app.board.repository.BoardRepository;
import com.board.www.app.board.utils.BoardUtils;
import com.board.www.common.dto.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.board.www.app.board.dto.BoardDto.KeywordType.CREATEDBY;
import static com.board.www.app.board.dto.BoardDto.KeywordType.TITLE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/boards/service")
@Transactional
@SpringBootTest
class BoardServiceTest extends BoardUtils {
    @Autowired private AccountUtils accountUtils;

    @Autowired private BoardService service;
    @Autowired private BoardRepository repository;
    @Autowired private AccountRepository accountRepository;

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트")
    void index() {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);
        boards.forEach(dto -> service.create(dto));

        BoardDto.KeywordType keywordType = null;
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);
//        Pageable pageable = PageRequest.of(1, 10); // 오류

        // when
        Page<BoardDto.ResponseIndexDto> responseDto = service.index(keywordType, keyword, pageable);
        List<BoardDto.ResponseIndexDto> contents = responseDto.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(responseDto.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(10); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - 2페이지")
    void index_page2() {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);
        boards.forEach(dto -> service.create(dto));

        BoardDto.KeywordType keywordType = null;
        String keyword = "";
        Pageable pageable = PageRequest.of(1, 10);
//        Pageable pageable = PageRequest.of(0, 10); // 오류

        // when
        Page<BoardDto.ResponseIndexDto> responseDto = service.index(keywordType, keyword, pageable);
        List<BoardDto.ResponseIndexDto> contents = responseDto.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(responseDto.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(1); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title1");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: NULL / 내용: 1)")
    void index_search_keywordType_null_keyword_1() {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);
        boards.forEach(dto -> service.create(dto));

        BoardDto.KeywordType keywordType = null;
        String keyword = "1";
//        String keyword = "2"; // 오류
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BoardDto.ResponseIndexDto> responseDto = service.index(keywordType, keyword, pageable);
        List<BoardDto.ResponseIndexDto> contents = responseDto.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(responseDto.getTotalElements()).isEqualTo(3); // 전체 개수
        assertThat(contents.size()).isEqualTo(3); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: 제목 / 내용: 1)")
    void index_search_keywordType_TITLE_keyword_1() {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);
        boards.forEach(dto -> service.create(dto));

        BoardDto.KeywordType keywordType = TITLE;
        String keyword = "1";
//        String keyword = "2"; // 오류
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BoardDto.ResponseIndexDto> responseDto = service.index(keywordType, keyword, pageable);
        List<BoardDto.ResponseIndexDto> contents = responseDto.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(responseDto.getTotalElements()).isEqualTo(3); // 전체 개수
        assertThat(contents.size()).isEqualTo(3); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: 등록자명 / 내용: Kang)")
    void index_search_keywordType_CREATEDBY_keyword() {
        // given
        accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);
        boards.forEach(dto -> service.create(dto));

        Account account1 = accountRepository.save(AccountDto.create("BaekKiSeon", "BaekKiSeon1234").toEntity());
        Account account2 = accountRepository.save(AccountDto.create("KimYoungHan", "KimYoungHan1234").toEntity());
        repository.save(new BoardTestDto.create("titleBaek", "contentBaek").toEntity(account1));
        repository.save(new BoardTestDto.create("titleKim", "contentKim").toEntity(account2));

        BoardDto.KeywordType keywordType = CREATEDBY;
        String keyword = "Kang";
//        String keyword = "Baek"; // 오류
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BoardDto.ResponseIndexDto> responseDto = service.index(keywordType, keyword, pageable);
        List<BoardDto.ResponseIndexDto> contents = responseDto.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(responseDto.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(10); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo("KangMinSung");
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 등록")
    void create()  {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(1);
        BoardDto dto = boards.get(0);

        // when
        BoardDto boardDto = service.create(dto);

        // then
        assertThat(boardDto.getTitle()).isEqualTo("title1");
        assertThat(boardDto.getContent()).isEqualTo("content1");
        assertThat(boardDto.getCreatedBy()).isEqualTo(username);
    }
}
