package com.sparta.wuzuzu.global.security;

import com.sparta.wuzuzu.global.jwt.JwtTokenBlacklist;
import com.sparta.wuzuzu.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenBlacklist jwtTokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);
        if (!StringUtils.hasText(tokenValue)) {
            filterChain.doFilter(req, res);
            return;
        }

        if (StringUtils.hasText(tokenValue)) {

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Access Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            if(jwtTokenBlacklist.isBlacklisted(tokenValue)){
                log.info("로그아웃 된 토큰입니다.");
                return;
            }

            // 만료된 토큰일 경우 RefreshToken 검사
            if (!jwtUtil.isAccessTokenExpired(tokenValue)) { // 만료되지 않은 경우에만 RefreshToken 검사 수행
                String refreshTokenValue = jwtUtil.getRefreshToken(info.getSubject());
                if (StringUtils.hasText(refreshTokenValue) && jwtUtil.validateToken(refreshTokenValue)) {
                    // 새로운 accessToken 생성
                    log.info("새로운 accessToken 생성");
                    String newAccessToken = jwtUtil.generateAccessTokenFromRefreshToken(refreshTokenValue);
                    tokenValue = newAccessToken.substring(7);
                    info = jwtUtil.getUserInfoFromToken(tokenValue);
                    // 새로운 accessToken을 클라이언트에 반환
                    res.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
                } else {
                    log.error("RefreshToken이 유효하지 않습니다.");
                    return;
                }
            }

            try {
                setAuthentication(info);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(Claims info) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(info);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(Claims info) {
        UserDetails userDetails = userDetailsService.loadUserByClaims(info);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
