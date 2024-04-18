package com.sparta.wuzuzu.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // 아래 설정을 등록하여 활성화 합니다.
@EnableJpaAuditing // 시간 자동 변경이 가능하도록 합니다.
public class JpaConfig {
    // 메인 클래스에서 @EnableJpaAuditing 을 빼기 위한 Config 클래스
}