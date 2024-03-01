package com.board.www.common.security.cookie.ajax.provider;

import com.board.www.common.exception.dto.Exception500;
import com.board.www.common.security.cookie.common.dto.AccountContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@RequiredArgsConstructor // -> config 때문에 사용 못함
@Component
public class CustomAjaxAuthenticationProvider implements AuthenticationProvider {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserDetailsService userDetailsService;
//    private final UserDetailsService userDetailsService; // -> config 때문에 사용 못함
//    private final PasswordEncoder passwordEncoder; // -> config 때문에 사용 못함

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);
        boolean isMatch = passwordEncoder.matches(password, accountContext.getAccount().getPassword());
        if (!isMatch) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        return new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
