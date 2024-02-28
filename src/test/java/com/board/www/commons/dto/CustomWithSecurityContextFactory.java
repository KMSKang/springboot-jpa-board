package com.board.www.commons.dto;

import com.board.www.app.accounts.domain.Account;
import com.board.www.commons.security.dto.AccountContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomWithSecurityContextFactory implements WithSecurityContextFactory<WithMockAccount> {
    @Override
    public SecurityContext createSecurityContext(WithMockAccount withMockAccount) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Account account = Account.builder()
                                 .id(withMockAccount.id())
                                 .role(withMockAccount.role())
                                 .username(withMockAccount.username())
                                 .password(withMockAccount.password())
                                 .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole().getName()));
        AccountContext accountMockDetails = new AccountContext(account, roles);
        Authentication auth = new UsernamePasswordAuthenticationToken(accountMockDetails, accountMockDetails.getPassword(), accountMockDetails.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
