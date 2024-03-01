package com.board.www.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/denied")
public class ErrorController {

    @GetMapping
    public String index() {
        return "common/error/index";
    }
}
