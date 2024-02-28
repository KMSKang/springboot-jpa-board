package com.board.www.app.boards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @GetMapping
    public String index() {
        return "boards/index";
    }

    @GetMapping("/insert")
    public String insert() {
        return "boards/insert/index";
    }
}
