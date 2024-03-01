package com.board.www.common.dto;

import com.board.www.app.account.domain.Account;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomWithSecurityContextFactory.class)
public @interface WithMockAccount {
    long id() default 1L;
    Account.Role role () default Account.Role.USER;
    String username() default "KangMinSung";
    String password() default "!!1q2w3e4r";
}
