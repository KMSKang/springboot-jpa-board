package com.board.www.app.boards.service;

import com.board.www.app.accounts.service.AccountService;
import com.board.www.app.boards.domain.Board;
import com.board.www.app.boards.dto.BoardDto;
import com.board.www.app.boards.repository.BoardDslRepository;
import com.board.www.app.boards.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final AccountService accountService;
    private final BoardRepository repository;
    private final BoardDslRepository dslRepository;

//    @Cacheable(value = "boards", key="{#dto.page, #dto.keyword}")
    public Page<BoardDto> index(BoardDto.KeywordType keywordType, String keyword, Pageable pageable) {
        return dslRepository.findAll(keywordType, keyword, pageable);
    }

//    @CacheEvict(value = "boards", allEntries = true)
    @Transactional
    public BoardDto insert(BoardDto dto) {
        Board entity = dto.toEntity();
        entity.setAccount(accountService.getAccount());
        return new BoardDto(repository.save(entity));
    }
}
