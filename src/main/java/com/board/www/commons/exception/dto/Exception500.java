package com.board.www.commons.exception.dto;

import lombok.Getter;

// 서버 에러
@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }
}
