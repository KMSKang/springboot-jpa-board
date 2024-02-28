package com.board.www.app.accounts.controller.api;

import com.board.www.app.accounts.dto.AccountDto;
import com.board.www.app.accounts.service.AccountService;
import com.board.www.commons.dto.ResponseDto;
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
    public ResponseEntity<?> insert(@RequestBody @Valid AccountDto dto) {
        return ResponseEntity.ok(new ResponseDto<>(service.insert(dto)));
    }
}
