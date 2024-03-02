package com.board.www.common.security.cookie.ajax.config;

import com.board.www.common.security.cookie.ajax.filter.LoginAuthenticationFilter;
import com.board.www.common.security.cookie.ajax.handler.CustomAjaxAccessDeniedHandler;
import com.board.www.common.security.cookie.ajax.handler.CustomAjaxAuthenticationEntryPoint;
import com.board.www.common.security.cookie.ajax.handler.CustomAjaxLoginFailureHandler;
import com.board.www.common.security.cookie.ajax.handler.CustomAjaxLoginSuccessHandler;
import com.board.www.common.security.cookie.ajax.provider.CustomAjaxAuthenticationProvider;
import com.board.www.common.security.cookie.ajax.service.CustomAjaxUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static java.lang.Boolean.TRUE;

@Configuration
//@Order(2)
public class CookieAjaxSecurityConfig {
    @Bean
    public UserDetailsService ajaxUserDetailsService() {
        return new CustomAjaxUserDetailsService();
    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        return new CustomAjaxAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint ajaxAuthenticationEntryPoint() {
        return new CustomAjaxAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationSuccessHandler ajaxLoginSuccessHandler() {
        return new CustomAjaxLoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxLoginFailureHandler() {
        return new CustomAjaxLoginFailureHandler();
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new CustomAjaxAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(ajaxAuthenticationProvider());
        return builder.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("remember-me", ajaxUserDetailsService());
    }

    @Bean
    public LoginAuthenticationFilter ajaxLoginProcessingFilter(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager(http));
        filter.setAuthenticationSuccessHandler(ajaxLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(ajaxLoginFailureHandler());
        filter.setSecurityContextRepository(securityContextRepository());
//        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChainAjax(HttpSecurity http) throws Exception {
        // 접근 제한 페이지 접속 후 url?continue 삭제
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http.requestCache(request -> request.requestCache(requestCache)); // 접근 제한 페이지 접속 후 url?continue 삭제

//        http.csrf().ignoringRequestMatchers("/api/**");
//        http.csrf().disable();

        http.authorizeHttpRequests()
            .requestMatchers("/boards/**").hasRole("USER")
            .requestMatchers("/api/boards").hasRole("USER")
            .requestMatchers("/admin/**").hasRole("ADMIN")
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
            .userDetailsService(ajaxUserDetailsService());

        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .deleteCookies("remember-me", "JSESSIONID")
            .invalidateHttpSession(TRUE);

        return http.build();
    }
}
