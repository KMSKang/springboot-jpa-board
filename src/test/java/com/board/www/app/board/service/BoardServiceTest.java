package com.board.www.app.board.service;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.dto.AccountDto;
import com.board.www.app.account.repository.AccountRepository;
import com.board.www.app.account.utils.AccountUtils;
import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.repository.BoardDslRepository;
import com.board.www.app.board.repository.BoardRepository;
import com.board.www.app.board.utils.BoardUtils;
import com.board.www.common.dto.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/boards/service")
@Transactional
@SpringBootTest
class BoardServiceTest extends BoardUtils {
    @Autowired private AccountUtils accountUtils;

    @Autowired private BoardService service;
    @Autowired private BoardRepository repository;
    @Autowired private BoardDslRepository dslRepository;
    @Autowired private AccountRepository accountRepository;

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트")
    void index() {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
//        PageRequest pageRequest = PageRequest.of(1, 10); // 오류
        Page<BoardDto> pageable = dslRepository.findAll(null, null, pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(11); // 전체 개수
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

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(1, 10);
//        PageRequest pageRequest = PageRequest.of(0, 10); // 오류
        Page<BoardDto> pageable = dslRepository.findAll(null, null, pageRequest);
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(11); // 전체 개수
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

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(null, "1", pageRequest);
//        Page<BoardDto> pageable = dslRepository.findAll(null, "2", pageRequest); // 오류
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(3); // 전체 개수
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

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(BoardDto.KeywordType.TITLE, "1", pageRequest);
//        Page<BoardDto> pageable = dslRepository.findAll(BoardDto.KeywordType.TITLE, "2", pageRequest); // 오류
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(3); // 전체 개수
        assertThat(contents.size()).isEqualTo(3); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo(username);
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 리스트 - (검색: 등록자명 / 내용: kang)")
    void index_search_keywordType_CREATEDBY_keyword() {
        // given
        accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(11);
        Account account1 = accountRepository.save(AccountDto.create("BaekKiSeon", "BaekKiSeon1234").toEntity());
        Account account2 = accountRepository.save(AccountDto.create("KimYoungHan", "KimYoungHan1234").toEntity());
        repository.save(BoardDto.create("titleBaek", "contentBaek").toEntity(account1));
        repository.save(BoardDto.create("titleKim", "contentKim").toEntity(account2));

        // when
        boards.forEach(dto -> service.insert(dto));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BoardDto> pageable = dslRepository.findAll(BoardDto.KeywordType.CREATEDBY, "Kang", pageRequest);
//        Page<BoardDto> pageable = dslRepository.findAll(BoardDto.KeywordType.CREATEDBY, "Baek", pageRequest); // 오류
        List<BoardDto> contents = pageable.getContent();
        BoardDto content = contents.get(0);

        // then
        assertThat(pageable.getTotalElements()).isEqualTo(11); // 전체 개수
        assertThat(contents.size()).isEqualTo(10); // 현재 페이지 개수
        assertThat(content.getTitle()).isEqualTo("title11");
        assertThat(content.getCreatedBy()).isEqualTo("KangMinSung");
    }

    @Test
    @WithMockAccount
    @DisplayName("게시판 등록")
    void insert()  {
        // given
        String username = accountRepository.save(accountUtils.givenAccount()).getUsername();
        List<BoardDto> boards = givenBoards(1);
        BoardDto dto = boards.get(0);

        // when
        BoardDto boardDto = service.insert(dto);

        // then
        assertThat(boardDto.getTitle()).isEqualTo("title1");
        assertThat(boardDto.getContent()).isEqualTo("content1");
        assertThat(boardDto.getCreatedBy()).isEqualTo(username);
    }
}
