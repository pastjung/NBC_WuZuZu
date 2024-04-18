package com.sparta.wuzuzu.global.jwt;

import com.sparta.wuzuzu.domain.user.entity.User;
import com.sparta.wuzuzu.domain.user.entity.UserRole;
import com.sparta.wuzuzu.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 1000L;
    private final RedisTemplate<String, String> refreshTokenRedisTemplate;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtUtil(@Qualifier("refreshTokenRedisTemplate") RedisTemplate<String, String> refreshTokenRedisTemplate) {
        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // ACCESS 토큰 생성
    public String createAccessToken(User user) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(user.getEmail())
                        .claim("userId",user.getUserId())
                        .claim(AUTHORIZATION_KEY, user.getRole())
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // REFRESH 토큰 생성
    public String createRefreshToken(User user) {
        Date date = new Date();

        String refreshToken = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(user.getEmail())
                        .claim("userId",user.getUserId())
                        .claim(AUTHORIZATION_KEY, user.getRole())
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();

        // redis에 저장
        refreshTokenRedisTemplate.opsForValue().set(
                user.getEmail(),
                refreshToken,
                REFRESH_TOKEN_TIME,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public String getRefreshToken(String email) {
        return refreshTokenRedisTemplate.opsForValue().get(email);
    }

    // RefreshToken으로 AccessToken 재발급
    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        // refreshToken에서 사용자 정보 추출
        Claims refreshTokenClaims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(refreshToken).getBody();
        String email = refreshTokenClaims.getSubject();
        UserRole role = UserRole.valueOf(
                (String) refreshTokenClaims.get(AUTHORIZATION_KEY));
        User user = new User(email, role);

        // 새로운 accessToken 생성
        return createAccessToken(user);
    }

    // 토큰 만료 확인
    public boolean isAccessTokenExpired(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date()); // 토큰 만료 시간과 현재 시간을 비교하여 유효 여부 반환
        } catch (ExpiredJwtException e) {
            return false; // 만료된 토큰
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false; // 유효하지 않은 토큰
        }
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
