package com.board.www.app.account.controller.api;

import com.board.www.app.account.dto.AccountDto;
import com.board.www.app.account.service.AccountService;
import com.board.www.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountApiController {
    private final AccountService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid AccountDto dto) {
        return ResponseEntity.ok(new ResponseDto<>(service.create(dto)));
    }
}
