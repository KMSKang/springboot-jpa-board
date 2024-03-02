package com.board.www.common.exception.handler;

import com.board.www.common.dto.ResponseDto;
import com.board.www.common.exception.dto.Exception500;
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

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null).getDefaultMessage();
        ResponseDto responseDto = new ResponseDto(httpStatus, message, null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(Exception500.class)
    public final ResponseEntity<Object> exception500(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = new ResponseDto(httpStatus, ex.getMessage(), null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    // @NotNull, @NotBlank
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> constraintViolation(ConstraintViolationException ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getConstraintViolations().stream().findFirst().orElse(null).getMessage();
        ResponseDto responseDto = new ResponseDto(httpStatus, message, null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public final ResponseEntity<Object> unexpectedTypeException() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = new ResponseDto(httpStatus, "UnexpectedType Exception", null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> exception(Exception exception) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = new ResponseDto(httpStatus, "InternalServer Exception", null);
        return new ResponseEntity<>(responseDto, httpStatus);
    }
}
