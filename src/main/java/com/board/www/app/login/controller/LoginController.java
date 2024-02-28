package com.board.www.app.login.controller;

import com.board.www.app.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final AccountService service;

    @GetMapping
    public String index() {
        if (service.isLogin()) {
            return "redirect:/";
        }
        return "login/index";
    }
}
