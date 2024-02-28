package com.board.www.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ResponseDto<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseDto(T data) {
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.name();
        this.data = data;
    }

    public ResponseDto(HttpStatus httpStatus, String msg, T data) {
        this.code = httpStatus.value();
        this.message = msg;
        this.data = data;
    }
}
