//package com.board.www.common.security.cookie.ajax.config;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.MessageSourceAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
//
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 864000)
//@Configuration
//public class FrontSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Inject
//    private FrontUserDetailsService userDetailsService;
//    private final static String REMEMBER_ME_KEY = "KEY";
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .anonymous().disable()
//                .formLogin()
//                // 중략
//                .rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(tokenBasedRememberMeServices());
//        // 중략
//    }
//
//    @Bean
//    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
//        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
//        rememberMeServices.setAlwaysRemember(true);
//        rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 31);
//        rememberMeServices.setCookieName(Constants.SPRING_REMEMBER_ME_COOKIE);
//        return rememberMeServices;
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(new ShaPasswordEncoder(512));
//        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
//        return daoAuthenticationProvider;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }
//
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return userDetailsService;
//    }
//}
////        .rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(tokenBasedRememberMeServices())
////        .rememberMe().key()로 키 설정을 하지않으면 RemeberMeAuthenticationProvider에 key가 Random UUID로 들어가게 됩니다.
//
//public class RememberMeAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
//    // 중략
//    public RememberMeAuthenticationProvider(String key) {
//        this.key = key;
//    }
//}
//
//public final class RememberMeConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<RememberMeConfigurer<H>, H> {
//    private String getKey() {
//        if (key == null) {
//            key = UUID.randomUUID().toString();
//        }
//        return key;
//    }
//}
//
////그러다보니,RememberMeAuthenticationProvider에서 authenticate 할 때 hashCode 값이 맞지 않아 계속 쿠키가 삭제되고,자동 로그인이 되지 않습니다.
////public Authentication authenticate(Authentication authentication)throws AuthenticationException{
////        if(!supports(authentication.getClass())){
////        return null;
////        }
////        if(this.key.hashCode()!=((RememberMeAuthenticationToken)authentication).getKeyHash()){
////        throw new BadCredentialsException(messages.getMessage("RememberMeAuthenticationProvider.incorrectKey",
////        "The presented RememberMeAuthenticationToken does not contain the expected key"));
////        }
////        return authentication;
////        }