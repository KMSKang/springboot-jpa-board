//package com.board.www.commons.security.cookie.config;
//
//import com.board.www.commons.security.cookie.handler.CustomAccessDeniedHandler;
//import com.board.www.commons.security.cookie.handler.CustomAuthenticationHandler;
//import com.board.www.commons.security.cookie.handler.ajax.CustomAjaxLoginFailureHandler;
//import com.board.www.commons.security.cookie.handler.ajax.CustomAjaxLoginSuccessHandler;
//import com.board.www.commons.security.cookie.provider.CustomAjaxAuthenticationProvider;
//import com.board.www.commons.security.cookie.common.AjaxLoginConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static java.lang.Boolean.TRUE;
//
//@Configuration
//public class CookieAjaxSecurityConfig_JSON {
////    @Autowired private UserDetailsService userDetailsService; // 기본 로그인
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AccessDeniedHandler accessDeniedHandler() {
//        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
//        accessDeniedHandler.setErrorPage("/denied");
//        return accessDeniedHandler;
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler(){
//        return new CustomAjaxLoginSuccessHandler();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler(){
//        return new CustomAjaxLoginFailureHandler();
//    }
//
//    @Bean
//    public CustomAuthenticationHandler authenticationEntryPoint() {
//        return new CustomAuthenticationHandler();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        return new CustomAjaxAuthenticationProvider();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // 접근 제한 페이지 접속 후 url?continue 삭제
//        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//        requestCache.setMatchingRequestParameterName(null);
//        http.requestCache(request -> request.requestCache(requestCache));
//
//        // JSON LOGIN
//        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
//        AuthenticationManager authenticationManager = sharedObject.build();
//        http.authenticationManager(authenticationManager);
//
//        http.authorizeHttpRequests()
//            .requestMatchers("/boards/**").hasRole("USER")
//            .anyRequest().permitAll();
//
//        http.apply(new AjaxLoginConfigurer<>(authenticationProvider(), ajaxAuthenticationSuccessHandler(), ajaxAuthenticationFailureHandler()))
//            .loginPage("/login")
//            .loginProcessingUrl("/api/login");
//
//        http.exceptionHandling()
//            .authenticationEntryPoint(authenticationEntryPoint())
//            .accessDeniedHandler(accessDeniedHandler());
//
////        http.rememberMe()
////            .key("remember-me")
////            .tokenValiditySeconds(86400 * 30)
////            .alwaysRemember(true)
////            .userDetailsService(userDetailsService);
//
//        http.logout()
//            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//            .logoutSuccessUrl("/")
//            .deleteCookies("remember-me", "JSESSIONID")
//            .invalidateHttpSession(TRUE);
//
//        return http.build();
//    }
//}
