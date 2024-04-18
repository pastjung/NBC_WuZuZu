package com.sparta.wuzuzu.global.jwt;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenBlacklist {
    private Set<String> blacklist = new HashSet<>();

    // 토큰 추가
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
