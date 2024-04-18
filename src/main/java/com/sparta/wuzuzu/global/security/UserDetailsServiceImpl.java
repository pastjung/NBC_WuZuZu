package com.sparta.wuzuzu.global.security;

import com.sparta.wuzuzu.domain.admin.entity.Admin;
import com.sparta.wuzuzu.domain.admin.repository.AdminRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.entity.UserRole;
import com.sparta.wuzuzu.domain.user.repository.UserRepository;
import com.sparta.wuzuzu.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null)
            return new UserDetailsImpl(user);

        Admin admin = adminRepository.findByEmail(email);
        if (admin != null)
            return new UserDetailsImpl(admin);

        throw new UsernameNotFoundException("Not Found " + email);
    }

    public UserDetails loadUserByClaims(Claims info) {
        User user = new User(info.getSubject(),
                UserRole.valueOf(info.get(JwtUtil.AUTHORIZATION_KEY).toString()));
        user.setUserId(info.get("userId", Long.class));

        return new UserDetailsImpl(user);
    }
}
