package com.board.www.common.security.cookie.ajax.handler;

import com.board.www.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED; // 인가 (로그인 X)
        response.setStatus(httpStatus.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(objectMapper.writeValueAsString(new ResponseDto<>(httpStatus, httpStatus.name(), "Unauthorized Exception")));
    }
}
