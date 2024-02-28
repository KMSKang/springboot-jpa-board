package com.board.www.commons.exception.handler;

import com.board.www.commons.exception.dto.Exception500;
import com.board.www.commons.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        ResponseDto responseDto = new ResponseDto(httpStatus, httpStatus.name(), errors);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(Exception500.class)
    public final ResponseEntity<Object> exception500(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = new ResponseDto(httpStatus, httpStatus.name(), ex.getMessage());
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> constraintViolation(ConstraintViolationException ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(error -> errors.add("[ConstraintViolationException]: " + error.getMessage()));

        ResponseDto responseDto = new ResponseDto(httpStatus, httpStatus.name(), errors);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public final ResponseEntity<Object> unexpectedTypeException(UnexpectedTypeException ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = new ResponseDto(httpStatus, httpStatus.name(), "UnexpectedTypeException");
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> exception(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = new ResponseDto(httpStatus, httpStatus.name(), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(responseDto, httpStatus);
    }
}
