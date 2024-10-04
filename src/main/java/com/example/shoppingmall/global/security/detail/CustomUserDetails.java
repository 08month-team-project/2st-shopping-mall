package com.example.shoppingmall.global.security.detail;

import com.example.shoppingmall.global.security.dto.UserDetailsDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String userEmail;
    private String userPassword;
    private Collection<GrantedAuthority> authorities;

    public CustomUserDetails(UserDetailsDTO dto) {
        userEmail = dto.getEmail();
        userPassword = dto.getPassword();
        authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) dto::getRole);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
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
}
