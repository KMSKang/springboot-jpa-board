//package com.board.www.common.security.cookie.ajax.config;
//
//import com.board.www.common.security.cookie.ajax.filter.LoginAuthenticationFilter;
//import com.board.www.common.security.cookie.ajax.handler.CustomAjaxAccessDeniedHandler;
//import com.board.www.common.security.cookie.ajax.handler.CustomAjaxAuthenticationEntryPoint;
//import com.board.www.common.security.cookie.ajax.handler.CustomAjaxLoginFailureHandler;
//import com.board.www.common.security.cookie.ajax.handler.CustomAjaxLoginSuccessHandler;
//import com.board.www.common.security.cookie.ajax.provider.CustomAjaxAuthenticationProvider;
//import com.board.www.common.security.cookie.ajax.service.CustomAjaxUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.RememberMeAuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.RememberMeServices;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
//import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
//import org.springframework.security.web.context.DelegatingSecurityContextRepository;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static java.lang.Boolean.TRUE;
//
//@Configuration
////@Order(2)
//public class CookieAjaxSecurityConfigTest {
//    @Bean
//    public UserDetailsService ajaxUserDetailsService() {
//        return new CustomAjaxUserDetailsService();
//    }
//
//    @Bean
//    public AccessDeniedHandler ajaxAccessDeniedHandler() {
//        return new CustomAjaxAccessDeniedHandler();
//    }
//
//    @Bean
//    public AuthenticationEntryPoint ajaxAuthenticationEntryPoint() {
//        return new CustomAjaxAuthenticationEntryPoint();
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler ajaxLoginSuccessHandler() {
//        return new CustomAjaxLoginSuccessHandler();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler ajaxLoginFailureHandler() {
//        return new CustomAjaxLoginFailureHandler();
//    }
//
//    @Bean
//    public AuthenticationProvider ajaxAuthenticationProvider() {
//        return new CustomAjaxAuthenticationProvider();
//    }
//
//    // [불필요] authenticationManager() 메서드에서 설정 가능
////    @Bean
////    public DaoAuthenticationProvider daoAuthenticationProvider() {
////        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
////        daoAuthenticationProvider.setUserDetailsService(ajaxUserDetailsService());
////        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
////        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
////        return daoAuthenticationProvider;
////    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
//        auth.authenticationProvider(ajaxAuthenticationProvider());
//
//        // 아래에서 설정 따로 설정하나 설정 자체는 똑같음..
////        auth.authenticationProvider(daoAuthenticationProvider());
//
//        // 1. (정상) CustomAjaxAuthenticationProvider authenticate() 메서드 호출
//        // 2. (정상) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="a"
//        // 3. (오류) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="com.board.www.app.account.domain.Account@1926f31a"
////        auth.authenticationProvider(ajaxAuthenticationProvider());
//
//
//        // 1. (정상) CustomAjaxAuthenticationProvider authenticate() 메서드 호출
//        // 2. (정상) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="a"
//        // 3. (오류) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="com.board.www.app.account.domain.Account@1926f31a"
////        auth.authenticationProvider(ajaxAuthenticationProvider())
////            .userDetailsService(ajaxUserDetailsService())
////            .passwordEncoder(new BCryptPasswordEncoder());
//
//
//        // 1. (비정상) CustomAjaxAuthenticationProvider authenticate() 메서드 호출 안함
//        // 2. (정상) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="a"
//        // 3. (정상) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="a"
//        // 4. (정상) remember-me 쿠키 저장됨
////        auth.authenticationProvider(rememberMeAuthenticationProvider())
////            .userDetailsService(ajaxUserDetailsService())
////            .passwordEncoder(new BCryptPasswordEncoder());
//
//
//        // 위 모두 주석처리 시
//        // 1. (정상) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="a"
//        // 2. (정상) CustomAjaxUserDetailsService loadUserByUsername() 메서드 호출 username="a"
//        // 3. (정상) remember-me 쿠키 저장됨
//
//
//        return auth.build();
//    }
//
//    // 쿠키
//    @Bean
//    public SecurityContextRepository securityContextRepository() {
//        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
//    }
//
//    // 쿠키
//    @Bean
//    public RememberMeServices rememberMeServices() {
//        return new TokenBasedRememberMeServices("remember-me", ajaxUserDetailsService());
//    }
//
//    @Bean
//    public LoginAuthenticationFilter ajaxLoginProcessingFilter(HttpSecurity http) throws Exception {
//        LoginAuthenticationFilter ajaxLoginProcessingFilter = new LoginAuthenticationFilter();
//        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager(http));
//        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxLoginSuccessHandler());
//        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxLoginFailureHandler());
//
//        // 쿠키
//        ajaxLoginProcessingFilter.setSecurityContextRepository(securityContextRepository()); // [필수] 쿠키
//        ajaxLoginProcessingFilter.setRememberMeServices(rememberMeServices()); // [필수] 쿠키
//
//        return ajaxLoginProcessingFilter;
//    }
//
//    // [불필요]
////    @Bean
////    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter(HttpSecurity http) throws Exception {
////        return new RememberMeAuthenticationFilter(authenticationManager(http), rememberMeServices());
////    }
//
//    // [불필요]
////    @Bean
////    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
////        return new RememberMeAuthenticationProvider("remember-me");
////    }
//
//    @Bean
//    public SecurityFilterChain filterChainAjax(HttpSecurity http) throws Exception {
//        // 접근 제한 페이지 접속 후 url?continue 삭제
//        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//        requestCache.setMatchingRequestParameterName(null);
//        http.requestCache(request -> request.requestCache(requestCache)); // 접근 제한 페이지 접속 후 url?continue 삭제
//
//        http.authorizeHttpRequests()
//            .requestMatchers("/api/**").hasRole("USER")
//            .requestMatchers("/boards/**").hasRole("USER")
//            .requestMatchers("/admin/**").hasRole("ADMIN")
//            .anyRequest().permitAll();
//
//        http.formLogin()
//            .loginPage("/login")
//            .loginProcessingUrl("/api/login");
//
//        // [삭제] authenticationManager에 설정한 것과 동일함 (위 확인)
////        http.authenticationProvider(ajaxAuthenticationProvider());
//
//        // 쿠키
//        http.addFilterBefore(ajaxLoginProcessingFilter(http), UsernamePasswordAuthenticationFilter.class);
//
//        // [필요 없음]
////        http.setSharedObject(LoginAuthenticationFilter.class, ajaxLoginProcessingFilter(http));
//
//        // [필요 없음]
////        http.addFilter(rememberMeAuthenticationFilter(http));
//
//        // [삭제] authenticationManager에 설정한 것과 동일함 (위 확인)
////        http.authenticationProvider(ajaxAuthenticationProvider());
//
//        http.exceptionHandling()
//            .authenticationEntryPoint(ajaxAuthenticationEntryPoint())
//            .accessDeniedHandler(ajaxAccessDeniedHandler());
//
//        // 페이지 로딩 시 쿠키가 있을 경우 로그인 시동 ~ ~ ~
//        http.rememberMe()
//            .key("remember-me")
//            .tokenValiditySeconds(86400 * 30)
//            .alwaysRemember(true)
//            .userDetailsService(ajaxUserDetailsService());
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
