package com.board.www.common.security.cookie.ajax.handler;

import com.board.www.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAjaxLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		String message = exception.getMessage();

		// 아이디 오류
//		if (exception instanceof InternalAuthenticationServiceException) {
//			//
//		}

		// 비밀번호 오류
		if (exception instanceof BadCredentialsException) {
			message = "비밀번호가 일치하지 않습니다";
		}

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		response.setStatus(httpStatus.value());
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(objectMapper.writeValueAsString(new ResponseDto<>(httpStatus, message, null)));
	}
}
