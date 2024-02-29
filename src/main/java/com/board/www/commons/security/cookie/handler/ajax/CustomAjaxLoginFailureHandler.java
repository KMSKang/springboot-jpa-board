package com.board.www.commons.security.cookie.handler.ajax;

import com.board.www.commons.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAjaxLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		String message = exception.getMessage();
		if(("자격 증명에 실패하였습니다.").equals(exception.getMessage())) {
			message = "비밀번호가 일치하지 않습니다";
		}

		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		mapper.writeValue(response.getWriter(), new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", message));
	}
}
