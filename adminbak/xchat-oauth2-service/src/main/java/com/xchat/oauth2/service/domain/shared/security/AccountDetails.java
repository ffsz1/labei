package com.xchat.oauth2.service.domain.shared.security;

import com.xchat.oauth2.service.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by liuguofu on 2017/4/30.
 */
public class AccountDetails implements UserDetails {
    protected static final String ROLE_PREFIX = "ROLE_";
    protected static final GrantedAuthority DEFAULT_account_ROLE = new SimpleGrantedAuthority(ROLE_PREFIX + "account");

    protected Account account;

    public AccountDetails() {
    }

    public AccountDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.asList(DEFAULT_account_ROLE, new SimpleGrantedAuthority(ROLE_PREFIX + "UNITY"),
                new SimpleGrantedAuthority(ROLE_PREFIX + "MOBILE"));
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getErbanNo().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{account=").append(account);
        sb.append('}');
        return sb.toString();
    }
}
