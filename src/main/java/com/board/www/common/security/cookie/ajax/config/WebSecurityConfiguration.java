//package com.board.www.common.security.cookie.ajax.config;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDecisionManager;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.RememberMeAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.RememberMeServices;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
//import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import java.io.IOException;
//import java.lang.reflect.Member;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private MemberUserDetailsService memberUserDetailsService;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AccessDecisionManager accessDecisionManager;
//
//    @Autowired
//    private ApplicationEventPublisher eventPublisher;
//
//    @Autowired
//    private CsrfTokenRepository csrfTokenRepository;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //@formatter:off
//        http.headers().cacheControl().and().and()
//            .csrf().csrfTokenRepository(csrfTokenRepository()).and()
//            .rememberMe().key("myKey").tokenValiditySeconds(60*60*24*7).userDetailsService(memberUserDetailsService).and()
//            .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
//            .formLogin().loginProcessingUrl("/api/signin").failureHandler(authenticationFailureHandler()).successHandler(authenticationSuccessHandler()).and()
//            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/api/signout")).logoutSuccessHandler(logoutSuccessHandler()).and()
//            .addFilter(usernamePasswordAuthenticationFilter())
//            .addFilter(rememberMeAuthenticationFilter())
//            .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//            .authorizeRequests().accessDecisionManager(accessDecisionManager).antMatchers("/resources/**", "/**").permitAll().anyRequest().authenticated();
//        //@formatter:on
//    }
//
//    private LogoutSuccessHandler logoutSuccessHandler() {
//        return new LogoutSuccessHandler() {
//            @Override
//            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                response.setStatus(HttpStatus.OK.value());
//            }
//        };
//    }
//
//    private AccessDeniedHandler accessDeniedHandler() {
//        return new AccessDeniedHandler() {
//            @Override
//            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//                // TODO: deal with InvalidCsrfTokenException & MissingCsrfTokenException
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//            }
//        };
//    }
//
//    private AuthenticationFailureHandler authenticationFailureHandler() {
//        return new AuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            }
//        };
//    }
//
//    private AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                response.setStatus(HttpStatus.OK.value());
//                Member member = (Member) authentication.getPrincipal();
//                eventPublisher.publishEvent(new SigninApplicationEvent(member));
//                response.setStatus(HttpStatus.OK.value());
//                // TODO: overhaul below
//                response.addHeader("MEMBER_ROLE", member.getRole().name());
//            }
//        };
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(rememberMeAuthenticationProvider())
//            .userDetailsService(memberUserDetailsService)
//            .passwordEncoder(passwordEncoder);
//    }
//
//    @Bean
//    protected CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        repository.setHeaderName("X-XSRF-TOKEN");
//        return repository;
//    }
//
//    @Bean
//    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
//        return new RememberMeAuthenticationProvider("myKey");
//    }
//
//    @Bean
//    public RememberMeServices rememberMeServices() {
//        return new TokenBasedRememberMeServices("myKey", memberUserDetailsService);
//    }
//
//    @Bean
//    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
//        return new RememberMeAuthenticationFilter(authenticationManager(), rememberMeServices());
//    }
//
//    @Bean
//    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
//        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
//        filter.setRememberMeServices(rememberMeServices());
//        filter.setAuthenticationManager(authenticationManager());
//        return filter;
//    }
//
//}