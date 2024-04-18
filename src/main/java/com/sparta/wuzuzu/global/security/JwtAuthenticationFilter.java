package com.sparta.wuzuzu.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wuzuzu.domain.user.dto.LoginRequest;
import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.entity.UserRole;
import com.sparta.wuzuzu.global.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest requestDto = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        UserRole role;

        if (userDetails.getUser() != null) {
            role = userDetails.getUser().getRole();
        } else if (userDetails.getAdmin() != null) {
            role = userDetails.getAdmin().getRole();
        } else {
            throw new IllegalStateException("User or Admin not found");
        }

        // AccessToken 생성
        String accessToken = jwtUtil.createAccessToken(userDetails.getUser());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        // RefreshToken 생성
        String refreshToken = jwtUtil.createRefreshToken(userDetails.getUser());

        // 응답에 map형식으로 AccessToken, RefreshToken 추가
        setTokenResponse(response, accessToken, refreshToken, userDetails.getUser());
    }

    private void setTokenResponse(HttpServletResponse response, String accessToken, String refreshToken,
        User user) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("userId", user.getUserId());
        result.put("userName", user.getUserName());

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(result);
        response.getWriter().println(objectMapper.writeValueAsString(responseEntity.getBody()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
