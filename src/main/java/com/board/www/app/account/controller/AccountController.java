package com.board.www.app.account.controller;

import com.board.www.app.account.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountServiceImpl service;

    @GetMapping
    public String index() {
        if (service.isLogin()) {
            return "redirect:/";
        }
        return "app/account/index";
    }
}

