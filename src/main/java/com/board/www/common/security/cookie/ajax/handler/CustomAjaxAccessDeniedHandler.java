package com.board.www.common.security.cookie.ajax.handler;

import com.board.www.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAjaxAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN; // 인증 (권한)
        response.setStatus(httpStatus.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(objectMapper.writeValueAsString(new ResponseDto<>(httpStatus, httpStatus.name(), "Forbidden Exception")));
    }
}
