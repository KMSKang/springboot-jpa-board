package com.board.www.app.board.repository;

import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.dto.BoardResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardDslRepository {
    Page<BoardResDto.Index> searchIndex(BoardDto.KeywordType keywordType, String keyword, Pageable pageable);
    BoardResDto.Detail searchDetail(Long id);
}
