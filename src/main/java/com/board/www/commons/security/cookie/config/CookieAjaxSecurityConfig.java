package com.board.www.commons.security.cookie.config;

import com.board.www.commons.security.cookie.filter.LoginAuthenticationFilter;
import com.board.www.commons.security.cookie.handler.CustomAccessDeniedHandler;
import com.board.www.commons.security.cookie.handler.CustomAuthenticationHandler;
import com.board.www.commons.security.cookie.handler.ajax.CustomAjaxLoginFailureHandler;
import com.board.www.commons.security.cookie.handler.ajax.CustomAjaxLoginSuccessHandler;
import com.board.www.commons.security.cookie.provider.CustomAjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Configuration
public class CookieAjaxSecurityConfig {
    private final UserDetailsService userDetailsService; // 기본 로그인

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }

    @Bean
    public CustomAuthenticationHandler ajaxAuthenticationEntryPoint() {
        return new CustomAuthenticationHandler();
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new CustomAjaxLoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new CustomAjaxLoginFailureHandler();
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new CustomAjaxAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(ajaxAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
    }

    @Bean
    public LoginAuthenticationFilter ajaxLoginProcessingFilter(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter ajaxLoginProcessingFilter = new LoginAuthenticationFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager(http));
        ajaxLoginProcessingFilter.setSecurityContextRepository(securityContextRepository());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public SecurityFilterChain filterChainAjax(HttpSecurity http) throws Exception {
        // 접근 제한 페이지 접속 후 url?continue 삭제
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http.requestCache(request -> request.requestCache(requestCache)); // 접근 제한 페이지 접속 후 url?continue 삭제

        http.authorizeHttpRequests()
            .requestMatchers("/boards/**").hasRole("USER")
            .anyRequest().permitAll();

        http.formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/api/login");

        http.addFilterBefore(ajaxLoginProcessingFilter(http), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
            .authenticationEntryPoint(ajaxAuthenticationEntryPoint())
            .accessDeniedHandler(ajaxAccessDeniedHandler());

        http.rememberMe()
            .key("remember-me")
            .tokenValiditySeconds(86400 * 30)
            .alwaysRemember(true)
            .userDetailsService(userDetailsService);

        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .deleteCookies("remember-me", "JSESSIONID")
            .invalidateHttpSession(TRUE);

        return http.build();
    }
}
