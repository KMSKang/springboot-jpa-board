package com.board.www.app.board.service;

import com.board.www.app.account.service.AccountService;
import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.repository.BoardDslRepository;
import com.board.www.app.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
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
    public Page<BoardDto.ResponseIndexDto> index(BoardDto.KeywordType keywordType, String keyword, Pageable pageable) {
        return dslRepository.findAll(keywordType, keyword, pageable);
    }

//    @CacheEvict(value = "boards", allEntries = true)
    @Transactional
    public BoardDto create(BoardDto dto) {
        return new BoardDto.ResponseCreateDto(repository.save(dto.toEntity(accountService.getAccount())));
    }
}
