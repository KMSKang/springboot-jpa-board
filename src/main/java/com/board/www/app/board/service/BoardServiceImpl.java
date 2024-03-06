package com.board.www.app.board.service;

import com.board.www.app.account.domain.Account;
import com.board.www.app.account.service.AccountServiceImpl;
import com.board.www.app.board.domain.Board;
import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.dto.BoardResDto;
import com.board.www.app.board.repository.BoardRepository;
import com.board.www.common.exception.dto.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {
    private final AccountServiceImpl accountService;
    private final BoardRepository boardRepository;

//    @Cacheable(value = "boards", key="{#dto.page, #dto.keyword}")
    public Page<BoardResDto.Index> index(BoardDto.KeywordType keywordType, String keyword, Pageable pageable) {
        return boardRepository.searchIndex(keywordType, keyword, pageable);
    }

//    @CacheEvict(value = "boards", allEntries = true)
    @Transactional
    public BoardDto create(BoardDto dto) {
        Account account = accountService.getAccount();
        Board entity = dto.toEntity(account);
        return new BoardResDto.Create(boardRepository.save(entity));
    }

    public BoardResDto.Detail detail(Long id) {
        BoardResDto.Detail boardDto = boardRepository.searchDetail(id);

        // 관리자 삭제 체크
        if (boardDto == null) {
            throw new Exception500("존재하지 않는 게시물 입니다");
        }

        // 등록자 삭제 체크
        if (boardDto.getIsRemoved()) {
            throw new Exception500("삭제된 게시물 입니다");
        }

        // 비밀글 등록자 체크
        if (boardDto.getIsScret() && !accountService.getAccountId().equals(boardDto.getAccountId())) {
            throw new Exception500("비밀글 게시물 입니다");
        }

        return boardDto;
    }
}
