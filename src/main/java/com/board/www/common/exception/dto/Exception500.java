package com.board.www.common.exception.dto;

import lombok.Getter;

@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }
}
