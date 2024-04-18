package com.sparta.wuzuzu.global.security;

import com.sparta.wuzuzu.domain.admin.entity.Admin;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private User user;
    private Admin admin;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public UserDetailsImpl(Admin admin) {
        this.admin = admin;
    }

    public User getUser() {
        return user;
    }

    public Admin getAdmin() {
        return admin;
    }

    @Override
    public String getPassword() {
        if (user != null) {
            return user.getPassword();
        } else if (admin != null) {
            return admin.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (user != null) {
            return user.getEmail();
        } else if (admin != null) {
            return admin.getEmail();
        }
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role;
        if (user != null) {
            role = user.getRole();
        } else if (admin != null) {
            role = admin.getRole();
        } else {
            return null;
        }

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
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
