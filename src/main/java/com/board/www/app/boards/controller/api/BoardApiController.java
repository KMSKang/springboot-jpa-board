package com.board.www.app.boards.controller.api;

import com.board.www.app.boards.dto.BoardDto;
import com.board.www.app.boards.service.BoardService;
import com.board.www.commons.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardApiController {
    private final BoardService service;

    @GetMapping
    public ResponseEntity<?> index(BoardDto.KeywordType keywordType, String keyword, Pageable pageable) {
        return ResponseEntity.ok(new ResponseDto<>(service.index(keywordType, keyword, pageable)));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestPart @Valid BoardDto dto
                                  , @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(new ResponseDto<>(service.insert(dto)));
    }
}
