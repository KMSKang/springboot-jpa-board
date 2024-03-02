package com.board.www.common.security.cookie.ajax.filter;

import com.board.www.app.account.dto.AccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired private Validator validator;

    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String method = request.getMethod();
        if (!method.equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        ServletInputStream inputStream = request.getInputStream();
        AccountDto dto = new ObjectMapper().readValue(inputStream, AccountDto.class);

        // 유효성 검사
        Set<ConstraintViolation<AccountDto>> validate = validator.validate(dto);
        Iterator<ConstraintViolation<AccountDto>> iterator = validate.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<AccountDto> next = iterator.next();
            throw new BadRequestException(next.getMessage());
        }

        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
    }
}