package com.board.www.common.security.cookie.ajax.handler;

import com.board.www.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAjaxLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String url = "/";
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        // 접근 제한이 있는 페이지
        if (savedRequest != null) {
            url = savedRequest.getRedirectUrl();
        }
        HttpStatus httpStatus = HttpStatus.OK;
        response.setStatus(httpStatus.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(objectMapper.writeValueAsString(new ResponseDto<>(httpStatus, httpStatus.name(), url)));
    }
}
