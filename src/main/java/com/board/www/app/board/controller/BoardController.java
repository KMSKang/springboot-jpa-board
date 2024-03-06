package com.board.www.app.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
public class BoardController {

    @GetMapping
    public String index() {
        return "app/board/index";
    }

    @GetMapping("/create")
    public String create() {
        return "app/board/create/index";
    }

    @GetMapping("/{id}")
    public String index(@PathVariable("id") Long id) {
        return "app/board/detail/index";
    }
}
