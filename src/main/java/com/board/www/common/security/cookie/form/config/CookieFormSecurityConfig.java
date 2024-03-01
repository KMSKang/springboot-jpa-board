//package com.board.www.common.security.cookie.form.config;
//
//import com.board.www.common.security.cookie.form.source.FormAuthenticationDetailsSource;
//import com.board.www.common.security.cookie.form.handler.CustomFormAccessDeniedHandler;
//import com.board.www.common.security.cookie.form.handler.CustomFormAuthenticationEntryPoint;
//import com.board.www.common.security.cookie.form.handler.CustomFormLoginFailureHandler;
//import com.board.www.common.security.cookie.form.handler.CustomFormLoginSuccessHandler;
//import com.board.www.common.security.cookie.form.provider.CustomFormAuthenticationProvider;
//import com.board.www.common.security.cookie.form.service.CustomFormUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static java.lang.Boolean.TRUE;
//
//@RequiredArgsConstructor
//@Configuration
////@Order(1)
//public class CookieFormSecurityConfig {
//  private final FormAuthenticationDetailsSource authenticationDetailsSource; // 접근 권한 핸들러
//
//  @Bean
//  public UserDetailsService formUserDetailsService() {
//    return new CustomFormUserDetailsService();
//  }
//
//  @Bean
//  public AccessDeniedHandler formAccessDeniedHandler() {
//    return new CustomFormAccessDeniedHandler();
//  }
//
//  @Bean
//  public AuthenticationEntryPoint formAuthenticationEntryPoint() {
//    return new CustomFormAuthenticationEntryPoint();
//  }
//
//  @Bean
//  public AuthenticationSuccessHandler formLoginSuccessHandler() {
//    return new CustomFormLoginSuccessHandler();
//  }
//
//  @Bean
//  public AuthenticationFailureHandler formLoginFailureHandler() {
//    return new CustomFormLoginFailureHandler();
//  }
//
//  @Bean
//  public AuthenticationProvider formAuthenticationProvider() {
//    return new CustomFormAuthenticationProvider();
//  }
//
//  @Bean
//  public SecurityFilterChain filterChainForm(HttpSecurity http) throws Exception {
//    // 접근 제한 페이지 접속 후 url?continue 삭제
//    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//    requestCache.setMatchingRequestParameterName(null);
//
//    http.authorizeHttpRequests()
//        .requestMatchers("/api/boards").hasRole("USER")
//        .requestMatchers("/boards/**").hasRole("USER")
//        .requestMatchers("/admin/**").hasRole("ADMIN")
//        .anyRequest().permitAll();
//
//    http.requestCache(request -> request.requestCache(requestCache)); // 접근 제한 페이지 접속 후 url?continue 삭제
//
//    http.formLogin()
//        .loginPage("/login")
//        .loginProcessingUrl("/login")
//        .authenticationDetailsSource(authenticationDetailsSource)
//        .successHandler(formLoginSuccessHandler())
//        .failureHandler(formLoginFailureHandler())
//        .permitAll();
//
//    http.authenticationProvider(formAuthenticationProvider()); // 실행 정상
//
//    http.exceptionHandling()
//        .authenticationEntryPoint(formAuthenticationEntryPoint())
//        .accessDeniedHandler(formAccessDeniedHandler());
//
//    http.rememberMe()
//        .key("remember-me")
//        .tokenValiditySeconds(86400 * 30)
//        .alwaysRemember(true)
////        .userDetailsService(userDetailsService);
//        .userDetailsService(formUserDetailsService());
//
//    http.logout()
//        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//        .logoutSuccessUrl("/")
//        .deleteCookies("remember-me", "JSESSIONID")
//        .invalidateHttpSession(TRUE);
//
//    return http.build();
//  }
//}
