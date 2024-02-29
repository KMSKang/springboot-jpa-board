//package com.board.www.commons.security.cookie.config;
//
//import com.board.www.commons.security.cookie.common.FormAuthenticationDetailsSource;
//import com.board.www.commons.security.cookie.handler.CustomAccessDeniedHandler;
//import com.board.www.commons.security.cookie.handler.CustomAuthenticationHandler;
//import com.board.www.commons.security.cookie.handler.form.CustomFormLoginFailureHandler;
//import com.board.www.commons.security.cookie.handler.form.CustomFormLoginSuccessHandler;
//import com.board.www.commons.security.cookie.provider.CustomFormAuthenticationProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
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
//@Order(1)
//public class CookieFormSecurityConfig {
//  private final UserDetailsService userDetailsService; // 기본 로그인
//  private final FormAuthenticationDetailsSource authenticationDetailsSource; // 접근 권한 핸들러
//
////  @Bean
////  public PasswordEncoder passwordEncoder() {
////    return new BCryptPasswordEncoder();
////  }
//
//  @Bean
//  public AccessDeniedHandler formAccessDeniedHandler() {
//    CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
//    accessDeniedHandler.setErrorPage("/denied");
//    return accessDeniedHandler;
//  }
//
//  @Bean
//  public CustomAuthenticationHandler formAuthenticationEntryPoint() {
//    return new CustomAuthenticationHandler();
//  }
//
//  @Bean
//  public AuthenticationSuccessHandler formAuthenticationSuccessHandler() {
//    return new CustomFormLoginSuccessHandler();
//  }
//
//  @Bean
//  public AuthenticationFailureHandler formAuthenticationFailureHandler() {
//    return new CustomFormLoginFailureHandler();
//  }
//
//  @Bean
//  public AuthenticationProvider formAuthenticationProvider() {
//    return new CustomFormAuthenticationProvider();
//  }
//
//  /**
//   * 아이디 및 비밀번호 체크 후 접속
//   */
////  @Override
////  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////    auth.userDetailsService(userDetailsService);
////  }
//
//  /**
//   * resource 권한 체크 패스
//   */
////  @Override
////  public void configure(WebSecurity web) {
////    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
////  }
//
//
//  @Bean
//  public SecurityFilterChain filterChainForm(HttpSecurity http) throws Exception {
//    // 접근 제한 페이지 접속 후 url?continue 삭제
//    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//    requestCache.setMatchingRequestParameterName(null);
//
//    http.authorizeHttpRequests()
//        .requestMatchers("/boards/**").hasRole("USER")
//        .anyRequest().permitAll();
//
//    http.requestCache(request -> request.requestCache(requestCache)); // 접근 제한 페이지 접속 후 url?continue 삭제
//
//    http.formLogin()
//        .loginPage("/login")
//        .loginProcessingUrl("/login")
//        .authenticationDetailsSource(authenticationDetailsSource)
//        .successHandler(formAuthenticationSuccessHandler())
//        .failureHandler(formAuthenticationFailureHandler())
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
//        .userDetailsService(userDetailsService);
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
