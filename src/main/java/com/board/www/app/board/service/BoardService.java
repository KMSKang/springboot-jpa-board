package com.board.www.app.board.service;

import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.dto.BoardResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Page<BoardResDto.Index> index(BoardDto.KeywordType keywordType, String keyword, Pageable pageable);
    BoardDto create(BoardDto dto);
    BoardResDto.Detail detail(Long id);
}
