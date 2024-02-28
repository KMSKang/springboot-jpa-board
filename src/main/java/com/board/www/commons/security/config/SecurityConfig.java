package com.board.www.commons.security.config;

import com.board.www.commons.security.common.FormAuthenticationDetailsSource;
import com.board.www.commons.security.handler.CustomAccessDeniedHandler;
import com.board.www.commons.security.handler.CustomAuthenticationHandler;
import com.board.www.commons.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
  private final UserDetailsService userDetailsService; // 기본 로그인
  private final FormAuthenticationDetailsSource authenticationDetailsSource; // 접근 권한 핸들러
  private final AuthenticationSuccessHandler authenticationSuccessHandler; // 로그인 성공 핸들러
  private final AuthenticationFailureHandler authenticationFailureHandler; // 로그인 실패 핸들러
//  private final AuthenticationDetailsSource authenticationDetailsSource; // secret 보안 설정
//  private final PrincipalOauth2UserService principalOauth2UserService; // 소셜 로그인
//  private final AuthenticationEntryPoint authenticationEntryPoint; // 인증 핸들러

  /**
   * 비밀번호 암호화
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 접근권한
   */
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
    accessDeniedHandler.setErrorPage("/denied");
    return accessDeniedHandler;
  }

  @Bean
  public CustomAuthenticationHandler authenticationEntryPoint() {
    return new CustomAuthenticationHandler();
  }

  /**
   * 아이디 및 비밀번호 체크 후 접속 (빈)
   */
//  @Bean
//  public AuthenticationProvider authenticationProvider() {
//    return new CustomAuthenticationProvider();
//  }

  /**
   * 아이디 및 비밀번호 체크 후 접속
   */
//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.userDetailsService(userDetailsService);
//  }

  /**
   * resource 권한 체크 패스
   */
//  @Override
//  public void configure(WebSecurity web) {
//    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 접근 제한 페이지 접속 후 url?continue 삭제
    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    requestCache.setMatchingRequestParameterName(null);

    http.authorizeHttpRequests().requestMatchers("/boards/**").hasRole("USER")
                                .anyRequest().permitAll();

    http.requestCache(request -> request.requestCache(requestCache)); // 접근 제한 페이지 접속 후 url?continue 삭제

    http.formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .authenticationDetailsSource(authenticationDetailsSource)
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
        .permitAll();

//    http.authenticationProvider(authenticationProvider());

    http.exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler());

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

//    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
